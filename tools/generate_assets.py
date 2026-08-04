"""Generate original deterministic pixel art, procedural audio, and the banya NBT."""
from __future__ import annotations

import gzip
import math
import random
import struct
import subprocess
import wave
from pathlib import Path

from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "src/main/resources/assets/russian_survival"
TEX = ASSETS / "textures"
SOUNDS = ASSETS / "sounds"
RNG = random.Random(20260804)


def save_pixel(name: str, paint, size=(16, 16)) -> None:
    path = TEX / name
    path.parent.mkdir(parents=True, exist_ok=True)
    image = Image.new("RGBA", size, (0, 0, 0, 0))
    paint(ImageDraw.Draw(image), image)
    image.save(path)


def stipple(draw, box, colors, chance=0.28):
    x0, y0, x1, y1 = box
    for y in range(y0, y1 + 1):
        for x in range(x0, x1 + 1):
            if RNG.random() < chance:
                draw.point((x, y), fill=RNG.choice(colors))


def item_textures():
    def fur(d, _):
        d.polygon([(3,3),(7,2),(12,4),(13,9),(10,14),(5,13),(2,9)], fill="#694129")
        stipple(d, (3,3,12,13), ["#8c6040", "#b1845b", "#4a2c1d"], .42)
    save_pixel("item/bear_fur.png", fur)

    def ushanka(d, _):
        d.rectangle((4,3,11,9), fill="#6d3d24"); d.rectangle((2,7,4,13), fill="#56301f"); d.rectangle((11,7,13,13), fill="#56301f")
        d.rectangle((5,2,10,4), fill="#a87849"); d.rectangle((5,7,10,9), fill="#bf9361"); stipple(d,(4,3,11,9),["#82502f","#4d291b"],.3)
    save_pixel("item/ushanka.png", ushanka)

    def coat(d, _):
        d.polygon([(5,2),(10,2),(13,6),(11,8),(10,6),(11,14),(4,14),(5,6),(3,8),(1,6)], fill="#704529")
        d.line((7,3,7,14), fill="#c79a68"); d.rectangle((5,2,9,4), fill="#c79a68"); stipple(d,(4,5,10,13),["#8d613d","#52321f"],.25)
    save_pixel("item/fur_coat.png", coat)

    def mash(d, _):
        d.rectangle((3,7,12,13), fill="#7c4f2a"); d.rectangle((4,6,11,11), fill="#d8bd74"); stipple(d,(4,6,11,11),["#f0d88d","#ad8c4f"],.35)
    save_pixel("item/potato_mash.png", mash)

    def vodka(d, _):
        d.rectangle((6,1,9,3), fill="#c7e9ee"); d.rectangle((5,3,10,14), fill="#9fd3df"); d.rectangle((6,4,9,12), fill="#e5f7f7")
        d.rectangle((5,7,10,10), fill="#f3f0df"); d.rectangle((6,8,9,8), fill="#c63332"); d.point((9,4), fill="#ffffff")
    save_pixel("item/vodka_bottle.png", vodka)

    def tea(d, _):
        d.rectangle((4,5,11,13), fill="#d5edf1"); d.rectangle((5,7,10,12), fill="#a65a24"); d.arc((9,7,14,12),-90,90,fill="#d5edf1",width=2); d.line((6,3,7,1), fill="#c7e8ed"); d.line((9,4,10,2), fill="#c7e8ed")
    save_pixel("item/hot_tea.png", tea)

    def borscht(d, _):
        d.ellipse((2,6,13,14), fill="#80522d"); d.ellipse((3,5,12,11), fill="#9f1832"); d.rectangle((4,7,11,10), fill="#b8253e"); d.point((6,7),fill="#e8d58b"); d.point((9,8),fill="#d7b378")
    save_pixel("item/borscht.png", borscht)


