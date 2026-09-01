import json
import sys
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

SCREEN_W = 960
SCREEN_H = 540
FLOOR_Y_GAME = 70
FLOOR_H = 20
SCALE = 0.35

SPRITES = {
    "player": "assets/sprites/player/Normal/idle.png",
    "solidWall": "assets/sprites/objects/Walls/Wall.png",
    "breakableWall": "assets/sprites/objects/Walls/Cracked Wall.png",
    "key": "assets/sprites/objects/Key.png",
    "door": "assets/sprites/objects/Doors/Closed Door.png",
    "ghost": "assets/sprites/objects/Enemies/Ghost (Looking Left).png",
    "cage": "assets/sprites/objects/Doors/Jail Door (Closed).png",
    "heavyButton": "assets/sprites/objects/Buttons/Button (unpushed).png",
    "laserEmitter": "assets/sprites/objects/Lazer Turret (Facing Left).png",
    "surpriseWall": "assets/sprites/objects/Walls/Wall (Surprise).png"
}

FALLBACK_COLORS = {
    "solidWall": (120, 0, 0),
    "breakableWall": (170, 35, 35),
    "heavyButton": (40, 200, 40),
    "laserEmitter": (80, 80, 80),
    "chasingWall": (120, 60, 170),
    "surpriseWall": (190, 180, 80),
    "ghost": (210, 210, 255),
    "cage": (80, 80, 80),
}

LABEL_COLORS = {
    "player": (40, 90, 255),
    "key": (255, 230, 0),
    "door": (140, 70, 35),
    "floor": (60, 60, 60),
}


def game_to_image_y(game_y, game_h, image_h):
    return image_h - int((game_y + game_h) * SCALE)


def rect_from_game(x, y, w, h, image_h):
    return (
        int(x * SCALE),
        game_to_image_y(y, h, image_h),
        int((x + w) * SCALE),
        game_to_image_y(y, 0, image_h),
    )


def draw_label(draw, text, x, y):
    try:
        font = ImageFont.truetype("Arial.ttf", 12)
    except Exception:
        font = ImageFont.load_default()

    draw.text((x + 3, y + 3), text, fill=(255, 255, 255), font=font)


def paste_sprite(img, sprite_key, x, y, w, h, image_h):
    sprite_path = SPRITES.get(sprite_key)

    if sprite_path is None:
        return False

    path = Path(sprite_path)

    if not path.exists():
        return False

    sprite = Image.open(path).convert("RGBA")

    draw_w = max(1, int(w * SCALE))
    draw_h = max(1, int(h * SCALE))

    sprite = sprite.resize((draw_w, draw_h), Image.Resampling.NEAREST)

    px = int(x * SCALE)
    py = game_to_image_y(y, h, image_h)

    img.paste(sprite, (px, py), sprite)
    return True


def draw_fallback_rect(draw, obj_type, x, y, w, h, image_h):
    color = FALLBACK_COLORS.get(obj_type, (255, 255, 255))
    rect = rect_from_game(x, y, w, h, image_h)
    draw.rectangle(rect, fill=color)
    draw_label(draw, obj_type, rect[0], rect[1])


