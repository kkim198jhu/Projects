# Let's Face It Together — Level Builder Guide

Levels are stored as JSON files inside:

assets/levels/<world-name>/<level-name>.json

Example:

assets/levels/neutral/happy.json

Each level file controls:
- tutorial text
- level width
- background color
- player start
- key position
- door position
- objects in the level

The door is always the endpoint of the level. It does not move the player to another room.

---

## Basic Level Format

{
  "tutorialText": "Text shown at the start of the level.",
  "levelWidth": 1800,

  "background": {
    "r": 0.62,
    "g": 0.32,
    "b": 0.55
  },

  "playerStart": {
    "x": 100,
    "y": 90
  },

  "key": {
    "x": 1250,
    "y": 130
  },

  "door": {
    "x": 1650,
    "y": 90
  },

  "objects": [
    { "type": "solidWall", "x": 760, "y": 90, "w": 100, "h": 285 }
  ]
}

---

## Coordinate Notes

The floor top is around y = 90.

Good default positions:
- Player start: x = 100, y = 90
- Door: y = 90
- Floor objects: y = 90
- Key: y = 130

The visible screen is 960 pixels wide, but side-scrolling levels can be wider.

Recommended tutorial level widths:
- Short: 960
- Medium: 1800
- Long: 2400

---

## Object Types

### solidWall

A normal wall that blocks the player.

{ "type": "solidWall", "x": 760, "y": 90, "w": 100, "h": 285 }

---

### breakableWall

A cracked wall. Angry can break it. Other emotions collide with it.

{ "type": "breakableWall", "x": 430, "y": 90, "w": 100, "h": 95 }

For stacked blocks, add multiple objects:

{ "type": "breakableWall", "x": 430, "y": 90, "w": 100, "h": 95 }
{ "type": "breakableWall", "x": 430, "y": 185, "w": 100, "h": 95 }
{ "type": "breakableWall", "x": 430, "y": 280, "w": 100, "h": 95 }

---

### heavyButton

A button that Happy can press.

{ "type": "heavyButton", "x": 420, "y": 90, "w": 100, "h": 35 }

In the current Happy level, pressing any button removes all solid walls in that level.

---

### laserEmitter

A turret that shoots a lazer left. Sad can pass safely. Other emotions reset.

{ "type": "laserEmitter", "x": 760, "y": 90, "w": 50, "h": 80, "length": 560 }

The x/y position is the turret position.
The lazer shoots left from the turret.

---

### chasingWall

A wall that moves right and resets the player if it touches them.

{ "type": "chasingWall", "x": -90, "y": 0, "w": 80, "h": 540 }

Usually used in Fear levels.

---

### surpriseWallSet

A three-block wall stack. The middle block becomes passable when the player is Surprise.

{ "type": "surpriseWallSet", "x": 560, "y": 90, "w": 100, "h": 95 }

This creates:
- bottom normal wall
- middle surprise wall
- top normal wall

---

### ghost

A ghost for Disgust levels.

{ "type": "ghost", "x": 820, "y": 260, "w": 0, "h": 0 }

---

### cage

A cage/jail for Disgust levels.

{ "type": "cage", "x": 500, "y": 325, "w": 145, "h": 125 }

---

## Tutorial Text Style

Since the final game will use face detection, tutorial text should teach expressions:

Good:
- "Make a happy face to press heavy buttons."
- "Make an angry face to break cracked walls."
- "Make a sad face to safely pass through the lazer."

Avoid relying only on keyboard controls:
- "Press 1 to become Happy."
- "Press 2 to become Angry."

Keyboard controls are only temporary testing controls.

---

## Recommended Neutral World Level Structure

Neutral World is the tutorial world.

1. Happy — button tutorial
2. Angry — cracked wall tutorial
3. Sad — lazer tutorial
4. Fear — chase/speed tutorial
5. Surprise — surprise wall tutorial
6. Disgust — ghost/cage tutorial

After Happy, do not repeat the key/door explanation unless needed.
