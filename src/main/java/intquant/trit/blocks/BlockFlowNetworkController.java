package intquant.trit.blocks;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
