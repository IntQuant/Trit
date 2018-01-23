package intquant.trit.blocks.tiles;

import net.minecraft.util.ITickable;

public class TileSolarPanel extends TileEnergyController implements ITickable{
	public TileSolarPanel() {
		super();
		this.setMaxLightStorage(1000);
		this.setDoProvide(true);
	}
	public void update() {
		this.manageLight(Math.min(this.getMaxLightStorage() - this.lightStorage, 1));
	}
}
