package intquant.trit.items;

import intquant.trit.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
        return EnumAction.BOW;
    }
	
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return maxTime;
    }
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if (light_st>=count*0.2) {
			light_st -= count*0.2;
			
			int damage = (int)Math.pow(count/10, 1.2);
			
			total_damage += damage;
			
			CommonProxy.logger.info("Current damage {}", total_damage);
			
			return;
		}
		
		CommonProxy.logger.info("Item tick");
		
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
		
		CommonProxy.logger.info("Current damage {}", total_damage);	
		
		Vec3d hit = entityLiving.rayTrace(100, 0).hitVec;
		
		worldIn.createExplosion(null, hit.x, hit.y, hit.z, total_damage/10, true);
		
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// TODO Auto-generated method stub
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

}
