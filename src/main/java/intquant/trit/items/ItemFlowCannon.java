package intquant.trit.items;

import java.util.HashMap;
import java.util.Map;

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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemFlowCannon extends ItemPowered {
	protected int maxTime = 10;
	protected Map<EntityPlayer, Long> last_use = new HashMap<>();
	
	public ItemFlowCannon() {
		// TODO Auto-generated constructor stub
		max_light_st = 10000;
	}
	
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }
	
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return maxTime;
    }
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if (player.world.isRemote) {
			return;
		}
		super.onUsingTick(stack, player, count);
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
		
		Long lu = last_use.get(playerIn);
		if (lu == null) {
			lu = 0l;
		}

		if (light_st>=100 && lu+10 < worldIn.getTotalWorldTime()) {
			light_st -= 100;
			last_use.put(playerIn, worldIn.getTotalWorldTime());
			int damage = 10;
			
			Vec3d startPos =  playerIn.getLookVec().normalize().scale(0.5).add(playerIn.getPositionEyes(0));
			Vec3d endPos = playerIn.getLookVec().normalize().scale(100).add(startPos);
			
			RayTraceResult hit = RayTrace.tracePath(playerIn.world, startPos, endPos, 0.2, null);
			
			//player.world.createExplosion(null, hit.x, hit.y, hit.z, damage/10, true);
			
			if (hit != null && hit.entityHit != null && hit.entityHit instanceof EntityLivingBase) {
				((EntityLivingBase) hit.entityHit).attackEntityFrom(DamageSource.causePlayerDamage(playerIn), damage);
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