def block_textures():
    def frozen(d, _):
        d.rectangle((0,0,15,15), fill="#282b30")
        stipple(d,(0,0,15,15),["#353941","#171a1e","#424750"],.48)
        d.line([(0,4),(4,5),(6,9),(10,10),(15,14)],fill="#71c8df",width=1); d.line([(9,0),(8,4),(11,7),(15,7)],fill="#b8edf4")
    save_pixel("block/frozen_blackstone.png", frozen)

    def permafrost(d, _):
        d.rectangle((0,0,15,15),fill="#36576b"); stipple(d,(0,0,15,15),["#47798d","#233f52","#7db6c4"],.5)
        d.line([(1,2),(5,5),(4,9),(9,12),(14,11)],fill="#a8e2e8")
    save_pixel("block/nether_permafrost.png", permafrost)

    def copper(d, _, top=False, bottom=False):
        d.rectangle((0,0,15,15),fill="#a95831"); stipple(d,(0,0,15,15),["#c97845","#713b2b","#dda15f"],.35)
        if top: d.ellipse((3,3,12,12),outline="#f0b873",width=2); d.rectangle((7,0,8,5),fill="#6c3d29")
        if bottom: d.rectangle((3,3,12,12),fill="#74432e")
    save_pixel("block/samovar_side.png", lambda d,i: copper(d,i))
    save_pixel("block/samovar_top.png", lambda d,i: copper(d,i,top=True))
    save_pixel("block/samovar_bottom.png", lambda d,i: copper(d,i,bottom=True))


def armor_and_hud():
    def layer(name, color, trim):
        image = Image.new("RGBA", (64,32), (0,0,0,0)); d=ImageDraw.Draw(image)
        d.rectangle((0,0,31,15),fill=color); d.rectangle((16,16,39,31),fill=color)
        for x in range(0,40,4): d.point((x%32,(x*3)%31),fill=trim)
        path=TEX/f"models/armor/{name}"; path.parent.mkdir(parents=True,exist_ok=True); image.save(path)
    layer("ushanka_layer_1.png", "#6b4028", "#c09261")
    layer("fur_coat_layer_1.png", "#75482c", "#c89c69")
    save_pixel("gui/snowflake.png", lambda d,i: (d.line((8,1,8,14),fill="#dff8ff"),d.line((1,8,14,8),fill="#dff8ff"),d.line((3,3,13,13),fill="#94ddeb"),d.line((13,3,3,13),fill="#94ddeb")))


def synth(path: Path, duration: float, maker, rate=22050):
    path.parent.mkdir(parents=True, exist_ok=True)
    wav = path.with_suffix(".wav")
    samples=[]
    for n in range(int(rate*duration)):
        t=n/rate
        value=max(-1.0,min(1.0,maker(t,n,rate)))
        samples.append(struct.pack('<h',int(value*24000)))
    with wave.open(str(wav),'wb') as f:
        f.setnchannels(1);f.setsampwidth(2);f.setframerate(rate);f.writeframes(b''.join(samples))
    subprocess.run(["ffmpeg","-y","-loglevel","error","-i",str(wav),"-c:a","libvorbis","-q:a","4",str(path)],check=True)
    wav.unlink()


def audio_assets():
    noise=random.Random(44)
    wind=lambda t,n,r: (noise.random()*2-1)*.16*(.5+.5*math.sin(t*1.7)) + math.sin(2*math.pi*72*t)*.025
    synth(SOUNDS/"ambient/wind_1.ogg",3.8,wind); synth(SOUNDS/"ambient/wind_2.ogg",4.2,lambda t,n,r: wind(t*1.12,n,r)*.85)
    synth(SOUNDS/"player/teeth_1.ogg",.65,lambda t,n,r: (.42*math.sin(2*math.pi*180*t) if int(t*24)%2==0 else 0)*math.exp(-t*2))
    synth(SOUNDS/"player/teeth_2.ogg",.7,lambda t,n,r: (.35*math.sin(2*math.pi*215*t) if int(t*29)%2==0 else 0)*math.exp(-t*2))
    synth(SOUNDS/"player/cold_breath.ogg",1.0,lambda t,n,r:(noise.random()*2-1)*.22*math.sin(math.pi*t))
    synth(SOUNDS/"entity/bear_warning.ogg",1.2,lambda t,n,r:(math.sin(2*math.pi*(65+12*t)*t)+.3*math.sin(2*math.pi*34*t))*.25*math.sin(math.pi*t))
    synth(SOUNDS/"entity/bear_step.ogg",.45,lambda t,n,r:(noise.random()*2-1)*.6*math.exp(-t*12)+math.sin(2*math.pi*55*t)*.3*math.exp(-t*9))
    synth(SOUNDS/"item/vodka_open.ogg",.35,lambda t,n,r:math.sin(2*math.pi*(680+900*t)*t)*.35*math.exp(-t*13))
    synth(SOUNDS/"item/vodka_drink.ogg",.8,lambda t,n,r:(noise.random()*2-1)*.12+math.sin(2*math.pi*(180-60*t)*t)*.14)
    synth(SOUNDS/"effect/drunk_sting.ogg",1.2,lambda t,n,r:sum(math.sin(2*math.pi*f*t) for f in (98,123.5,147))*.11*math.exp(-t*1.6))
    synth(SOUNDS/"item/bottle_clink.ogg",.45,lambda t,n,r:(math.sin(2*math.pi*930*t)+.6*math.sin(2*math.pi*1420*t))*.25*math.exp(-t*10))
    synth(SOUNDS/"block/samovar_boil.ogg",2.2,lambda t,n,r:(noise.random()*2-1)*.10+(.12*math.sin(2*math.pi*300*t) if n%1900<70 else 0))
    synth(SOUNDS/"block/samovar_whistle.ogg",1.4,lambda t,n,r:math.sin(2*math.pi*(920+25*math.sin(t*8))*t)*.22*math.sin(math.pi*t/1.4))
    synth(SOUNDS/"ambient/inferno_1.ogg",4.0,lambda t,n,r:wind(t,n,r)*.8+math.sin(2*math.pi*42*t)*.06)
    synth(SOUNDS/"ambient/inferno_2.ogg",4.4,lambda t,n,r:wind(t*.8,n,r)*.7+math.sin(2*math.pi*31*t)*.08)


