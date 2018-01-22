package intquant.trit.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import intquant.trit.Trit;
import intquant.trit.blocks.BlockFlowLinker;
import intquant.trit.blocks.BlockFlowNetworkController;
import intquant.trit.blocks.BlockModel;
import intquant.trit.blocks.tiles.TileFlowLinker;
import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.items.ItemNetworkConfigurator;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public static Logger logger;
	
	public static Block FNC, BFL;
	
	public static int component_count = 0;
	public static List<Item> trit_items = new ArrayList<Item>();
	public static List<Block> trit_blocks = new ArrayList<Block>();
	
	public static Item regItem(Item item, String name) {
		Item tmp = item.setUnlocalizedName(Trit.MODID+"."+name).setRegistryName(Trit.MODID, name);
		trit_items.add(tmp);
		return tmp;
	}
	public static Item makeBasicItem(String name) {
		return new Item().setUnlocalizedName(Trit.MODID+"."+name).setRegistryName(Trit.MODID, name);
	}
	public static Item addBasicItem(String name) {
		Item tmp = makeBasicItem(name);
		trit_items.add(tmp);
		return tmp;
	}
	
	public static Block regBlock(Block block, String name) {
		Block tmp = block.setUnlocalizedName(Trit.MODID+"."+name).setRegistryName(Trit.MODID, name);
		trit_blocks.add(tmp);
		return tmp;
	}
	public static Block makeBasicBlock(Material material) {
		return new BlockModel(material);
	}
	public static Block addBasicBlock(String name, Material material) {
		return regBlock(makeBasicBlock(material), name);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		//event.getRegistry().register(new BlockSensor(Material.IRON).setUnlocalizedName("sensor").setRegistryName(OCSensors.MODID, "sensor"));
		//GameRegistry.registerTileEntity(TileEntitySensor.class, "TileEntitySensor");
		
		addBasicBlock("chassis", Material.IRON);
		addBasicBlock("adapter", Material.IRON);
		addBasicBlock("vehicle_core", Material.IRON);
		
		BFL = regBlock(new BlockFlowLinker(Material.IRON), "flow_linker");
		FNC = regBlock(new BlockFlowNetworkController(Material.IRON), "flow_network_controller");
		
		GameRegistry.registerTileEntity(TileFlowNetworkController.class, "TileFlowNetworkController");
		GameRegistry.registerTileEntity(TileFlowLinker.class, "TileFlowLinker");
		
		
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
		
		regItem(new ItemNetworkConfigurator(), "network_configurator");
		
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
