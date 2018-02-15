package intquant.trit.TESR;

import intquant.trit.blocks.tiles.TileFlowNetworkController;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRFlowNetworkController extends TileEntitySpecialRenderer<TileFlowNetworkController> {
	@Override
    public void render(TileFlowNetworkController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		//CommonProxy.logger.info("Rendering TESR at {} {} {}", x, y, z);
		/*
        GL11.glPushMatrix();
        GL11.glScaled(1, 1, 1);
        GL11.glTranslated(x, y, z);
        GL11.glTranslated(0.5, 1.5, 0.5);
        GL11.glScaled(0.5, 0.5, 0.5);
        
        //GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GlStateManager.color(1, 1, 1, 1);
        
        BufferBuilder wr = Tessellator.getInstance().getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.pos(-0.5, -0.5, -0.5).endVertex();
        wr.pos(+0.5, -0.5, -0.5).endVertex();
        wr.pos(+0.5, -0.5, +0.5).endVertex();
        wr.pos(+0.5, -0.5, +0.5).endVertex();
        
        wr.pos(+0.5, +0.5, +0.5).endVertex();
        wr.pos(-0.5, +0.5, -0.5).endVertex();
        wr.pos(+0.5, +0.5, -0.5).endVertex();
        wr.pos(+0.5, +0.5, +0.5).endVertex();
        
         
        
        Tessellator.getInstance().draw();
		
        
		//GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glPopMatrix();
        */
	}
	
}
