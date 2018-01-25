package intquant.trit.blocks;

import intquant.trit.blocks.tiles.TileSolarPanel;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSolarPanel extends BlockModel implements ITileEntityProvider{

	public BlockSolarPanel(Material materialIn) {
		super(materialIn);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSolarPanel();
	}
	
	public static AxisAlignedBB AABB = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 0.2f, 1.0f);
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}
	
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

}
