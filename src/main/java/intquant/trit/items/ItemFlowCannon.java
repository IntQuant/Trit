package intquant.trit.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlowCannon extends ItemPowered {

	public ItemFlowCannon() {
		// TODO Auto-generated constructor stub
	}
	
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		// TODO Auto-generated method stub
		super.onUsingTick(stack, player, count);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		// TODO Auto-generated method stub
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// TODO Auto-generated method stub
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

}
