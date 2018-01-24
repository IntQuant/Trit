pattern = """\
this.setMax[0]Storage(1000);
"""



for word in ["Light", "Force", "Spatial"]:
	print(pattern.replace("{", "=>").replace("}", "<=").replace("[", "{").replace("]", "}").format(word).replace("=>", "{").replace("<=", "}"))
