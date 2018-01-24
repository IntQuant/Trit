package intquant.trit.items;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.energy.IEnergyController;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (isSelected) {
			updateTokens += 10;
		} else {
			updateTokens++;
		}
		if (updateTokens >= 50) {
			updateTokens -= 50;
			tokenizedUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		}
		// TODO Auto-generated method stub
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	public void tokenizedUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if ((controller==null || controller.isInvalid()) && controllerPos!=null) {
			World worldC = DimensionManager.getWorld(controllerDim);
			if (worldC.isBlockLoaded(controllerPos)) {
				TileEntity tile = worldC.getTileEntity(controllerPos);
				if (tile != null && tile instanceof TileFlowNetworkController) {
					controller = (TileFlowNetworkController) tile;
					return;
				}
				else {
					controllerPos = null;
				}
			}
		}
		if (controllerPos==null) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z") && nbt.hasKey("dim")) {
				controllerPos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
				controllerDim = nbt.getInteger("dim");
				return;
			}
		}
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
		}
	}
	
	

	@Override
	public void setLinker(BlockPos pos) {
	}

	@Override
	public BlockPos getLinker() {
		return null;
	}

}