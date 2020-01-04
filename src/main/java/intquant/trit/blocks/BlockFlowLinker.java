package intquant.trit.blocks;

import intquant.trit.TESR.TESRFlowLinker;
import intquant.trit.TESR.TESRFlowNetworkController;
import intquant.trit.blocks.tiles.TileEnergyController;
import intquant.trit.blocks.tiles.TileFlowLinker;
import intquant.trit.misc.FacingHelper;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFlowLinker extends BlockModel implements ITileEntityProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockFlowLinker(Material materialIn) {
		super(materialIn);
	}

	public BlockFlowLinker(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, FacingHelper.getFacingFromEntity(pos, placer)), 2);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (worldIn.isRemote) {
			return true;
		}
		//TileEntity tile = worldIn.getTileEntity(pos);
		//if (tile != null && tile instanceof TileFlowLinker) {
		//	((TileFlowLinker)tile).startSeeking();
		//}
        return true;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileFlowLinker();
	}
	
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
    }

}
