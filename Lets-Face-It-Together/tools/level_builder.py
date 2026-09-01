import json
import tkinter as tk
from tkinter import filedialog, messagebox
from pathlib import Path

SCREEN_W = 960
SCREEN_H = 540
FLOOR_Y = 70
FLOOR_H = 20

CANVAS_H = 540
GRID = 10

DEFAULT_OBJECTS = {
    "solidWall": {"w": 100, "h": 95},
    "breakableWall": {"w": 100, "h": 95},
    "heavyButton": {"w": 100, "h": 35},
    "laserEmitter": {"w": 50, "h": 80, "length": 560},
    "chasingWall": {"w": 80, "h": 540},
    "surpriseWallSet": {"w": 100, "h": 95},
    "ghost": {"w": 0, "h": 0},
    "cage": {"w": 145, "h": 125},
}

OBJECT_COLORS = {
    "solidWall": "#7a0000",
    "breakableWall": "#b03535",
    "heavyButton": "#31c931",
    "laserEmitter": "#555555",
    "chasingWall": "#8844aa",
    "surpriseWallSet": "#e6d84a",
    "ghost": "#ddddff",
    "cage": "#555555",
    "playerStart": "#245cff",
    "key": "#ffe600",
    "door": "#9a4f28",
}

OBJECT_ORDER = [
    "solidWall",
    "breakableWall",
    "heavyButton",
    "laserEmitter",
    "chasingWall",
    "surpriseWallSet",
    "ghost",
    "cage",
]


