package intquant.trit.items;

import java.util.List;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.energy.IEnergyController;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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
		if (worldIn.isRemote) {
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
		
		
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
			
			TextComponentTranslation component = new TextComponentTranslation("msg.trit.linked_to");
	        component.getStyle().setColor(TextFormatting.AQUA);
	        player.sendStatusMessage(component, false);
			
			return EnumActionResult.SUCCESS;
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		load(stack);
		if (this.controllerPos == null) {
			tooltip.add("Not connected");
		}
		tooltip.add("Stores:");
		if (this.max_light_st>0) {tooltip.add("Light   "+this.light_st+"/"+this.max_light_st);}
		if (this.max_force_st>0) {tooltip.add("Force   "+this.force_st+"/"+this.max_force_st);}
		if (this.max_spatial_st>0) {tooltip.add("Spatial "+this.spatial_st+"/"+this.max_spatial_st);}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	
	protected void load(ItemStack stack) {
		NBTTagCompound nbt = null;
		controller = null;
		if (stack == null) {
			nbt = new NBTTagCompound();
		} else {
			nbt = stack.getTagCompound();
			if (nbt == null) nbt = new NBTTagCompound();
		}
		
		if (nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z") && nbt.hasKey("dim")) {
			controllerPos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
			controllerDim = nbt.getInteger("dim");
		} else {
			controllerPos = null;
		}
		
		if (controllerPos!=null) {
			World worldC = DimensionManager.getWorld(controllerDim);
			if (worldC != null && worldC.isBlockLoaded(controllerPos)) {
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
		} else light_st = 0;
		
		if (nbt.hasKey("fe", 99)) {
			force_st = nbt.getLong("fe");
		} else force_st = 0;
		
		if (nbt.hasKey("se", 99)) {
			spatial_st = nbt.getLong("se");
		} else spatial_st = 0;
	}
	
	
	protected void save(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) nbt = new NBTTagCompound();
		
		nbt.setLong("le", light_st);
		nbt.setLong("fe", force_st);
		nbt.setLong("se", spatial_st);
		
		stack.setTagCompound(nbt);
	}
	
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (isSelected) {
			updateTokens += 2;
		} else {
			updateTokens++;
		}
		if (isSelected && updateTokens >= 20) {
			updateTokens -= 20;
			tokenizedUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		}
		if (updateTokens >= 100) {
			updateTokens -= 100;
			tokenizedUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		}
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