def draw_level(json_path):
    path = Path(json_path)

    with path.open() as f:
        data = json.load(f)

    level_w = int(data.get("levelWidth", 960))
    image_w = int(level_w * SCALE)
    image_h = int(SCREEN_H * SCALE)

    bg = data.get("background", {"r": 0.05, "g": 0.05, "b": 0.08})
    bg_color = (
        int(bg.get("r", 0.05) * 255),
        int(bg.get("g", 0.05) * 255),
        int(bg.get("b", 0.08) * 255),
    )

    img = Image.new("RGB", (image_w, image_h), bg_color)
    draw = ImageDraw.Draw(img)

    # Screen markers every 960 pixels
    for screen_x in range(0, level_w + 1, SCREEN_W):
        px = int(screen_x * SCALE)
        draw.line((px, 0, px, image_h), fill=(255, 255, 255), width=1)
        draw_label(draw, f"x={screen_x}", px + 2, 2)

    # Floor
    floor_rect = rect_from_game(0, FLOOR_Y_GAME, level_w, FLOOR_H, image_h)
    draw.rectangle(floor_rect, fill=LABEL_COLORS["floor"])

    # Objects
    for obj in data.get("objects", []):
        obj_type = obj.get("type", "unknown")
        x = obj.get("x", 0)
        y = obj.get("y", 0)
        w = obj.get("w", 50)
        h = obj.get("h", 50)

        if obj_type == "laserEmitter":
            length = obj.get("length", 0)

            # Draw laser beam shooting left from turret.
            laser_h = 40
            laser_rect = rect_from_game(x - length, y + h / 2 - laser_h / 2, length, laser_h, image_h)
            draw.rectangle(laser_rect, fill=(0, 220, 255))

            if not paste_sprite(img, "laserEmitter", x, y, 60, 60, image_h):
                draw_fallback_rect(draw, obj_type, x, y, 60, 60, image_h)

            draw_label(draw, "laser", laser_rect[0], laser_rect[1])

        elif obj_type == "chasingWall":
            draw_fallback_rect(draw, obj_type, x, y, w, h, image_h)

        elif obj_type == "surpriseWall":
            block_h = h

            # Bottom normal wall
            if not paste_sprite(img, "solidWall", x, y, w, block_h, image_h):
                draw_fallback_rect(draw, "solidWall", x, y, w, block_h, image_h)

            # Middle surprise wall
            if not paste_sprite(img, "surpriseWall", x, y + block_h, w, block_h, image_h):
                draw_fallback_rect(draw, "surpriseWall", x, y + block_h, w, block_h, image_h)

            # Top normal wall
            if not paste_sprite(img, "solidWall", x, y + block_h * 2, w, block_h, image_h):
                draw_fallback_rect(draw, "solidWall", x, y + block_h * 2, w, block_h, image_h)

            middle_rect = rect_from_game(x, y + block_h, w, block_h, image_h)
            draw_label(draw, "surpriseWall", middle_rect[0], middle_rect[1])

        elif obj_type == "ghost":
            if not paste_sprite(img, "ghost", x, y, 95, 95, image_h):
                draw_fallback_rect(draw, "ghost", x, y, 95, 95, image_h)
            rect = rect_from_game(x, y, 95, 95, image_h)
            draw_label(draw, "ghost", rect[0], rect[1])

        elif obj_type == "cage":
            if not paste_sprite(img, "cage", x, y, w, h, image_h):
                draw_fallback_rect(draw, "cage", x, y, w, h, image_h)
            rect = rect_from_game(x, y, w, h, image_h)
            draw_label(draw, "cage", rect[0], rect[1])

        elif obj_type == "solidWall":
            if not paste_sprite(img, "solidWall", x, y, w, h, image_h):
                draw_fallback_rect(draw, obj_type, x, y, w, h, image_h)
            rect = rect_from_game(x, y, w, h, image_h)
            draw_label(draw, "solidWall", rect[0], rect[1])

        elif obj_type == "breakableWall":
            if not paste_sprite(img, "breakableWall", x, y, w, h, image_h):
                draw_fallback_rect(draw, obj_type, x, y, w, h, image_h)
            rect = rect_from_game(x, y, w, h, image_h)
            draw_label(draw, "breakableWall", rect[0], rect[1])

        elif obj_type == "heavyButton":
            if not paste_sprite(img, "heavyButton", x, y, w, h, image_h):
                draw_fallback_rect(draw, obj_type, x, y, w, h, image_h)
            rect = rect_from_game(x, y, w, h, image_h)
            draw_label(draw, "button", rect[0], rect[1])

        else:
            draw_fallback_rect(draw, obj_type, x, y, w, h, image_h)

    # Player
    player = data.get("playerStart", {"x": 100, "y": 90})
    px = player.get("x", 100)
    py = player.get("y", 90)

    # Match the player sprite visual size roughly.
    if not paste_sprite(img, "player", px - 35, py - 20, 120, 120, image_h):
        player_rect = rect_from_game(px, py, 50, 85, image_h)
        draw.rectangle(player_rect, fill=LABEL_COLORS["player"])

    player_rect = rect_from_game(px, py, 50, 85, image_h)
    draw_label(draw, "player", player_rect[0], player_rect[1])

    # Key
    key = data.get("key", {"x": 720, "y": 130})
    kx = key.get("x", 720)
    ky = key.get("y", 130)

    if not paste_sprite(img, "key", kx, ky, 84, 84, image_h):
        key_rect = rect_from_game(kx, ky, 50, 50, image_h)
        draw.ellipse(key_rect, fill=LABEL_COLORS["key"])

    key_rect = rect_from_game(kx, ky, 50, 50, image_h)
    draw_label(draw, "key", key_rect[0], key_rect[1])

    # Door
    door = data.get("door", {"x": 830, "y": 90})
    dx = door.get("x", 830)
    dy = door.get("y", 90)

    if not paste_sprite(img, "door", dx, dy, 90, 130, image_h):
        door_rect = rect_from_game(dx, dy, 80, 130, image_h)
        draw.rectangle(door_rect, fill=LABEL_COLORS["door"])

    door_rect = rect_from_game(dx, dy, 80, 130, image_h)
    draw_label(draw, "door", door_rect[0], door_rect[1])

    out_dir = Path("level_previews")
    out_dir.mkdir(exist_ok=True)

    out_path = out_dir / f"{path.stem}_preview.png"
    img.save(out_path)

    print(f"Saved preview: {out_path}")


def main():
    if len(sys.argv) < 2:
        print("Usage:")
        print("  python3 tools/preview_level.py assets/levels/neutral/happy.json")
        raise SystemExit(1)

    for json_path in sys.argv[1:]:
        draw_level(json_path)


if __name__ == "__main__":
    main()
