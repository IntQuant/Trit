package intquant.trit.TESR;

import org.lwjgl.opengl.GL11;

import intquant.trit.blocks.tiles.TileFlowLinker;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRFlowLinker extends TileEntitySpecialRenderer<TileFlowLinker> {
    @Override
    public void render(TileFlowLinker te, double x, double y, double z, float partialTicks, int destroyStage,
            float alpha) {
        if (te == null) {
            return;
        }
        
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        //GlStateManager.enableBlend();
        //GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        

        double dx = 1;
        double dy = 0;
        double dz = 0;

        for (int side = 0; side < 3; side++) {

            int dist = te.getLinkedDistances()[side];
            float r = ((float) te.rates[side][2]);
            float g = ((float) te.rates[side][1]);
            float b = ((float) te.rates[side][0]);
            float amp = Math.max(r, Math.max(g, b));
            r /= amp;
            g /= amp;
            b /= amp;

            if (dist > 0 && Math.abs(amp)>1) {
            //if (dist > 0) {
                //dist = 1;
                renderBeam(dx*dist, dy*dist, dz*dist, r, g, b, amp);
            }
            
            dz = dy;
            dy = dx;
            dx = 0;
        }
        //setLightmapDisabled(false);

        GlStateManager.color(1, 1, 1, 1);
        //GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private void renderBeam(double dx, double dy, double dz, float r, float g, float b, float amp)  {
        //GlStateManager.disableLighting();
        GlStateManager.color(r, g, b);
        //GlStateManager.pushMatrix();
        
        //GlStateManager.rotate()

        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuffer();
        buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        buf.pos(0, 0, 0).endVertex();
        buf.pos(dx, dy, dz).endVertex();
        tes.draw();
        //GlStateManager.popMatrix();
        //GlStateManager.enableLighting();
        

    }

    @Override
    public boolean isGlobalRenderer(TileFlowLinker te) {
        return true;
    }
}