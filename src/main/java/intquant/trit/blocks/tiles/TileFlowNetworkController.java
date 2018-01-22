package intquant.trit.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileFlowNetworkController extends TileEntity implements ITickable{
	
	private long lastUpdateTick = 0;
	
	public TileFlowNetworkController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		long tickDelta = world.getTotalWorldTime() - lastUpdateTick;
		
		
		
		lastUpdateTick = world.getTotalWorldTime();		
	}

}
