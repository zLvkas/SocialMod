package de.socialmod.addon.useraction;

import de.socialmod.addon.utils.ReflectionUtils;
import net.labymod.main.LabyMod;
import net.labymod.user.gui.UserActionGui;
import net.labymod.user.util.UserActionEntry;

import java.util.List;

public class UserActionEntryInvoker {

    public static void addUserActions() {
        final List<UserActionEntry> defaultEntries = ReflectionUtils.get(List.class, UserActionGui.class, LabyMod.getInstance().getUserManager().getUserActionGui(), "defaultEntries");
        if (defaultEntries != null) {
            defaultEntries.add(new GetSocialUserActionEntry());
        }
    }

}