package intquant.trit.blocks.tiles;

import intquant.trit.Trit;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

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
	
	public boolean setControllerPos(BlockPos pos) {
		//TODO add dimension handling
		TileEntity tile = world.getTileEntity(pos);
		
		if (tile != null & tile instanceof TileFlowNetworkController) {
			try {
				controller = (TileFlowNetworkController)tile;
				return true;
			} catch (ClassCastException e) {
				return false;
			}
		}
		return false;
	}
	
	public void update() {
		
		lastCheck = this.world.getTotalWorldTime();
	}
	
	

}
