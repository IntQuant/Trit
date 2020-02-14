package intquant.trit.blocks;

import javax.annotation.Nullable;

import intquant.trit.blocks.tiles.TileFlowLinker;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockFlowLinker extends BlockModel {

	//public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	
	/*@Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		//world.setBlockState(pos, state.withProperty(FACING, FacingHelper.getFacingFromEntity(pos, placer)), 2);
	}*/

	public BlockFlowLinker(Properties properties) {
		super(properties);
	}

	@Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileFlowLinker(0);
    }

	/*
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }*/

}
