package intquant.trit.blocks.tiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import intquant.trit.energy.IEnergyController;
import intquant.trit.misc.ValidicyPredicate;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileFlowNetworkController extends TileEntity implements ITickable{
	
	private List<IEnergyController> controlled;
	private List<BlockPos> controlledPositions;
	
	private Iterator<IEnergyController> lightIn, lightOut;
	private Iterator<IEnergyController> forceIn, forceOut;
	private Iterator<IEnergyController> spatialIn, spatialOut;
	
	private long version = 0;
	private long lastCheck = 0;
	private boolean versionChange = false;
	private static final long MINIFY_CHECK_TIME = 200; //TODO: change to bigger value
	
	public TileFlowNetworkController() {
		controlled = new ArrayList<IEnergyController>();
		controlledPositions = new ArrayList<BlockPos>();
	}
	
	@Nonnull
	public IEnergyController getNext(Iterator<IEnergyController> iterator) {
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			iterator = controlled.iterator();
			return iterator.next();
		}
	}
	
	@Override
	public void update() {
		if (world.getTotalWorldTime() - lastCheck > MINIFY_CHECK_TIME) {
			CommonProxy.logger.info("Cleaning up controlled entities at {}, {}, {}", pos.getX(), pos.getY(), pos.getZ());
			lastCheck = world.getTotalWorldTime();
			controlled.removeIf(new ValidicyPredicate());
		}
		
		if (controlled.size()>0) {
			
		}
	}
	
	public void updateVersion() {
		if (!versionChange) {
			versionChange = true;
			version++;
		}
	}
	
	public boolean addControlled(BlockPos pos) {
		TileEntity tmp = world.getTileEntity(pos);
		
		if (tmp == null | tmp.equals(null)) {
			return false;
		}
		
		if (tmp instanceof IEnergyController) {
			controlledPositions.add(pos);
			controlled.add((IEnergyController)tmp);
			markDirty();
			return true;
		}
		return false;
	}
	
	
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
        CommonProxy.logger.info("Loading state of block at {} {} {} with version {}", this.pos.getX(), this.pos.getY(), this.pos.getZ(), version);
                
        if (compound.hasKey("version") &
        	compound.getLong("version") > version &
            compound.hasKey("controlled_x") & 
            compound.hasKey("controlled_y") & 
            compound.hasKey("controlled_z")) {
        	
        	version = compound.getLong("version");
        	versionChange = false;
        
	        int[] xs = compound.getIntArray("controlled_x");
	        int[] ys = compound.getIntArray("controlled_y");
	        int[] zs = compound.getIntArray("controlled_z");
	        
	        controlled.clear();
			controlledPositions.clear();
	        
	        int minlen = Math.min(xs.length, Math.min(ys.length, zs.length));
	        
	        for (int i=0;i<minlen;i++) {
	        	addControlled(new BlockPos(xs[i], ys[i], zs[i]));
	        }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        CommonProxy.logger.info("Saving state of block at {} {} {} with version {}", this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.version);
        
        controlled.removeIf(new ValidicyPredicate());        
        
        int curr = 0;
        int size = controlledPositions.size();
	    
        if (size>0) {
	        int[] xs, ys, zs;
	        xs = new int[size];
	        ys = new int[size];
	        zs = new int[size];
	        
	        for (BlockPos current : controlledPositions) {
	        	xs[curr] = current.getX();
	        	ys[curr] = current.getY();
	        	zs[curr] = current.getZ();
	        	curr++;
	        }
        
	        compound.setIntArray("controlled_x", xs);
        	compound.setIntArray("controlled_y", ys);
        	compound.setIntArray("controlled_z", zs);
        	
        	compound.setLong("version", version);
        }
        return compound;
    }

}
