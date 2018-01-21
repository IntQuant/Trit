
pattern = """\
{
  "parent": "item/generated",
  "textures": {
    "layer0": "trit:items/%s"
  }
} 
"""

print("Input item name")
item_name = input("->")

result = pattern % item_name

with open("%s.json" % item_name, "w") as f:
	f.write(result)

