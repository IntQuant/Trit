package intquant.trit.blocks.tiles;

public class TileForceTransiever extends TileEnergyController {

	public TileForceTransiever() {
		// TODO Auto-generated constructor stub
		super();
		
		this.setMaxForceStorage(1000000);
		this.setDoProvide(true);
	}
	
	public void onCollision(long speed) {
		this.manageForce(Math.min(this.getForcedAcceptableForce(), speed));
	}

}
