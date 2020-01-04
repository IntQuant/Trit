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

            if (dist > 0 && Math.abs(amp)>0) {
                renderBeam(dx*dist, dy*dist, dz*dist, r, g, b, amp);
            }
            
            dz = dy;
            dy = dx;
            dx = 0;
        }
        

        GlStateManager.color(1, 1, 1, 1);
        
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private void renderBeam(double dx, double dy, double dz, float r, float g, float b, float amp)  {
        GlStateManager.pushMatrix();
        GlStateManager.color(r, g, b);
        
        double nx = 0;
        double ny = 0;
        double nz = 0;
        
        if (Math.abs(dx) > 0) ny = 1;
        if (Math.abs(dy) > 0) nz = 1;
        if (Math.abs(dz) > 0) nx = 1;

        double mx = Math.max(nx, Math.max(ny, nz));
        
        nx /= mx;
        ny /= mx;
        nz /= mx;

        double mult = Math.min(0.1, 0.001 * amp);

        nx *= mult;
        ny *= mult;
        nz *= mult;

        long time = (System.currentTimeMillis()/50) % 360;

        GlStateManager.rotate((float)time, (float)dx, (float)dy, (float)dz);

        for (int i=0;i<3;i++) {
            GlStateManager.rotate(360/3, (float)dx, (float)dy, (float)dz);
            Tessellator tes = Tessellator.getInstance();
            BufferBuilder buf = tes.getBuffer();
            buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            buf.pos(0+nx, 0+ny, 0+nz).endVertex();
            buf.pos(dx+nx, dy+ny, dz+nz).endVertex();
            tes.draw();
        }
        GlStateManager.popMatrix();
        
        

    }

    @Override
    public boolean isGlobalRenderer(TileFlowLinker te) {
        return true;
    }
}