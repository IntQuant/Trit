package intquant.trit.blocks;

import intquant.trit.blocks.tiles.TileFlowLinker;
import intquant.trit.blocks.tiles.TileFlowNetworkController;
import intquant.trit.blocks.tiles.TileSolarPanel;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {
    @ObjectHolder("trit:flow_linker")
    public static TileEntityType<TileFlowLinker> TILE_FLOWLINKER;
    @ObjectHolder("trit:flow_network_controller")
    public static TileEntityType<TileFlowNetworkController> TILE_FLOWNETWORKCONTROLLER;
    @ObjectHolder("trit:solar_panel")
    public static TileEntityType<TileSolarPanel> TILE_SOLAR_PANEL;
}