def nbt_string(value):
    raw=value.encode(); return struct.pack('>H',len(raw))+raw
def tag(kind,name,payload): return bytes([kind])+nbt_string(name)+payload
def int_tag(name,value): return tag(3,name,struct.pack('>i',value))
def string_tag(name,value): return tag(8,name,nbt_string(value))
def compound_tag(name,parts): return tag(10,name,b''.join(parts)+b'\0')
def int_list(name,values): return tag(9,name,b'\x03'+struct.pack('>i',len(values))+b''.join(struct.pack('>i',v) for v in values))
def compound_list(name,items): return tag(9,name,b'\x0a'+struct.pack('>i',len(items))+b''.join(b''.join(x)+b'\0' for x in items))


def banya_template():
    palette=[]; index={}
    def state(name,props=None):
        key=(name,tuple(sorted((props or {}).items())))
        if key not in index:
            parts=[string_tag('Name',name)]
            if props: parts.append(compound_tag('Properties',[string_tag(k,v) for k,v in sorted(props.items())]))
            index[key]=len(palette);palette.append(parts)
        return index[key]
    blocks=[]
    def put(x,y,z,name,props=None,nbt=None):
        parts=[int_list('pos',[x,y,z]),int_tag('state',state(name,props))]
        if nbt: parts.append(compound_tag('nbt',nbt))
        blocks.append(parts)
    for x in range(7):
        for z in range(7): put(x,0,z,'minecraft:dark_oak_planks')
    for y in range(1,4):
        for x in range(7):
            for z in (0,6):
                if not (z==0 and x==3 and y<3): put(x,y,z,'minecraft:stripped_spruce_log',{'axis':'y'})
        for z in range(1,6):
            for x in (0,6): put(x,y,z,'minecraft:stripped_spruce_log',{'axis':'y'})
    for x in range(7):
        for z in range(7): put(x,4,z,'minecraft:dark_oak_planks')
    put(3,1,4,'minecraft:campfire',{'facing':'north','lit':'true','signal_fire':'false','waterlogged':'false'})
    put(2,1,4,'minecraft:cauldron')
    put(4,1,4,'minecraft:barrel',{'facing':'north','open':'false'},[string_tag('LootTable','russian_survival:chests/abandoned_banya')])
    put(1,1,1,'russian_survival:samovar',{'lit':'false','water':'true'})
    root=[int_tag('DataVersion',3955),int_list('size',[7,5,7]),compound_list('palette',palette),compound_list('blocks',blocks),compound_list('entities',[])]
    target=ROOT/'src/main/resources/data/russian_survival/structure/abandoned_banya.nbt'; target.parent.mkdir(parents=True,exist_ok=True)
    with gzip.open(target,'wb') as f: f.write(compound_tag('',root))


def icon():
    source=ROOT/'tmp/imagegen/icon_alpha.png'
    target=ASSETS/'icon.png'; target.parent.mkdir(parents=True,exist_ok=True)
    image=Image.open(source).convert('RGBA')
    bbox=image.getbbox(); image=image.crop(bbox)
    side=max(image.size); canvas=Image.new('RGBA',(side,side),(0,0,0,0)); canvas.alpha_composite(image,((side-image.width)//2,(side-image.height)//2))
    canvas.resize((128,128),Image.Resampling.NEAREST).save(target)


if __name__ == '__main__':
    item_textures(); block_textures(); armor_and_hud(); audio_assets(); banya_template(); icon()
