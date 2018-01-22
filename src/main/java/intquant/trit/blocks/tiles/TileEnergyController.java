package intquant.trit.blocks.tiles;

import intquant.trit.energy.IEnergyController;
import net.minecraft.tileentity.TileEntity;

public class TileEnergyController extends TileEntity implements IEnergyController {

	private long maxLightStorage = 0;
	private long maxForceStorage = 0;
	private long maxSpatialStorage = 0;
	
	private long lightStorage = 0;
	private long forceStorage = 0;
	private long spatialStorage = 0;
	
	private boolean doProvide = false;
	private boolean doAccept  = false;
	
	@Override
	public long getProvideableLight() {
		if (doProvide) {
			return Math.max(0, manageLight(0));
		}
		return 0;
	}
	@Override
	public long getProvideableForce() {
		if (doProvide) {
			return Math.max(0, manageForce(0));
		}
		return 0;
	}
	@Override
	public long getProvideableSpatial() {
		if (doProvide) {
			return Math.max(0, manageSpatial(0));
		}
		return 0;
	}
	
	@Override
	public long getAcceptableLight() {
		if (doAccept) return Math.max(0, getMaxLightStorage() - manageLight(0));
		return 0;
	}
	@Override
	public long getAcceptableForce() {
		if (doAccept) return Math.max(0, getMaxForceStorage() - manageForce(0));
		return 0;
	}
	@Override
	public long getAcceptableSpatial() {
		if (doAccept) return Math.max(0, getMaxSpatialStorage() - manageSpatial(0));
		return 0;
	}
	
	@Override
	public long manageLight(long value) {
		lightStorage += value;
		return lightStorage;
	}
	@Override
	public long manageForce(long value) {
		forceStorage += value;
		return forceStorage;
	}
	@Override
	public long manageSpatial(long value) {
		spatialStorage += value;
		return spatialStorage;
	}
	
	public TileEnergyController() {
		super();
	}
	@Override
	public long getMaxLightStorage() {
		return maxLightStorage;
	}
	public void setMaxLightStorage(long maxLightStorage) {
		this.maxLightStorage = maxLightStorage;
	}
	public long getMaxForceStorage() {
		return maxForceStorage;
	}
	public void setMaxForceStorage(long maxForceStorage) {
		this.maxForceStorage = maxForceStorage;
	}
	public long getMaxSpatialStorage() {
		return maxSpatialStorage;
	}
	public void setMaxSpatialStorage(long maxSpatialStorage) {
		this.maxSpatialStorage = maxSpatialStorage;
	}
	public boolean isDoProvide() {
		return doProvide;
	}
	public void setDoProvide(boolean doProvide) {
		this.doProvide = doProvide;
	}
	public boolean isDoAccept() {
		return doAccept;
	}
	public void setDoAccept(boolean doAccept) {
		this.doAccept = doAccept;
	}
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return this.isValid();
	}
	
	

}
