package de.socialmod.addon.useraction;

import de.socialmod.addon.server.ServerHelper;
import net.labymod.user.User;
import net.labymod.user.util.UserActionEntry;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

public class GetSocialUserActionEntry extends UserActionEntry {

    public GetSocialUserActionEntry() {
        super("Social Media", EnumActionType.NONE, null, null);
    }

    @Override
    public void execute(User user, EntityPlayer entityPlayer, NetworkPlayerInfo networkPlayerInfo) {
        ServerHelper.displayGuiScreen(entityPlayer.getName());
    }
}