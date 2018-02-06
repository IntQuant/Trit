package intquant.trit.items;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.energy.IEnergyController;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ItemPowered extends Item implements IEnergyController {
	
	protected TileFlowNetworkController controller = null;
	protected BlockPos controllerPos = null;
	protected int controllerDim = 0;
	protected int updateTokens = 0;
	
	protected long light_st   = 0;
	protected long force_st   = 0;
	protected long spatial_st = 0;
	
	protected long max_light_st   = 0;
	protected long max_force_st   = 0;
	protected long max_spatial_st = 0;
	
	@Override
	public long getAcceptableLight() {
		return max_light_st - light_st;
	}

	@Override
	public long getAcceptableForce() {
		return max_force_st - force_st;
	}

	@Override
	public long getAcceptableSpatial() {
		return max_spatial_st - spatial_st;
	}

	@Override
	public long manageLight(long value) {
		return light_st += value;
	}

	@Override
	public long manageForce(long value) {
		return force_st += value;
	}

	@Override
	public long manageSpatial(long value) {
		return spatial_st += value;
	}

	public ItemPowered() {
		super();
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		Block blk = worldIn.getBlockState(pos).getBlock();
		
		if (blk != null & blk.equals(CommonProxy.FNC)) {
			
			if (worldIn.isRemote) {
				return EnumActionResult.SUCCESS;
			}
			
			ItemStack selected = player.getHeldItem(hand);
			
			NBTTagCompound nbt = selected.getTagCompound();
			if (nbt == null) nbt = new NBTTagCompound();
			
			nbt.setInteger("x", pos.getX());
			nbt.setInteger("y", pos.getY());
			nbt.setInteger("z", pos.getZ());
			nbt.setInteger("dim", player.dimension);
			
			selected.setTagCompound(nbt);
			
			controllerPos = pos;
			controllerDim = player.dimension;
			
			CommonProxy.logger.info("Saved block data");
			
			return EnumActionResult.SUCCESS;
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	protected void load(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) nbt = new NBTTagCompound();
		
		if (nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z") && nbt.hasKey("dim")) {
			controllerPos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
			controllerDim = nbt.getInteger("dim");
		}
		
		if (controllerPos!=null) {
			World worldC = DimensionManager.getWorld(controllerDim);
			if (worldC.isBlockLoaded(controllerPos)) {
				TileEntity tile = worldC.getTileEntity(controllerPos);
				if (tile != null && tile instanceof TileFlowNetworkController) {
					controller = (TileFlowNetworkController) tile;
				}
				else {
					controllerPos = null;
				}
			}
		}
		
		if (nbt.hasKey("le", 99)) {
			light_st = nbt.getLong("le");
		}
		
		if (nbt.hasKey("fe", 99)) {
			force_st = nbt.getLong("fe");
		}
		
		if (nbt.hasKey("se", 99)) {
			spatial_st = nbt.getLong("se");
		}
		
		//CommonProxy.logger.info("Loaded data from item");
		//stack.setTagCompound(nbt);
	}
	protected void save(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) nbt = new NBTTagCompound();
		
		nbt.setLong("le", light_st);
		nbt.setLong("fe", force_st);
		nbt.setLong("se", spatial_st);
		
		stack.setTagCompound(nbt);
		
		//CommonProxy.logger.info("Saved data to item");
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (isSelected) {
			updateTokens += 2;
		} else {
			updateTokens++;
		}
		if (updateTokens >= 100) {
			updateTokens -= 100;
			tokenizedUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		}
		// TODO Auto-generated method stub
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	public void tokenizedUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (worldIn == null || worldIn.isRemote) {
			return;
		}
		
		load(stack);
		
		if (controller != null && controller.isValid()) {
				
			long acc = 0;
			acc = Math.min(controller.getForcedProvideableLight(), this.getAcceptableLight());
			controller.manageLight(-acc);
			this.manageLight(acc);

			acc = Math.min(controller.getForcedProvideableForce(), this.getAcceptableForce());
			controller.manageForce(-acc);
			this.manageForce(acc);

			acc = Math.min(controller.getForcedProvideableSpatial(), this.getAcceptableSpatial());
			controller.manageSpatial(-acc);
			this.manageSpatial(acc);
			
			//stack.setTagCompound(nbt);
		}
		
		save(stack);
	}
	
	

	@Override
	public void setLinker(BlockPos pos) {
	}

	@Override
	public BlockPos getLinker() {
		return null;
	}

}
