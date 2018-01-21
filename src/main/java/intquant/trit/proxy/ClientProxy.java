package intquant.trit.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    	for (Item current : CommonProxy.trit_items) {
    		CommonProxy.logger.info("Registering model {}", current.getRegistryName());
    		ModelLoader.setCustomModelResourceLocation(current, 0, new ModelResourceLocation(current.getRegistryName(), "inventory"));
    	}
    	for (Block current : CommonProxy.trit_blocks) {
    		CommonProxy.logger.info("Registering model {}", current.getRegistryName());
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(current), 0, new ModelResourceLocation(current.getRegistryName(), "inventory"));
    	}
    }
}