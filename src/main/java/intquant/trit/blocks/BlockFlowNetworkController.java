package intquant.trit.blocks;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFlowNetworkController extends BlockModel implements ITileEntityProvider{

	public BlockFlowNetworkController(Material materialIn) {
		super(materialIn);
	}

	public BlockFlowNetworkController(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileFlowNetworkController();
	}

}
