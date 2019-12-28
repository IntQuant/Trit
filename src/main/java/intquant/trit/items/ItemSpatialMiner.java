package intquant.trit.items;

import intquant.trit.misc.RayTrace;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemSpatialMiner extends ItemPowered {
	protected long last_use = 0;
	
	public ItemSpatialMiner() {
		max_spatial_st = 1000;
	}
	
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }
	
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 10;
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.world.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		
		playerIn.setActiveHand(handIn);
		
		ItemStack stack = playerIn.getActiveItemStack();

		load(stack);

		//CommonProxy.logger.info("last_use {} {}", last_use, worldIn.getTotalWorldTime());
		
		if (spatial_st>=10 && last_use+10 < worldIn.getTotalWorldTime()) {
			
			//last_use = worldIn.getTotalWorldTime();
			
			
			Vec3d startPos =  playerIn.getLookVec().normalize().scale(0.5).add(playerIn.getPositionEyes(0));
			Vec3d endPos = playerIn.getLookVec().normalize().scale(10).add(startPos);
			
			RayTraceResult hit = worldIn.rayTraceBlocks(startPos, endPos, false);

			
			//player.world.createExplosion(null, hit.x, hit.y, hit.z, damage/10, true);
			
			if (hit != null) {
				BlockPos pos = hit.getBlockPos();
				float hardness = worldIn.getBlockState(pos).getBlockHardness(worldIn, pos);
				if (hardness > 0 && spatial_st > hardness+2) {
					worldIn.destroyBlock(pos, true);
					spatial_st -= (int)hardness+1;
				}
				
			}
			
			
			save(stack);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		// TODO Auto-generated method stub
		//total_damage = (int) Math.min((maxTime - timeLeft), light_st);
		//light_st -= total_damage;
		//CommonProxy.logger.info("Current damage {}", total_damage);		
	}
}
