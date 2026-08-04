"""Fast release check for JSON syntax and every locally referenced PNG/OGG."""
from __future__ import annotations
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "src/main/resources"
errors=[]
for path in RES.rglob("*.json"):
    try: json.loads(path.read_text(encoding="utf-8"))
    except Exception as exc: errors.append(f"invalid JSON {path.relative_to(ROOT)}: {exc}")

sounds=json.loads((RES/"assets/russian_survival/sounds.json").read_text(encoding="utf-8"))
for event,data in sounds.items():
    for entry in data.get("sounds",[]):
        name=entry if isinstance(entry,str) else entry["name"]
        ns,rel=(name.split(":",1) if ":" in name else ("minecraft",name))
        if ns=="russian_survival":
            path=RES/f"assets/{ns}/sounds/{rel}.ogg"
            if not path.exists() or path.read_bytes()[:4]!=b"OggS": errors.append(f"missing/invalid sound for {event}: {path}")

for model in (RES/"assets/russian_survival/models").rglob("*.json"):
    data=json.loads(model.read_text(encoding="utf-8"))
    for texture in data.get("textures",{}).values():
        if texture.startswith("#") or texture.startswith("minecraft:"): continue
        ns,rel=texture.split(":",1)
        path=RES/f"assets/{ns}/textures/{rel}.png"
        if not path.exists(): errors.append(f"missing texture referenced by {model.name}: {path}")

if errors:
    raise SystemExit("\n".join(errors))
print(f"Validated {sum(1 for _ in RES.rglob('*.json'))} JSON files, {len(sounds)} sounds, and all model textures.")
