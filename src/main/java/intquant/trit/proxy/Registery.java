package intquant.trit.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import intquant.trit.Trit;
import intquant.trit.blocks.BlockFlowLinker;
import intquant.trit.blocks.BlockFlowNetworkController;
import intquant.trit.blocks.BlockModel;
import intquant.trit.blocks.BlockSolarPanel;
import intquant.trit.blocks.tiles.TileFlowLinker;
import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.blocks.tiles.TileSolarPanel;
import intquant.trit.items.ItemDebugTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class Registery {
	
	//public static Logger logger;
	
	public static Block FNC, BFL, SOLAR_PANEL, FORCE_TRANCIEVER;
	
	public static int component_count = 0;
	public static List<Item> trit_items = new ArrayList<Item>();
	public static List<Block> trit_blocks = new ArrayList<Block>();
	
	//@CapabilityInject(IEnergyStorage.class)
	//public static Capability<IEnergyStorage> ENERGY_HANDLER_CAPABILITY = null;

	public static Item regItem(Item item, String name) {
		Item tmp = item.setRegistryName(Trit.MODID, name);
		trit_items.add(tmp);
		return tmp;
	}
	public static Item makeBasicItem(String name) {
		return new Item(new Item.Properties().group(Trit.ITEM_GROUP)).setRegistryName(Trit.MODID, name);
	}
	public static Item addBasicItem(String name) {
		Item tmp = makeBasicItem(name);
		trit_items.add(tmp);
		return tmp;
	}
	
	public static Block regBlock(Block block, String name) {
		Block tmp = block.setRegistryName(Trit.MODID, name);
		trit_blocks.add(tmp);
		Item.Properties properties = new Item.Properties().group(Trit.ITEM_GROUP);
		trit_items.add(new BlockItem(block, properties).setRegistryName(Trit.MODID, name));
		return tmp;
	}
	public static Block makeBasicBlock(Block.Properties material) {
		return new BlockModel(Block.Properties.create(Material.IRON));
		//return new BlockModel(material);//.setHardness(2);
	}
	public static Block addBasicBlock(String name, Block.Properties material) {
		return regBlock(makeBasicBlock(material), name);
	}
	public static ResourceLocation mkResoureLoc(String name) {
		return new ResourceLocation(Trit.MODID, name);
	}
	
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		//Material TritMat = Material.IRON;
		Block.Properties TritMat = Block.Properties.create(Material.IRON);
		
		addBasicBlock("chassis", TritMat);
		addBasicBlock("adapter", TritMat);
		addBasicBlock("vehicle_core", TritMat);
		addBasicBlock("glass_obsidian", TritMat);
		addBasicBlock("flowbricks", TritMat);
		addBasicBlock("ship_hull", TritMat);
		addBasicBlock("ship_interior_block", TritMat);
		addBasicBlock("ship_lamp", TritMat);
		addBasicBlock("blackhole_projector", TritMat);
		
		BFL = regBlock(new BlockFlowLinker(TritMat), "flow_linker");
		FNC = regBlock(new BlockFlowNetworkController(TritMat), "flow_network_controller");
		SOLAR_PANEL = regBlock(new BlockSolarPanel(TritMat), "solar_panel");
		//FORCE_TRANCIEVER = regBlock(new BlockForceTransiever(TritMat), "force_transiever");

		
		for (Block current : trit_blocks) {
			event.getRegistry().register(current);
			//current.setCreativeTab(Trit.TCT);
		}
	}

	public static void registerTEs(RegistryEvent.Register<TileEntityType<?>> event) {
		IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
		registry.register(TileEntityType.Builder.create(TileFlowNetworkController::new, FNC).build(null).setRegistryName(Trit.MODID, "flow_network_controller"));
		registry.register(TileEntityType.Builder.create(TileFlowLinker::new, BFL).build(null).setRegistryName(Trit.MODID, "flow_linker"));
		registry.register(TileEntityType.Builder.create(TileSolarPanel::new, SOLAR_PANEL).build(null).setRegistryName(Trit.MODID, "solar_panel"));
		//registry.register(TileEntityType.Builder.create(TileForceTransiever::new, FORCE_TRANCIEVER).build(null).setRegistryName("forcetranciever"));
		
	}

	public static void registerItems(RegistryEvent.Register<Item> event) {
	    //logger.info("Starting Item Registering");
		
	    addBasicItem("crystal_flow");
		addBasicItem("crystal_material");
		addBasicItem("crystal_spatial");
		addBasicItem("ingot_obsidian");
		addBasicItem("leninade");
		//addBasicItem("controller");
		
		//regItem(new ItemNetworkConfigurator(), "network_configurator");
		regItem(new ItemDebugTool(), "tool_debug");
		
		//regItem(new ItemFlowCannon(), "tool_flow_cannon");
		//regItem(new ItemForceSword(), "tool_force_sword");
		//regItem(new ItemSpatialMiner(), "tool_spatial_miner");
		
		//logger.info("Done Item Registering");
		
		component_count = trit_items.size();
		
		for (Item current : trit_items) {
			event.getRegistry().register(current);
			//current.setCreativeTab(Trit.TCT);
		}/*
		for (Block current : trit_blocks) {
            Item.Properties properties = new Item.Properties();//.group();
			event.getRegistry().register(new BlockItem(current, properties).setRegistryName(current.getRegistryName()));//.setCreativeTab(Trit.TCT));
		}*/
	}
}
