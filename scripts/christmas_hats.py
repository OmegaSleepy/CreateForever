import json

colors = [
    "white", "orange", "magenta", "light_blue",
    "yellow", "lime", "pink", "gray",
    "light_gray", "cyan", "purple", "blue",
    "brown", "green", "red", "black"
]

for color in colors:
    filename = f"christmas_{color}_hat.json"
    data = {
        "parent": "createforever:item/christmas_0_base_hat",
        "textures": {
            "1": f"createforever:item/hat/christmas/{color}_hat"
        }
    }

    with open(filename, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=4)

print("Successfully generated all 16 hat model JSON files!")