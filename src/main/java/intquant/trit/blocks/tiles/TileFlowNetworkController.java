package intquant.trit.blocks.tiles;

public class TileFlowNetworkController extends TileFlowLinker {
	public TileFlowNetworkController() {
		super();
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