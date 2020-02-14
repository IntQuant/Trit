package intquant.trit.blocks.tiles;

import intquant.trit.blocks.ModBlocks;

public class TileFlowNetworkController extends TileFlowLinker {
	public TileFlowNetworkController() {
		super(ModBlocks.TILE_FLOWNETWORKCONTROLLER);
		setMaxLightStorage(10000);
		setMaxForceStorage(10000);
		setMaxSpatialStorage(10000);
		setDoAccept(true);
		setDoProvide(false);
	}

	@Override
	public boolean isValid() {
		return true;
	}
}