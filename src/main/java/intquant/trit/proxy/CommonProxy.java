package intquant.trit.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import intquant.trit.Trit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public static Logger logger;
	
	public static int component_count = 0;
	public static List<Item> trit_items = new ArrayList<Item>();
	public static List<Block> trit_blocks = new ArrayList<Block>();
	
	public static Item makeBasicItem(String name) {
		return new Item().setUnlocalizedName(Trit.MODID+"."+name).setRegistryName(Trit.MODID, name);
	}
	public static Item addBasicItem(String name) {
		Item tmp = makeBasicItem(name);
		trit_items.add(tmp);
		return tmp;
	}
	
	public static Block makeBasicBlock(String name, Material material) {
		return new Block(material).setUnlocalizedName(Trit.MODID+"."+name).setRegistryName(Trit.MODID, name);
	}
	public static Block addBasicBlock(String name, Material material) {
		Block tmp = makeBasicBlock(name, material);
		trit_blocks.add(tmp);
		return tmp;
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		//event.getRegistry().register(new BlockSensor(Material.IRON).setUnlocalizedName("sensor").setRegistryName(OCSensors.MODID, "sensor"));
		//GameRegistry.registerTileEntity(TileEntitySensor.class, "TileEntitySensor");
		
		addBasicBlock("chassis", Material.IRON);
		addBasicBlock("flow_linker", Material.GLASS);
		addBasicBlock("adapter", Material.GLASS);
		addBasicBlock("vehicle_core", Material.GLASS);
		
		
		for (Block current : trit_blocks) {
			event.getRegistry().register(current);
		}
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
	    logger.info("Starting Item Registering");
		addBasicItem("crystal_flow");
		addBasicItem("crystal_material");
		addBasicItem("crystal_spatial");
		addBasicItem("ingot_obsidian");
		addBasicItem("leninade");
		logger.info("Done Item Registering");
		
		component_count = trit_items.size();
		
		for (Item current : trit_items) {
			event.getRegistry().register(current);
		}
		for (Block current : trit_blocks) {
			event.getRegistry().register(new ItemBlock(current).setRegistryName(current.getRegistryName()));
		}
	}
	    
	
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        
        
    }
    
    public void init(FMLInitializationEvent event)
    {
        // some example code
        //logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    	
    }
    
    public void postInit(FMLPostInitializationEvent event)
    {
       
    }
}
