package intquant.trit.items;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.energy.IEnergyController;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemDebugTool extends Item {

	public ItemDebugTool() {
		super();
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		}
		
		Block blk = worldIn.getBlockState(pos).getBlock();
		TileEntity tile = worldIn.getTileEntity(pos);
		
		TextComponentTranslation component = new TextComponentTranslation("debug_msg.trit.block_name", blk.getLocalizedName());
        component.getStyle().setColor(TextFormatting.AQUA);
        player.sendStatusMessage(component, false);
        
        if (tile != null && tile instanceof IEnergyController) {
        	IEnergyController ctrl = (IEnergyController) tile;
        	
        	component = new TextComponentTranslation("debug_msg.trit.energy_controller_data_0", ctrl.isValid(), ctrl.manageLight(0), ctrl.manageForce(0), ctrl.manageSpatial(0));
            component.getStyle().setColor(TextFormatting.AQUA);
            player.sendStatusMessage(component, false);
            
            component = new TextComponentTranslation("debug_msg.trit.energy_controller_data_1", ctrl.getAcceptableLight(), ctrl.getAcceptableForce(), ctrl.getAcceptableSpatial());
            component.getStyle().setColor(TextFormatting.AQUA);
            player.sendStatusMessage(component, false);
            
            component = new TextComponentTranslation("debug_msg.trit.energy_controller_data_2", ctrl.getProvideableLight(), ctrl.getProvideableForce(), ctrl.getProvideableSpatial());
            component.getStyle().setColor(TextFormatting.AQUA);
            player.sendStatusMessage(component, false);
        }
        
        if (tile != null && tile instanceof TileFlowNetworkController) {
        	TileFlowNetworkController ctrl = (TileFlowNetworkController) tile;
        	
        	component = new TextComponentTranslation("debug_msg.trit.network_controller_data_0", ctrl.getControlledSize());
            component.getStyle().setColor(TextFormatting.AQUA);
            player.sendStatusMessage(component, false);
        }
		
		
		return EnumActionResult.SUCCESS;
	}

}
