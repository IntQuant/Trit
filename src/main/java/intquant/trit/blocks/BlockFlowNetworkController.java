package intquant.trit.blocks;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.energy.IEnergyController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockFlowNetworkController extends BlockModel implements ITileEntityProvider{

	public BlockFlowNetworkController(Material materialIn) {
		super(materialIn);
		// TODO Auto-generated constructor stub
	}

	public BlockFlowNetworkController(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (placer.getName() != null) { //TODO: add checks
			((TileFlowNetworkController)worldIn.getTileEntity(pos)).setPlayer(placer.getName());
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn!=null && worldIn.isRemote) return true;

		TileEntity tile = worldIn.getTileEntity(pos);

		TextComponentTranslation component = null;
        
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
		

		
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		((TileFlowNetworkController)worldIn.getTileEntity(pos)).unload();
		// TODO Auto-generated method stub
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new TileFlowNetworkController();
	}

}
