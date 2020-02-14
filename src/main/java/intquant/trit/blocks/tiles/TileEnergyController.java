package intquant.trit.blocks.tiles;

import intquant.trit.Trit;
import intquant.trit.energy.IEnergyController;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class TileEnergyController extends TileEntity implements IEnergyController {

	public TileEnergyController(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	private long maxLightStorage = 0;
	private long maxForceStorage = 0;
	private long maxSpatialStorage = 0;
	
	protected long lightStorage = 0;
	protected long forceStorage = 0;
	protected long spatialStorage = 0;
	
	private boolean doProvide = false;
	private boolean doAccept  = false;
	
	private BlockPos linkerPos = null;
	
	private int id = 0;
	
	@Override
	public int getDebugId() {
		if (id==0) id = Trit.rand.nextInt();
		return id; 
	}
	
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
	
    public long getForcedProvideableLight() {
        return Math.max(0, manageLight(0));
	}
	
	public long getForcedProvideableForce() {
        return Math.max(0, manageForce(0));
	}
	
	public long getForcedProvideableSpatial() {
        return Math.max(0, manageSpatial(0));
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
	
    public long getForcedAcceptableLight() {
        return Math.max(0, getMaxLightStorage() - manageLight(0));
	}
	
	public long getForcedAcceptableForce() {
	    return Math.max(0, getMaxForceStorage() - manageForce(0));
	}
	
	public long getForcedAcceptableSpatial() {
	    return Math.max(0, getMaxSpatialStorage() - manageSpatial(0));
	}
	
	@Override
	public long manageLight(long value) {
		lightStorage += value;
		markDirty();
		return lightStorage;
	}
	@Override
	public long manageForce(long value) {
		forceStorage += value;
		markDirty();
		return forceStorage;
	}
	@Override
	public long manageSpatial(long value) {
		spatialStorage += value;
		markDirty();
		return spatialStorage;
	}
	
	@Override
	public long getMaxLightStorage() {
		return maxLightStorage;
	}
	public void setMaxLightStorage(long maxLightStorage) {
		this.maxLightStorage = maxLightStorage;
		markDirty();
	}
	public long getMaxForceStorage() {
		return maxForceStorage;
	}
	public void setMaxForceStorage(long maxForceStorage) {
		this.maxForceStorage = maxForceStorage;
		markDirty();
	}
	public long getMaxSpatialStorage() {
		return maxSpatialStorage;
	}
	public void setMaxSpatialStorage(long maxSpatialStorage) {
		this.maxSpatialStorage = maxSpatialStorage;
		markDirty();
	}
	public boolean isDoProvide() {
		return doProvide;
	}
	public void setDoProvide(boolean doProvide) {
		this.doProvide = doProvide;
		markDirty();
	}
	public boolean isDoAccept() {
		return doAccept;
	}
	public void setDoAccept(boolean doAccept) {
		this.doAccept = doAccept;
		markDirty();
	}
	
	@Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        
        if (compound.contains("linker_pos", 99)) {
        	linkerPos = BlockPos.fromLong(compound.getLong("linker_pos"));
        }
        
        if (compound.contains("stor_light", 99)) lightStorage            = compound.getLong("stor_light");
        if (compound.contains("stor_force", 99)) forceStorage            = compound.getLong("stor_force");
        if (compound.contains("stor_spatial", 99)) spatialStorage        = compound.getLong("stor_spatial");
        
        if (compound.contains("max_stor_light", 99)) maxLightStorage     = compound.getLong("max_stor_light");
        if (compound.contains("max_stor_force", 99)) maxForceStorage     = compound.getLong("max_stor_force");
        if (compound.contains("max_stor_spatial", 99)) maxSpatialStorage = compound.getLong("max_stor_spatial");
        
        if (compound.contains("do_provide")) doProvide                   = compound.getBoolean("do_provide");
        if (compound.contains("do_accept")) doAccept                     = compound.getBoolean("do_accept");
	}
	@Override
    public CompoundNBT write(CompoundNBT compound) {
        
        
        if (linkerPos != null) {
        	compound.putLong("linker_pos", linkerPos.toLong());
        }
        
        compound.putLong("stor_light", lightStorage);
        compound.putLong("stor_force", forceStorage);
        compound.putLong("stor_spatial", spatialStorage);
        
        compound.putLong("max_stor_light", maxLightStorage);
        compound.putLong("max_stor_force", maxForceStorage);
        compound.putLong("max_stor_spatial", maxSpatialStorage);
        
        compound.putBoolean("do_provide", doProvide);
        compound.putBoolean("do_accept", doAccept);
        
        return super.write(compound);
	}
}
