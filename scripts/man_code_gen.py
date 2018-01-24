pattern = """\
	@Nonnull
	public IEnergyController getNext[0]() {
		if ([0] != null && [0].hasNext()) {
			CommonProxy.logger.info("Returning next iterator value");
			IEnergyController tmp = [0].next();
			return tmp;

		} else {
			CommonProxy.logger.info("Creating new iterator");
			[0] = controlled.iterator();
			return (IEnergyController) this;
		}
	}
"""



for word0 in ["Light", "Force", "Spatial"]:
	for word1 in ["In", "Out"]:
		word = word0+word1
		print(pattern.replace("{", "=>").replace("}", "<=").replace("[", "{").replace("]", "}").format(word).replace("=>", "{").replace("<=", "}"))