class LevelBuilder:
    def __init__(self, root):
        self.root = root
        self.root.title("Let's Face It Together - Level Builder")

        self.file_path = None
        self.selected_tool = "solidWall"
        self.selected_item_id = None
        self.drag_offset_x = 0
        self.drag_offset_y = 0

        self.level_data = self.default_level()

        self.canvas_items = {}
        self.object_lookup = {}

        self.build_ui()
        self.redraw()

    def default_level(self):
        return {
            "tutorialText": "Write tutorial text here.",
            "levelWidth": 1800,
            "background": {"r": 0.30, "g": 0.30, "b": 0.30},
            "playerStart": {"x": 100, "y": 90},
            "key": {"x": 1300, "y": 130},
            "door": {"x": 1650, "y": 90},
            "objects": []
        }

    def build_ui(self):
        top = tk.Frame(self.root)
        top.pack(side=tk.TOP, fill=tk.X)

        tk.Button(top, text="New", command=self.new_level).pack(side=tk.LEFT)
        tk.Button(top, text="Open", command=self.open_level).pack(side=tk.LEFT)
        tk.Button(top, text="Save", command=self.save_level).pack(side=tk.LEFT)
        tk.Button(top, text="Save As", command=self.save_level_as).pack(side=tk.LEFT)
        tk.Button(top, text="+960 Width", command=self.expand_level).pack(side=tk.LEFT, padx=10)
        tk.Button(top, text="Preview", command=self.preview_level).pack(side=tk.LEFT)

        self.info_label = tk.Label(top, text="")
        self.info_label.pack(side=tk.LEFT, padx=15)

        main = tk.Frame(self.root)
        main.pack(side=tk.TOP, fill=tk.BOTH, expand=True)

        palette = tk.Frame(main, width=180)
        palette.pack(side=tk.LEFT, fill=tk.Y)

        tk.Label(palette, text="Objects", font=("Arial", 16, "bold")).pack(pady=8)

        for obj_type in OBJECT_ORDER:
            btn = tk.Button(
                palette,
                text=obj_type,
                width=18,
                command=lambda t=obj_type: self.set_tool(t)
            )
            btn.pack(pady=2)

        tk.Label(palette, text="Special", font=("Arial", 14, "bold")).pack(pady=(18, 6))

        for special in ["playerStart", "key", "door"]:
            btn = tk.Button(
                palette,
                text=special,
                width=18,
                command=lambda t=special: self.set_tool(t)
            )
            btn.pack(pady=2)

        tk.Label(
            palette,
            text="Click to add.\nDrag to move.\nDelete key removes selected.",
            justify=tk.LEFT
        ).pack(pady=20)

        canvas_frame = tk.Frame(main)
        canvas_frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

        self.canvas = tk.Canvas(canvas_frame, bg="#333333", height=CANVAS_H)
        self.canvas.pack(side=tk.TOP, fill=tk.BOTH, expand=True)

        self.scrollbar = tk.Scrollbar(canvas_frame, orient=tk.HORIZONTAL, command=self.canvas.xview)
        self.scrollbar.pack(side=tk.BOTTOM, fill=tk.X)

        self.canvas.configure(xscrollcommand=self.scrollbar.set)

        self.canvas.bind("<Button-1>", self.on_canvas_click)
        self.canvas.bind("<B1-Motion>", self.on_canvas_drag)
        self.canvas.bind("<ButtonRelease-1>", self.on_canvas_release)

        self.root.bind("<Delete>", self.delete_selected)
        self.root.bind("<BackSpace>", self.delete_selected)

    def set_tool(self, tool):
        self.selected_tool = tool
        self.selected_item_id = None
        self.update_info()

    def update_info(self):
        self.info_label.config(
            text=f"Tool: {self.selected_tool} | Width: {self.level_data.get('levelWidth', 960)}"
        )

    def game_to_canvas_y(self, y, h):
        return SCREEN_H - y - h

    def canvas_to_game_y(self, cy, h):
        return SCREEN_H - cy - h

    def snap(self, value):
        return round(value / GRID) * GRID

    def object_size(self, obj):
        obj_type = obj.get("type", "")
        if obj_type == "ghost":
            return 95, 95
        if obj_type == "door":
            return 90, 130
        if obj_type == "key":
            return 84, 84
        if obj_type == "playerStart":
            return 50, 85
        if obj_type == "surpriseWallSet":
            return obj.get("w", 100), obj.get("h", 95) * 3
        return obj.get("w", 50), obj.get("h", 50)

    def redraw(self):
        self.canvas.delete("all")
        self.canvas_items.clear()
        self.object_lookup.clear()

        level_width = self.level_data.get("levelWidth", 960)
        self.canvas.configure(scrollregion=(0, 0, level_width, SCREEN_H))

        bg = self.level_data.get("background", {"r": 0.3, "g": 0.3, "b": 0.3})
        bg_color = self.rgb_to_hex(bg.get("r", 0.3), bg.get("g", 0.3), bg.get("b", 0.3))
        self.canvas.create_rectangle(0, 0, level_width, SCREEN_H, fill=bg_color, outline="")

        for x in range(0, level_width + 1, SCREEN_W):
            self.canvas.create_line(x, 0, x, SCREEN_H, fill="white", width=2)
            self.canvas.create_text(x + 35, 20, text=f"x={x}", fill="white", font=("Arial", 16, "bold"))

        for x in range(0, level_width + 1, 100):
            self.canvas.create_line(x, 0, x, SCREEN_H, fill="#ffffff", stipple="gray25")

        floor_y = self.game_to_canvas_y(FLOOR_Y, FLOOR_H)
        self.canvas.create_rectangle(0, floor_y, level_width, floor_y + FLOOR_H, fill="#444444", outline="")

        self.draw_special("playerStart", self.level_data["playerStart"])
        self.draw_special("key", self.level_data["key"])
        self.draw_special("door", self.level_data["door"])

        for index, obj in enumerate(self.level_data.get("objects", [])):
            self.draw_object(obj, index)

        plus_x = level_width - 80
        self.canvas.create_rectangle(plus_x, 20, plus_x + 60, 80, fill="#222222", outline="white", width=2, tags=("expand_button",))
        self.canvas.create_text(plus_x + 30, 50, text="+", fill="white", font=("Arial", 34, "bold"), tags=("expand_button",))

        self.update_info()

    def rgb_to_hex(self, r, g, b):
        return f"#{int(r * 255):02x}{int(g * 255):02x}{int(b * 255):02x}"

    def draw_special(self, obj_type, data):
        obj = {"type": obj_type, "x": data["x"], "y": data["y"]}
        if obj_type == "playerStart":
            obj["w"], obj["h"] = 50, 85
        elif obj_type == "key":
            obj["w"], obj["h"] = 84, 84
        elif obj_type == "door":
            obj["w"], obj["h"] = 90, 130

        item_id = self.draw_rect_object(obj, f"special:{obj_type}")
        self.object_lookup[item_id] = ("special", obj_type)

    def draw_object(self, obj, index):
        obj_type = obj.get("type", "unknown")

        if obj_type == "laserEmitter":
            length = obj.get("length", 560)
            laser_h = 40
            lx = obj["x"] - length
            ly = obj["y"] + obj.get("h", 80) / 2 - laser_h / 2
            cy = self.game_to_canvas_y(ly, laser_h)
            self.canvas.create_rectangle(lx, cy, obj["x"], cy + laser_h, fill="#00ddff", outline="")

        item_id = self.draw_rect_object(obj, f"object:{index}")
        self.object_lookup[item_id] = ("object", index)

    def draw_rect_object(self, obj, tag):
        obj_type = obj.get("type", "unknown")
        x = obj.get("x", 0)
        y = obj.get("y", 0)
        w, h = self.object_size(obj)

        cy = self.game_to_canvas_y(y, h)
        color = OBJECT_COLORS.get(obj_type, "#ffffff")

        if obj_type == "ghost":
            item_id = self.canvas.create_oval(x, cy, x + w, cy + h, fill=color, outline="white", width=2, tags=(tag,))
        else:
            item_id = self.canvas.create_rectangle(x, cy, x + w, cy + h, fill=color, outline="white", width=2, tags=(tag,))

        self.canvas.create_text(
            x + 5, cy + 15,
            text=obj_type,
            fill="white",
            anchor="w",
            font=("Arial", 12, "bold"),
            tags=(tag,)
        )

        self.canvas_items[item_id] = tag
        return item_id

    def get_clicked_object_id(self, event):
        clicked = self.canvas.find_overlapping(event.x + self.canvas.canvasx(0), event.y, event.x + self.canvas.canvasx(0), event.y)
        for item in reversed(clicked):
            tags = self.canvas.gettags(item)
            for tag in tags:
                if tag.startswith("object:") or tag.startswith("special:"):
                    tagged_items = self.canvas.find_withtag(tag)
                    for candidate in tagged_items:
                        if candidate in self.object_lookup:
                            return candidate
        return None

    def on_canvas_click(self, event):
        world_x = self.canvas.canvasx(event.x)
        world_y = event.y

        if self.canvas.find_withtag("current"):
            tags = self.canvas.gettags("current")
            if "expand_button" in tags:
                self.expand_level()
                return

        clicked_id = self.get_clicked_object_id(event)

        if clicked_id is not None:
            self.selected_item_id = clicked_id
            kind, ref = self.object_lookup[clicked_id]
            obj = self.get_object_by_ref(kind, ref)
            w, h = self.object_size(obj)
            self.drag_offset_x = world_x - obj["x"]
            self.drag_offset_y = world_y - self.game_to_canvas_y(obj["y"], h)
            self.highlight_selected()
            return

        self.add_object_at(world_x, world_y)

    def on_canvas_drag(self, event):
        if self.selected_item_id is None:
            return

        world_x = self.canvas.canvasx(event.x)
        world_y = event.y

        kind, ref = self.object_lookup[self.selected_item_id]
        obj = self.get_object_by_ref(kind, ref)

        w, h = self.object_size(obj)

        new_x = self.snap(world_x - self.drag_offset_x)
        new_canvas_y = self.snap(world_y - self.drag_offset_y)
        new_y = self.canvas_to_game_y(new_canvas_y, h)

        level_width = self.level_data.get("levelWidth", 960)
        new_x = max(0, min(level_width - w, new_x))
        new_y = max(0, min(SCREEN_H - h, new_y))

        obj["x"] = new_x
        obj["y"] = new_y

        self.redraw()
        self.reselect(kind, ref)

    def on_canvas_release(self, event):
        pass

    def highlight_selected(self):
        self.redraw()
        if self.selected_item_id is None:
            return

    def reselect(self, kind, ref):
        for item_id, stored_ref in self.object_lookup.items():
            if stored_ref == (kind, ref):
                self.selected_item_id = item_id
                self.canvas.itemconfig(item_id, outline="yellow", width=4)
                return

    def get_object_by_ref(self, kind, ref):
        if kind == "special":
            return self.level_data[ref]
        return self.level_data["objects"][ref]

    def add_object_at(self, world_x, world_y):
        tool = self.selected_tool

        if tool in ["playerStart", "key", "door"]:
            if tool == "playerStart":
                w, h = 50, 85
            elif tool == "key":
                w, h = 84, 84
            else:
                w, h = 90, 130

            self.level_data[tool]["x"] = self.snap(world_x)
            self.level_data[tool]["y"] = self.snap(self.canvas_to_game_y(world_y, h))
            self.redraw()
            return

        defaults = DEFAULT_OBJECTS.get(tool, {"w": 50, "h": 50})
        w = defaults.get("w", 50)
        h = defaults.get("h", 50)

        obj = {
            "type": tool,
            "x": self.snap(world_x),
            "y": self.snap(self.canvas_to_game_y(world_y, h)),
            "w": w,
            "h": h
        }

        if "length" in defaults:
            obj["length"] = defaults["length"]

        self.level_data["objects"].append(obj)
        self.redraw()

    def delete_selected(self, event=None):
        if self.selected_item_id is None:
            return

        kind, ref = self.object_lookup[self.selected_item_id]

        if kind == "object":
            if 0 <= ref < len(self.level_data["objects"]):
                self.level_data["objects"].pop(ref)

        self.selected_item_id = None
        self.redraw()

    def expand_level(self):
        self.level_data["levelWidth"] = self.level_data.get("levelWidth", 960) + 960
        self.redraw()

    def new_level(self):
        if not messagebox.askyesno("New Level", "Start a new blank level? Unsaved changes will be lost."):
            return
        self.file_path = None
        self.level_data = self.default_level()
        self.redraw()

    def open_level(self):
        path = filedialog.askopenfilename(
            title="Open Level JSON",
            initialdir="assets/levels",
            filetypes=[("JSON files", "*.json")]
        )

        if not path:
            return

        with open(path, "r") as f:
            self.level_data = json.load(f)

        self.file_path = Path(path)
        self.redraw()

    def save_level(self):
        if self.file_path is None:
            self.save_level_as()
            return

        with open(self.file_path, "w") as f:
            json.dump(self.level_data, f, indent=2)

        messagebox.showinfo("Saved", f"Saved:\n{self.file_path}")

    def save_level_as(self):
        path = filedialog.asksaveasfilename(
            title="Save Level JSON",
            initialdir="assets/levels",
            defaultextension=".json",
            filetypes=[("JSON files", "*.json")]
        )

        if not path:
            return

        self.file_path = Path(path)
        self.save_level()

    def preview_level(self):
        self.save_level()
        if self.file_path is None:
            return

        import subprocess
        subprocess.run(["python3", "tools/preview_level.py", str(self.file_path)])
        preview_path = Path("level_previews") / f"{self.file_path.stem}_preview.png"
        if preview_path.exists():
            subprocess.run(["open", str(preview_path)])


def main():
    root = tk.Tk()
    root.geometry("1200x700")
    app = LevelBuilder(root)
    root.mainloop()


if __name__ == "__main__":
    main()
