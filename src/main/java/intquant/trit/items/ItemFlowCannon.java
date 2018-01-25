package intquant.trit.items;

import intquant.trit.Trit;
import intquant.trit.misc.RayTrace;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemFlowCannon extends ItemPowered {
	
	protected int total_damage = 0;
	protected int maxTime = 1000;
	
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
		
		load(stack);
		
		if (light_st>=10) {
			light_st -= 10;
			
			int damage = 1;
			
			Vec3d startPos =  player.getLookVec().normalize().scale(0.5).add(player.getPositionEyes(0));
			Vec3d endPos = player.getLookVec().normalize().scale(100).add(startPos);
			
			RayTraceResult hit = RayTrace.tracePath(player.world, startPos, endPos, 0.2, null);
			
			//player.world.createExplosion(null, hit.x, hit.y, hit.z, damage/10, true);
			
			if (hit != null && hit.entityHit != null && hit.entityHit instanceof EntityLivingBase) {
				CommonProxy.logger.info("Entity hit: {} {} {}", hit.entityHit.posX, hit.entityHit.posY, hit.entityHit.posZ);
				((EntityLivingBase) hit.entityHit).attackEntityFrom(Trit.damage_source_light, damage);
			}
			
			
			
			
			
			CommonProxy.logger.info("Current damage {}", damage);
			
			save(stack);
			return;
		}
		
		//CommonProxy.logger.info("Item tick");
		
		// TODO Auto-generated method stub
		super.onUsingTick(stack, player, count);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		total_damage = 0;
		CommonProxy.logger.info("Item rightcliced");
		// TODO Auto-generated method stub
		playerIn.setActiveHand(handIn);
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
