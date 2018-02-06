package intquant.trit.blocks;

import intquant.trit.blocks.tiles.TileForceTransiever;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockForceTransiever extends BlockModel implements ITileEntityProvider {

	public BlockForceTransiever(Material materialIn) {
		super(materialIn);
		// TODO Auto-generated constructor stub
	}

	public BlockForceTransiever(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		// TODO Auto-generated constructor stub
	}
	
	private long sqr(double motionX) {
		return (long) (motionX*motionX);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		// TODO Auto-generated method stub
		TileEntity tile = worldIn.getTileEntity(pos);
		
		final double mlt = 10;
		
		long speed = sqr(entityIn.motionX*mlt) + sqr(entityIn.motionY*mlt) + sqr(entityIn.motionZ*mlt);
		
		CommonProxy.logger.info("Collided with entity at speed {}", speed);
		
		if (tile != null && tile instanceof TileForceTransiever) {
			((TileForceTransiever) tile).onCollision(speed);
		}
		
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileForceTransiever();
	}
	

}
