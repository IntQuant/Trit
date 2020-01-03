package intquant.trit.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import intquant.trit.Trit;
import intquant.trit.blocks.BlockFlowLinker;
import intquant.trit.blocks.BlockFlowNetworkController;
import intquant.trit.blocks.BlockForceTransiever;
import intquant.trit.blocks.BlockModel;
import intquant.trit.blocks.BlockSolarPanel;
import intquant.trit.blocks.tiles.TileFlowLinker;
import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.blocks.tiles.TileForceTransiever;
import intquant.trit.blocks.tiles.TileSolarPanel;
import intquant.trit.items.ItemDebugTool;
import intquant.trit.items.ItemFlowCannon;
import intquant.trit.items.ItemForceSword;
import intquant.trit.items.ItemNetworkConfigurator;
import intquant.trit.items.ItemSpatialMiner;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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
	public static ResourceLocation mkResoureLoc(String name) {
		return new ResourceLocation(Trit.MODID, name);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		Material TritMat = new Material(MapColor.AIR);
		
		addBasicBlock("chassis", TritMat);
		addBasicBlock("adapter", TritMat);
		addBasicBlock("vehicle_core", TritMat);
		addBasicBlock("glass_obsidian", TritMat);

		
		BFL = regBlock(new BlockFlowLinker(TritMat), "flow_linker");
		FNC = regBlock(new BlockFlowNetworkController(TritMat), "flow_network_controller");
		regBlock(new BlockSolarPanel(TritMat), "solar_panel");
		regBlock(new BlockForceTransiever(TritMat), "force_transiever");

		GameRegistry.registerTileEntity(TileFlowNetworkController.class, mkResoureLoc("TileFlowNetworkController"));
		GameRegistry.registerTileEntity(TileFlowLinker.class, mkResoureLoc("TileFlowLinker"));
		GameRegistry.registerTileEntity(TileSolarPanel.class, mkResoureLoc("TileSolarPanel"));
		GameRegistry.registerTileEntity(TileForceTransiever.class, mkResoureLoc("TileForceTransiever"));
		
		
		for (Block current : trit_blocks) {
			event.getRegistry().register(current);
			current.setCreativeTab(Trit.TCT);
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
		addBasicItem("controller");
		
		regItem(new ItemNetworkConfigurator(), "network_configurator");
		regItem(new ItemDebugTool(), "tool_debug");
		
		regItem(new ItemFlowCannon(), "tool_flow_cannon");
		regItem(new ItemForceSword(), "tool_force_sword");
		regItem(new ItemSpatialMiner(), "tool_spatial_miner");
		
		logger.info("Done Item Registering");
		
		component_count = trit_items.size();
		
		for (Item current : trit_items) {
			event.getRegistry().register(current);
			current.setCreativeTab(Trit.TCT);
		}
		for (Block current : trit_blocks) {
			event.getRegistry().register(new ItemBlock(current).setRegistryName(current.getRegistryName()).setCreativeTab(Trit.TCT));
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
