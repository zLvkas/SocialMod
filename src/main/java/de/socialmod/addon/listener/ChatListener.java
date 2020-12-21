package de.socialmod.addon.listener;

import de.socialmod.addon.server.ServerHelper;
import de.socialmod.addon.utils.Constants;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.main.LabyMod;

public class ChatListener implements MessageSendEvent {

    @Override
    public boolean onSend(String message) {
        if (!message.toLowerCase().startsWith("/socialmedias")) {
            return false;
        }

        final String[] strings = message.split(" ");
        if (strings.length != 2) {
            LabyMod.getInstance().displayMessageInChat(Constants.PREFIX + "§7Verwendung §8» §e/SocialMedias §8<§eName§8>");
            return true;
        }

        ServerHelper.displayGuiScreen(strings[1]);
        return true;
    }
}