package intquant.trit.blocks.tiles;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;

import intquant.trit.Trit;
import intquant.trit.energy.IEnergyController;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TileFlowNetworkController extends TileEnergyController implements ITickable{
	
	private final int MAX_ITERATIONS = 1;
	
	private Ticket chunk_ticket;
	private String player;
	
	private List<IEnergyController> controlled;
	private List<BlockPos> controlledPositions;
	private List<BlockPos> potentialPositions;
	
	private Iterator<IEnergyController> LightIn, LightOut;
	private Iterator<IEnergyController> ForceIn, ForceOut;
	private Iterator<IEnergyController> SpatialIn, SpatialOut;
	
	private int iteration_score = 0;
	
	private IEnergyController in, out;
	
	private long version = 0;
	private long lastCheck = 0;
	private boolean versionChange = false;
	private static final long MINIFY_CHECK_TIME = 100; //TODO: change to bigger value
	
	public TileFlowNetworkController() {
		controlled = new CopyOnWriteArrayList<IEnergyController>();
		controlledPositions = new CopyOnWriteArrayList<BlockPos>();
		potentialPositions = new CopyOnWriteArrayList<BlockPos>();
		this.setDoAccept(true);
		this.setMaxLightStorage(10000);
		this.setMaxForceStorage(10000);
		this.setMaxSpatialStorage(10000);
	}
	
    @Nonnull
    public IEnergyController getNextLightIn() {
            if (LightIn != null && LightIn.hasNext()) {
                    IEnergyController tmp = LightIn.next();
                    return tmp;

            } else {
                    LightIn = controlled.iterator();
                    return (IEnergyController) this;
            }
    }

    @Nonnull
    public IEnergyController getNextLightOut() {
            if (LightOut != null && LightOut.hasNext()) {
                    IEnergyController tmp = LightOut.next();
                    return tmp;

            } else {
                    LightOut = controlled.iterator();
                    return (IEnergyController) this;
            }
    }

    @Nonnull
    public IEnergyController getNextForceIn() {
            if (ForceIn != null && ForceIn.hasNext()) {
                    IEnergyController tmp = ForceIn.next();
                    return tmp;

            } else {
                    ForceIn = controlled.iterator();
                    return (IEnergyController) this;
            }
    }

    @Nonnull
    public IEnergyController getNextForceOut() {
            if (ForceOut != null && ForceOut.hasNext()) {
                    IEnergyController tmp = ForceOut.next();
                    return tmp;

            } else {
                    ForceOut = controlled.iterator();
                    return (IEnergyController) this;
            }
    }

    @Nonnull
    public IEnergyController getNextSpatialIn() {
            if (SpatialIn != null && SpatialIn.hasNext()) {
                    IEnergyController tmp = SpatialIn.next();
                    return tmp;

            } else {
                    SpatialIn = controlled.iterator();
                    return (IEnergyController) this;
            }
    }

    @Nonnull
    public IEnergyController getNextSpatialOut() {
            if (SpatialOut != null && SpatialOut.hasNext()) {
                    IEnergyController tmp = SpatialOut.next();
                    return tmp;

            } else {
                    SpatialOut = controlled.iterator();
                    return (IEnergyController) this;
            }
    }
	
	public int getControlledSize() {
		return controlled.size();
	}
	
	public void setPlayer(String name) {
		player = name;
	}
	
	public void unload() {
		ForgeChunkManager.releaseTicket(chunk_ticket);
	}
	
	@Override
	public void update() {
		
		
		if (world.getTotalWorldTime() - lastCheck > MINIFY_CHECK_TIME) {
			//CommonProxy.logger.info("Cleaning up controlled entities at {}, {}, {}", pos.getX(), pos.getY(), pos.getZ());
			lastCheck = world.getTotalWorldTime();
			//controlled.removeIf(new ValidicyPredicate());
			for (BlockPos pos : potentialPositions) {
				if (this.addControlled(pos)) {
					potentialPositions.remove(pos);
				}
			}
		}
		
		if (chunk_ticket == null && player != null) {
			chunk_ticket = ForgeChunkManager.requestPlayerTicket(Trit.instance, player, world, Type.NORMAL);
			chunk_ticket.setChunkListDepth(1);
			ForgeChunkManager.forceChunk(chunk_ticket, world.getChunkFromBlockCoords(pos).getPos());
		}
		
		if (controlled.size()>0) {
			iteration_score = MAX_ITERATIONS;
			while (iteration_score>0) {
				while (iteration_score>0 && !(in != null && in.isValid() && in.getProvideableLight()>0)) {
					iteration_score--;
					in = getNextLightIn();
					//CommonProxy.logger.info("Changed light input to {}", in.getDebugId());
				}
				while (iteration_score>0 && !(out != null && out.isValid() && out.getAcceptableLight()>0)) {
					iteration_score--;
					out = getNextLightOut();
					//CommonProxy.logger.info("Changed light output");
				}
				if (in != null && out != null && in.isValid() && out.isValid()) {
					//CommonProxy.logger.info("Initiated light transfer");
					out.acceptLight(in.provideLight(out.getAcceptableLight()));
				}
				iteration_score--;				
			}
			
			iteration_score = MAX_ITERATIONS;
			while (iteration_score>0) {
				while (iteration_score>0 && !(in != null && in.isValid() && in.getProvideableForce()>0)) {
					iteration_score--;
					in = getNextForceIn();
				}
				while (iteration_score>0 && !(out != null && out.isValid() && out.getAcceptableForce()>0)) {
					iteration_score--;
					out = getNextForceOut();
				}
				if (in != null && out != null && in.isValid() && out.isValid()) {
					out.acceptForce(in.provideForce(out.getAcceptableForce()));
				}
				iteration_score--;				
			}
			
			iteration_score = MAX_ITERATIONS;
			while (iteration_score>0) {
				while (iteration_score>0 && !(in != null && in.isValid() && in.getProvideableSpatial()>0)) {
					iteration_score--;
					in = getNextSpatialIn();
				}
				while (iteration_score>0 && !(out != null && out.isValid() && out.getAcceptableSpatial()>0)) {
					iteration_score--;
					out = getNextSpatialOut();
				}
				if (in != null && out != null && in.isValid() && out.isValid()) {
					out.acceptSpatial(in.provideSpatial(out.getAcceptableSpatial()));
				}
				iteration_score--;				
			}
		}
	}
	
	public void updateVersion() {
		if (!versionChange) {
			versionChange = true;
			version++;
		}
	}
	
	public boolean addControlled(BlockPos pos) {
		if (world != null && world.isRemote) {
			return true;
		}
		if (world != null && world.isBlockLoaded(pos)) {
			TileEntity tmp = world.getTileEntity(pos);
			
			if (tmp == null) {
				return true;
			}
			
			if (tmp instanceof IEnergyController && !controlled.contains(tmp)) {
				controlledPositions.add(pos);
				controlled.add((IEnergyController)tmp);
				markDirty();
				return true;
			}
			return true;
		} else {
			potentialPositions.add(pos);
			return false;
		}
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
        if (world!=null && world.isRemote) {
        	return;
        }
                
        if (compound.hasKey("version") &&
        	compound.getLong("version") > version &&
            compound.hasKey("controlled_x") &&
            compound.hasKey("controlled_y") && 
            compound.hasKey("controlled_z")) {
        	
            CommonProxy.logger.info("Loading state of block at {} {} {} with version {}", this.pos.getX(), this.pos.getY(), this.pos.getZ(), version);
        	
        	version = compound.getLong("version");
        	versionChange = false;
        
	        int[] xs = compound.getIntArray("controlled_x");
	        int[] ys = compound.getIntArray("controlled_y");
	        int[] zs = compound.getIntArray("controlled_z");
	        
	        controlled.clear();
			controlledPositions.clear();
			potentialPositions.clear();
	        
	        int minlen = Math.min(xs.length, Math.min(ys.length, zs.length));
	        
	        for (int i=0;i<minlen;i++) {
	        	addControlled(new BlockPos(xs[i], ys[i], zs[i]));
	        }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        if (world!=null && world.isRemote) {
        	return compound;
        }
        
        CommonProxy.logger.info("Saving state of block at {} {} {} with version {}", this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.version);
        
        //controlled.removeIf(new ValidicyPredicate());        
        
        int curr = 0;
        int size = controlledPositions.size() + potentialPositions.size();
	    
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
	        for (BlockPos current : potentialPositions) {
	        	xs[curr] = current.getX();
	        	ys[curr] = current.getY();
	        	zs[curr] = current.getZ();
	        	curr++;
	        }
        
	        compound.setIntArray("controlled_x", xs);
        	compound.setIntArray("controlled_y", ys);
        	compound.setIntArray("controlled_z", zs);
        	
        	version++;
        	
        	compound.setLong("version", version);
        }
        markDirty();
        return compound;
    }

}
