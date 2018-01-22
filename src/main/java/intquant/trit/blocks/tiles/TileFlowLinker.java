package intquant.trit.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileFlowLinker extends TileEntity{
	
	private TileFlowNetworkController controller = null;
	private long lastCheck = 0;
	
	public TileFlowLinker() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setController(TileFlowNetworkController controller) {
		this.controller = controller;
	}
	public long getLastCheck() {
		return lastCheck;
	}
	public void setLastCheck(long lastCheck) {
		this.lastCheck = lastCheck;
	}
	
	public void update() {
		
		lastCheck = this.world.getTotalWorldTime();
	}
	
	

}
