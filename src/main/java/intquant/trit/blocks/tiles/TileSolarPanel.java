package intquant.trit.blocks.tiles;

import net.minecraft.util.ITickable;

public class TileSolarPanel extends TileEnergyController implements ITickable{
	private boolean isValid = false;
	private int updates = 0;
	
	public TileSolarPanel() {
		super();
		this.setMaxLightStorage(1000);
		this.setDoProvide(true);
	}
	public void update() {
		updates++;
		if (updates>=50) {
			updates = 0;
			isValid = world.canBlockSeeSky(pos);
		}
		if (isValid && world.isDaytime() && !world.isRaining()) {
			this.manageLight(Math.min(this.getMaxLightStorage() - this.lightStorage, 1));
		}
	}
}
