package intquant.trit.items;

import intquant.trit.blocks.tiles.TileFlowLinker;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.block.Block;
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

public class ItemNetworkConfigurator extends Item {
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
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
			
			CommonProxy.logger.info("Saved block data");
			
			return EnumActionResult.SUCCESS;
		}
		
		if (blk != null & blk.equals(CommonProxy.BFL)) {
			
			if (worldIn.isRemote) {
				return EnumActionResult.SUCCESS;
			}
			
			ItemStack selected = player.getHeldItem(hand);
			
			NBTTagCompound nbt = selected.getTagCompound();
			if (nbt == null) nbt = new NBTTagCompound();
			
			if (!(nbt.hasKey("x", 99) & nbt.hasKey("y", 99) & nbt.hasKey("z", 99))) {
				return EnumActionResult.SUCCESS;
			}
			
			BlockPos origin = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
			if (nbt.getInteger("dim") == player.dimension) {
				TileEntity tile = worldIn.getTileEntity(pos);
				if (tile != null & tile instanceof TileFlowLinker) { 
					try { //Just why not
						if (((TileFlowLinker)tile).setControllerPos(origin))
							CommonProxy.logger.info("Loaded block data");
						
					} catch (ClassCastException e) {
						CommonProxy.logger.warn("Failed type casting while saving block data");
					}
				}
			}
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.PASS;
	}

}
