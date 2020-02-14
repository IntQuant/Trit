package intquant.trit;

import java.util.Random;

import intquant.trit.proxy.Registery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Trit.MODID)
public class Trit {
	public static final String MODID   = "trit";
	public static final String NAME    = "Trit";
	public static final String VERSION = "0.2.0";
	
	public static Random rand = new Random();
	public static DamageSource damage_source_light = new DamageSource("trit.damagesource.light");
    public static Logger logger = LogManager.getLogger();

    public static final ItemGroup ITEM_GROUP = new ItemGroup("trit") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registery.trit_items.get(0));
        }
    };

    public Trit() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            //event.getRegistry().register(new FirstBlock());
            Registery.registerBlocks(event);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            //event.getRegistry().register(new BlockItem(ModBlocks.FIRSTBLOCK, new Item.Properties()).setRegistryName("firstblock"));
            Registery.registerItems(event);
        }

        @SubscribeEvent
        public static void onTERegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            Registery.registerTEs(event);
        }
    }

}
