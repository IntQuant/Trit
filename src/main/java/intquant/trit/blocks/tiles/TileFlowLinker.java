package intquant.trit.blocks.tiles;

import intquant.trit.energy.IEnergyController;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class TileFlowLinker extends TileEntity implements ITickable{
	
	public static int cubeHalfRange = 8;
	public static int cubeRange = cubeHalfRange*2+1;
	public static int squaredRange = cubeRange * cubeRange;
	public static int maxCurrent = squaredRange * cubeRange;
	
	public static int ITERATION_UPDATE_TOKENS = 32;
	
	private TileFlowNetworkController controller = null;
	private BlockPos controllerPos = null;
	private int controllerDim = 0;
	
	private boolean isSeeking = false;
	private int current = 0;
	private int updateTokens = 0;
	
	
	public TileFlowLinker() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setController(TileFlowNetworkController controller) {
		this.controller = controller;
	}
	
	public void startSeeking() {
		if (!isSeeking) {
			CommonProxy.logger.info("Started seeking for energy controllers");
			current = 0;
			isSeeking = true;
		}
	}
	
	
	public boolean setControllerPos(BlockPos pos, int dim) {
		//TODO add dimension handling
		World worldC = DimensionManager.getWorld(dim);
		if (worldC.isBlockLoaded(pos)) {
			TileEntity tile = worldC.getTileEntity(pos);
			if (tile != null && tile instanceof TileFlowNetworkController) {
				try {
					controller = (TileFlowNetworkController)tile;
					controllerPos = pos;
					controllerDim = dim;
					markDirty();
					return true;
				} catch (ClassCastException e) {
					return false;
				}
			} else {
				controllerPos = null;
			}
		}
		else {
			controllerPos = pos;
			controllerDim = dim;
			markDirty();
		}
		return false;
	}
	
	public void update() {
		updateTokens = ITERATION_UPDATE_TOKENS;
		
		if (controller == null && controllerPos != null) {
			setControllerPos(controllerPos, controllerDim);
			return;
		}
		
		if (isSeeking && controller != null) {			
			while (current<maxCurrent & updateTokens>0) {
				BlockPos currentPos = new BlockPos(current%cubeRange, current/cubeRange%cubeRange, current/squaredRange).add(-cubeHalfRange, -cubeHalfRange, -cubeHalfRange).add(this.pos);
				
				
				//CommonProxy.logger.info("Seeking for IEnergyController-s, currently at {} {} {}", currentPos.getX(), currentPos.getY(), currentPos.getZ());
				TileEntity tile = world.getTileEntity(currentPos);
				if (tile != null && tile instanceof IEnergyController) {
					IEnergyController c = (IEnergyController)tile;
					CommonProxy.logger.info("Found energy controller");
					if (!c.isValid()) {
						CommonProxy.logger.info("Adding energy controller");
						c.setLinker(pos);
						controller.addControlled(currentPos);
					}
				}
				current++;
				updateTokens--;
			}
			if (current>=maxCurrent) {
				isSeeking = false;
			}
		}
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
        if (compound.hasKey("pos", 99)) {
        	setControllerPos(BlockPos.fromLong(compound.getLong("pos")), compound.getInteger("dim"));
        }
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        compound.setLong("pos", controllerPos.toLong());
        compound.setInteger("dim", controllerDim);
        
        return compound;
	}
	
	
	

}
