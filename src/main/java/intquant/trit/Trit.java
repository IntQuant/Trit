package intquant.trit;

import intquant.trit.misc.TritCreativeTab;
import intquant.trit.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Trit.MODID, name = Trit.NAME, version = Trit.VERSION, dependencies = "required-after:forge@[11.16.0.1865,)")
public class Trit {
	public static final String MODID   = "trit";
	public static final String NAME    = "Trit";
	public static final String VERSION = "0.0.1";
	
	public static final CreativeTabs TCT = new TritCreativeTab();
	
	@SidedProxy(clientSide = "intquant.trit.proxy.ClientProxy", serverSide = "intquant.trit.proxy.ServerProxy")
    public static CommonProxy proxy;
	
	@Mod.Instance
    public static Trit instance;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		MinecraftForge.EVENT_BUS.register(new ConfigHandler());
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }

}
