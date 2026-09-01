import subprocess
from pathlib import Path

level_files = sorted(Path("assets/levels/neutral").glob("*.json"))

if not level_files:
    print("No level JSON files found.")
    raise SystemExit(1)

cmd = ["python3", "tools/preview_level.py"] + [str(path) for path in level_files]

print("Previewing:")
for path in level_files:
    print(" -", path)

subprocess.run(cmd, check=True)

print()
print("Done. Opening preview folder.")
subprocess.run(["open", "level_previews"])
