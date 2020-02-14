package intquant.trit.blocks;

import javax.annotation.Nullable;

import intquant.trit.blocks.tiles.TileSolarPanel;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockReader;

public class BlockSolarPanel extends BlockModel{
	public BlockSolarPanel(Properties properties) {
        super(properties);
    }
	
	public static AxisAlignedBB AABB = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 0.2f, 1.0f);
    
    /*
	@Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
    }
    */
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSolarPanel();
    }
}
