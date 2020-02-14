package intquant.trit.blocks;

import javax.annotation.Nullable;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockFlowNetworkController extends BlockModel {
	public BlockFlowNetworkController(Properties properties) {
		super(properties);
	}
	@Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileFlowNetworkController();
    }
}
