package intquant.trit.misc;

import intquant.trit.Trit;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TritCreativeTab extends CreativeTabs {
	public TritCreativeTab() {
		super(Trit.NAME);
	}
	
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(CommonProxy.trit_items.get(0));
	}

}
