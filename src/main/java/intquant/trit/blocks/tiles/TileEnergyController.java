package intquant.trit.blocks.tiles;

import intquant.trit.Trit;
import intquant.trit.energy.IEnergyController;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEnergyController extends TileEntity implements IEnergyController {
	
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
	
	public TileEnergyController() {
		super();
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
	public boolean isValid() {
		try {
			return (!super.isInvalid()) && 
					world != null && 
					linkerPos != null && 
					world.getBlockState(linkerPos).getBlock().equals(CommonProxy.BFL);
		} catch (NullPointerException e) {
			CommonProxy.logger.error("Null pointer exception while checking validicy");
			return false; //No-one cares, actually
		}
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
        if (compound.hasKey("linker_pos", 99)) {
        	linkerPos = BlockPos.fromLong(compound.getLong("linker_pos"));
        }
        
        if (compound.hasKey("stor_light", 99)) lightStorage            = compound.getLong("stor_light");
        if (compound.hasKey("stor_force", 99)) forceStorage            = compound.getLong("stor_force");
        if (compound.hasKey("stor_spatial", 99)) spatialStorage        = compound.getLong("stor_spatial");
        
        if (compound.hasKey("max_stor_light", 99)) maxLightStorage     = compound.getLong("max_stor_light");
        if (compound.hasKey("max_stor_force", 99)) maxForceStorage     = compound.getLong("max_stor_force");
        if (compound.hasKey("max_stor_spatial", 99)) maxSpatialStorage = compound.getLong("max_stor_spatial");
        
        if (compound.hasKey("do_provide")) doProvide                   = compound.getBoolean("do_provide");
        if (compound.hasKey("do_accept")) doAccept                     = compound.getBoolean("do_accept");
	}
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        if (linkerPos != null) {
        	compound.setLong("linker_pos", linkerPos.toLong());
        }
        
        compound.setLong("stor_light", lightStorage);
        compound.setLong("stor_force", forceStorage);
        compound.setLong("stor_spatial", spatialStorage);
        
        compound.setLong("max_stor_light", maxLightStorage);
        compound.setLong("max_stor_force", maxForceStorage);
        compound.setLong("max_stor_spatial", maxSpatialStorage);
        
        compound.setBoolean("do_provide", doProvide);
        compound.setBoolean("do_accept", doAccept);
        
        return compound;
	}
}
