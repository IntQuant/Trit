package intquant.trit.blocks.tiles;

import net.minecraft.util.ITickable;

public class TileSolarPanel extends TileFlowLinker implements ITickable{
	private boolean isValid = false;
	private int updates = 0;
	
	public TileSolarPanel() {
		super();
		this.setMaxLightStorage(1000);
		this.setMaxForceStorage(100);
		this.setMaxSpatialStorage(100);
		this.setDoProvide(true);
		this.setDoAccept(false);
	}
	public void update() {
		super.update();
		updates++;
		if (updates>50) {
			updates = 0;
			isValid = world.canBlockSeeSky(pos);
		}
		if (isValid && world.isDaytime() && !world.isRaining()) {
			this.manageLight(Math.min(this.getMaxLightStorage() - this.lightStorage, 1));
			if (updates % 10 == 0) {
				this.manageForce(Math.min(this.getMaxForceStorage() - this.forceStorage, 1));
				this.manageSpatial(Math.min(this.getMaxSpatialStorage() - this.spatialStorage, 1));
			}
		}
	}
}
