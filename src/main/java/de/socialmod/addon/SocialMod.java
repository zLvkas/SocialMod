package de.socialmod.addon;

import de.socialmod.addon.listener.ChatListener;
import de.socialmod.addon.server.ServerHelper;
import de.socialmod.addon.server.response.UpdateResponse;
import de.socialmod.addon.settings.UpdateMediaSettingsElement;
import de.socialmod.addon.types.SocialMediaType;
import de.socialmod.addon.useraction.UserActionEntryInvoker;
import de.socialmod.addon.utils.Constants;
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

    public final Map<SocialMediaType, String> socialMedias = new HashMap<>();

    @Override
    public void onEnable() {
        UserActionEntryInvoker.addUserActions();

        LabyMod.getInstance().getEventManager().register(new ChatListener());
    }

    @Override
    public void loadConfig() {
        this.socialMedias.putAll(ServerHelper.getSocialMedias(LabyMod.getInstance().getPlayerName()));
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        final ResourceLocation refreshResource = new ResourceLocation("socialmod/textures/icons/refresh.png");
        final UpdateMediaSettingsElement updateMediaSettingsElement = new UpdateMediaSettingsElement("Änderungen speichern", new ControlElement.IconData(refreshResource), "Klick");
        updateMediaSettingsElement.setDescriptionText("Klicke, um deine Änderungen zu speichern.");
        list.add(updateMediaSettingsElement);
        updateMediaSettingsElement.setClickListener(() -> {
            Minecraft.getMinecraft().displayGuiScreen(null);

            for (Map.Entry<SocialMediaType, String> entry : this.socialMedias.entrySet()) {
                if (entry.getValue().length() > 32) {
                    LabyMod.getInstance().displayMessageInChat(UpdateResponse.NAME_TOO_LONG.getMessage()
                            .replace("%media%", entry.getKey().getDisplayName())
                    );
                    return;
                }
            }

            Constants.EXECUTOR.execute(() -> {
                final UpdateResponse updateResponse = ServerHelper.updateSocialMedia(this.socialMedias);
                LabyMod.getInstance().displayMessageInChat(updateResponse.getMessage());
            });
        });

        for (SocialMediaType mediaType : SocialMediaType.values()) {
            this.createOptions(mediaType);
        }
    }

    public void createOptions(final SocialMediaType mediaType) {
        final String currentValue = this.socialMedias.get(mediaType);
        final StringElement setting = new StringElement(
                mediaType.getDisplayName(),
                new ControlElement.IconData(mediaType.getResourceLocation()),
                currentValue == null ? "" : currentValue,
                name -> this.socialMedias.put(mediaType, name));

        super.getSubSettings().add(setting);
    }
}