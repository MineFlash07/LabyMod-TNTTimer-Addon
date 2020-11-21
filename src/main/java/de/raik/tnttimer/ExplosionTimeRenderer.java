package de.raik.tnttimer;

import net.labymod.api.events.RenderEntityEvent;
import net.labymod.core.LabyModCore;
import net.labymod.core.WorldRendererAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Class including listener
 * to render the nametag
 *
 * @author Raik
 * @version 1.0
 */
public class ExplosionTimeRenderer implements RenderEntityEvent {

    /**
     * Addon instance for accessing settings
     */
    private final TNTTimerAddon addon;

    /**
     * The render manager of minecraft
     */
    private final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

    /**
     * Decimal format for the tag
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    /**
     * Event method called on render
     *
     * @param entity The entity that will be rendered
     * @param x X
     * @param y Y
     * @param z Z
     * @param partialTicks partialTicks
     */
    @Override
    public void onRender(Entity entity, double x, double y, double z, float partialTicks) {
        //Check enabled
        if (!this.addon.isEnabled())
            return;

        //Cancel on wrong entity
        if (!(entity instanceof EntityTNTPrimed))
            return;

        EntityTNTPrimed tntEntity = (EntityTNTPrimed) entity;

        //Check distance
        if (tntEntity.getDistanceSqToEntity(this.renderManager.livingPlayer) > (double) (64 * 64))
            return;

        //Render
        this.render(tntEntity, x, y, z, this.getTagString(tntEntity, partialTicks), this.getColor(tntEntity));
    }

    /**
     * Method calculating fuse to remaining seconds
     * Returns count as string in decimal format
     *
     * @param tntEntity The tnt entity
     * @param partialTicks partialTicks
     * @return The tag
     */
    private String getTagString(EntityTNTPrimed tntEntity, float partialTicks) {
        float number = (tntEntity.fuse - partialTicks) / 20F;
        return this.decimalFormat.format(number);
    }

    /**
     * Gets the color of tag
     * dynamic if enabled
     *
     * @param tntEntity The tnt entity to get the fuse
     * @return The color
     */
    private Color getColor(EntityTNTPrimed tntEntity) {
        if (!this.addon.isColored())
            return Color.WHITE;

        /*
         * Snippet from Sk1ers Code
         */
        float green = Math.min(tntEntity.fuse / 80F, 1F);
        return new Color(1F- green, green, 0F);
    }

    /**
     * Rendering the tag above the head
     *
     * @param tntEntity The entity of the tag
     * @param x x
     * @param y y
     * @param z z
     * @param tagString The string displayed as the tag
     * @param tagColor The color of the tag
     */
    private void render(EntityTNTPrimed tntEntity, double x, double y, double z, String tagString , Color tagColor) {
        /*
         * Code snippet to render tags by LabyStudio
         */
        float fixedPlayerViewX = renderManager.playerViewX * (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1 : 1);
        FontRenderer fontRenderer = renderManager.getFontRenderer();
        GlStateManager.pushMatrix();

        GlStateManager.translate((float) x, (float) y + tntEntity.height + 0.5F, (float) z);
        GL11.glNormal3f(0F, 1F, 0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0F, 1F, 0F);
        GlStateManager.rotate(fixedPlayerViewX, 1F, 0F, 0F);
        float scale = 0.016666668F * 1.6F;
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        //Background
        WorldRendererAdapter worldRenderer = LabyModCore.getWorldRenderer();
        int posX = fontRenderer.getStringWidth(tagString) / 2;
        GlStateManager.disableTexture2D();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(-posX - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldRenderer.pos(-posX - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldRenderer.pos(posX + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldRenderer.pos(posX + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        Tessellator.getInstance().draw();
        //Text
        GlStateManager.enableTexture2D();
        fontRenderer.drawString(tagString, -fontRenderer.getStringWidth(tagString) / 2, 0, tagColor.getRGB());
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontRenderer.drawString(tagString, -fontRenderer.getStringWidth(tagString) / 2, 0, -1);
        //Reset
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    /**
     * Constructor to set
     * the addon instance
     *
     * @param addon The addon
     */
    public ExplosionTimeRenderer(TNTTimerAddon addon) {
        this.addon = addon;
    }
}
