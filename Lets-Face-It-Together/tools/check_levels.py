import json
from pathlib import Path

root = Path("assets/levels")

ok = True

for path in sorted(root.rglob("*.json")):
    try:
        with path.open() as f:
            data = json.load(f)

        required = ["tutorialText", "levelWidth", "background", "playerStart", "key", "door", "objects"]
        missing = [field for field in required if field not in data]

        if missing:
            ok = False
            print(f"Missing fields in {path}: {missing}")
        else:
            print(f"OK: {path}")

    except Exception as e:
        ok = False
        print(f"ERROR in {path}: {e}")

if not ok:
    raise SystemExit(1)

print("All level JSON files look valid.")
