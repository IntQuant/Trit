package intquant.trit.misc;

import intquant.trit.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterTrit extends Teleporter {

	public TeleporterTrit(WorldServer worldIn, double x, double y, double z) {
		super(worldIn);
        this.world = worldIn;
        this.x = x;
        this.y = y;
        this.z = z;
		// TODO Auto-generated constructor stub
	}
	
	private WorldServer world;
	double x, y, z;
	
	@Override
	public void placeInPortal(Entity entity, float rotationYaw) {
        entity.setPosition(this.x, this.y, this.z);
        entity.motionX = 0.0f;
        entity.motionY = 0.0f;
        entity.motionZ = 0.0f;
	}

    public static void teleportToDimension(EntityPlayer player, int dimension, double x, double y, double z) {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
        
        player.addExperienceLevel(0);
        
        MinecraftServer server = player.getEntityWorld().getMinecraftServer();
        WorldServer worldServer = server.getWorld(dimension);
        
        

        if (worldServer == null || worldServer.getMinecraftServer() == null) {
            CommonProxy.logger.info("Target dimension not found");
        	return;
        }

        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(entityPlayerMP, dimension, new TeleporterTrit(worldServer, x, y, z));
        player.setPositionAndUpdate(x, y, z);
        player.setPositionAndUpdate(x, y, z);
        
        worldServer.spawnEntity(player);
        worldServer.updateEntityWithOptionalForce(player, false);  
    }
}
