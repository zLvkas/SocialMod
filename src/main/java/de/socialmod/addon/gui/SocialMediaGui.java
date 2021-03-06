package de.socialmod.addon.gui;

import de.socialmod.addon.types.SocialMediaType;
import de.socialmod.addon.utils.Constants;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Map;

public class SocialMediaGui extends GuiScreen {

    private int buttonWidth = 200;

    private static final int IMAGE_WIDTH = 16;

    private static final int EDGE_WITH = 20;

    private static final int SPACE_WIDTH = 5;

    private static final int CLOSE_GUI_BUTTON_ID = 0;

    private static final int NO_MEDIAS_AVAILABLE_BUTTON_ID = 1;

    private final Map<SocialMediaType, String> mediaTypes;

    private final String playerName;

    public SocialMediaGui(final String playerName, Map<SocialMediaType, String> mediaTypes) {
        this.mediaTypes = mediaTypes;
        this.playerName = playerName;
    }

    @Override
    public void initGui() {
        this.buttonWidth = 200;
        this.buttonList.clear();

        for (Map.Entry<SocialMediaType, String> entry : this.mediaTypes.entrySet()) {
            int width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.getButtonText(entry.getKey(), entry.getValue()));
            if (width > buttonWidth) {
                buttonWidth = width + IMAGE_WIDTH + EDGE_WITH + SPACE_WIDTH;
            }
        }

        int center = (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2) - buttonWidth / 2;

        this.buttonList.add(new GuiButton(CLOSE_GUI_BUTTON_ID, center, 205, buttonWidth, 20, "Zurück"));

        this.initButtons(center);
    }

    private void initButtons(int center) {
        if (mediaTypes.isEmpty()) {
            this.buttonList.add(
                    new GuiButton(
                            NO_MEDIAS_AVAILABLE_BUTTON_ID,
                            center,
                            LabyMod.getInstance().getDrawUtils().getScaledResolution().getScaledHeight() / 2 - 10,
                            buttonWidth,
                            20,
                            "§ckeine Medias"
                    )
            );
            return;
        }

        int y = 40;
        for (Map.Entry<SocialMediaType, String> entry : this.mediaTypes.entrySet()) {
            this.buttonList.add(
                    new GuiButton(
                            entry.getKey().getId(),
                            center,
                            y,
                            buttonWidth,
                            20,
                            this.getButtonText(entry.getKey(), entry.getValue())
                    )
            );

            y += 20;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        int centerX = LabyMod.getInstance().getDrawUtils().getScaledResolution().getScaledWidth() / 2;
        LabyMod.getInstance().getDrawUtils().drawCenteredString(this.playerName, centerX, 15);

        int buttonX = centerX - buttonWidth / 2;

        double y = 40 + 2.0625;
        for (SocialMediaType mediaType : this.mediaTypes.keySet()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(mediaType.getResourceLocation());
            LabyMod.getInstance().getDrawUtils().drawTexture(
                    buttonX + 2.5,
                    y,
                    256,
                    256,
                    16,
                    16,
                    1F
            );

            y += 20;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == CLOSE_GUI_BUTTON_ID) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            return;
        }

        if (button.id == NO_MEDIAS_AVAILABLE_BUTTON_ID) {
            LabyMod.getInstance().displayMessageInChat(Constants.PREFIX + "§7Der Spieler §6" + this.playerName + " §7hat §ckeine §eSocialMedias §7verlinkt!");
            Minecraft.getMinecraft().displayGuiScreen(null);
        }

        final SocialMediaType mediaType = this.getSocialMediaTypeById(button.id);
        if (mediaType == null) {
            return;
        }

        final String socialName = this.mediaTypes.get(mediaType);
        if (socialName == null) {
            return;
        }

        Minecraft.getMinecraft().displayGuiScreen(null);

        if (mediaType.shouldOpenWebpage()) {
            LabyMod.getInstance().openWebpage(mediaType.getUrl() + socialName, true);
            return;
        }

        this.setClipboardContent(socialName);
        LabyMod.getInstance().displayMessageInChat(Constants.PREFIX + "§7Der " + mediaType.getDisplayName() + " Name §7wurde §akopiert§7!");
    }

    private SocialMediaType getSocialMediaTypeById(int id) {
        for (SocialMediaType value : SocialMediaType.values()) {
            if (value.getId() == id) {
                return value;
            }
        }

        return null;
    }

    private String getButtonText(final SocialMediaType mediaType, final String text) {
        return mediaType.getDisplayName() + " §8× §f" + text;
    }

    private void setClipboardContent(final String name) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(name), null);
    }

}