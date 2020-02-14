package intquant.trit.blocks.tiles;

import intquant.trit.blocks.ModBlocks;
import net.minecraft.tileentity.ITickableTileEntity;

public class TileSolarPanel extends TileFlowLinker implements ITickableTileEntity{
	private boolean isValid = false;
	private int updates = 0;
	
	public TileSolarPanel() {
		super(ModBlocks.TILE_SOLAR_PANEL);
		this.setMaxLightStorage(1000);
		this.setMaxForceStorage(100);
		this.setMaxSpatialStorage(100);
		this.setDoProvide(true);
		this.setDoAccept(false);
	}
	public void tick() {
		super.tick();
		manageLight(1);
		/*updates++;
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
		}*/
	}
}
