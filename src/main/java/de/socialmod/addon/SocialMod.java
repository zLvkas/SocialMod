package de.socialmod.addon;

import de.socialmod.addon.server.ServerHelper;
import de.socialmod.addon.server.response.UpdateResponse;
import de.socialmod.addon.settings.UpdateMediaSettingsElement;
import de.socialmod.addon.types.SocialMediaType;
import de.socialmod.addon.useraction.UserActionEntryInvoker;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialMod extends LabyModAddon {

    public static final String PREFIX = "§8┃ §6Social§fMod §8× ";

    private final Map<SocialMediaType, String> socialMedias = new HashMap<>();

    private final ServerHelper serverHelper = new ServerHelper();

    @Override
    public void onEnable() {
        UserActionEntryInvoker.addUserActions();
    }

    @Override
    public void loadConfig() {
        for (SocialMediaType mediaType : SocialMediaType.values()) {
            if (!super.getConfig().has(mediaType.name().toLowerCase())) {
                super.getConfig().addProperty(mediaType.name().toLowerCase(), "");
            }

            this.socialMedias.put(mediaType, super.getConfig().get(mediaType.name().toLowerCase()).getAsString());
        }
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        final ResourceLocation refreshResource = new ResourceLocation("socialmod/textures/icons/refresh.png");
        final UpdateMediaSettingsElement updateMediaSettingsElement = new UpdateMediaSettingsElement("Änderungen speichern", new ControlElement.IconData(refreshResource), "Klick");
        updateMediaSettingsElement.setDescriptionText("Klicke, um deine Änderungen zu speichern.");
        list.add(updateMediaSettingsElement);
        updateMediaSettingsElement.setClickListener(() -> {
            for (Map.Entry<SocialMediaType, String> entry : this.socialMedias.entrySet()) {
                if (entry.getValue().length() > 32) {
                    Minecraft.getMinecraft().displayGuiScreen(null);
                    LabyMod.getInstance().displayMessageInChat(UpdateResponse.NAME_TOO_LONG.getMessage().replace("%media%", entry.getKey().getDisplayName()));
                    return;
                }

                super.getConfig().addProperty(entry.getKey().name().toLowerCase(), entry.getValue());
            }

            Minecraft.getMinecraft().displayGuiScreen(null);
            UpdateResponse updateResponse = this.serverHelper.updateSocialMedia(this.socialMedias);
            LabyMod.getInstance().displayMessageInChat(updateResponse.getMessage());

            super.saveConfig();
        });

        for (SocialMediaType mediaType : SocialMediaType.values()) {
            this.createOption(mediaType);
        }
    }

    private void createOption(final SocialMediaType mediaType) {
        final StringElement setting = new StringElement(
                mediaType.getDisplayName(),
                new ControlElement.IconData(mediaType.getResourceLocation()),
                super.getConfig().get(mediaType.name().toLowerCase()).getAsString(),
                name -> this.socialMedias.put(mediaType, name));

        super.getSubSettings().add(setting);
    }

}