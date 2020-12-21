package de.socialmod.addon.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.exceptions.AuthenticationException;
import de.socialmod.addon.gui.SocialMediaGui;
import de.socialmod.addon.server.response.UpdateResponse;
import de.socialmod.addon.server.response.UpdateResponseHelper;
import de.socialmod.addon.types.SocialMediaType;
import de.socialmod.addon.utils.Constants;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerHelper {

    private static final JsonParser JSON_PARSER = new JsonParser();

    public static void displayGuiScreen(final String playerName) {
        Constants.EXECUTOR.execute(() -> {
            final Map<SocialMediaType, String> mediaTypes = ServerHelper.getSocialMedias(playerName);
            Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new SocialMediaGui(playerName, mediaTypes)));
        });
    }

    public static Map<SocialMediaType, String> getSocialMedias(final String playerName) {
        final Map<SocialMediaType, String> socialMediaTypes = new HashMap<>();
        final JsonObject jsonObject = getJsonObject(playerName);
        if (jsonObject == null) {
            return socialMediaTypes;
        }

        for (SocialMediaType value : SocialMediaType.values()) {
            if (jsonObject.has(value.name().toLowerCase())) {
                socialMediaTypes.put(value, jsonObject.get(value.name().toLowerCase()).getAsString());
            }
        }

        return socialMediaTypes;
    }

    public static UpdateResponse updateSocialMedia(final Map<SocialMediaType, String> medias) {
        if (!authorize()) {
            return UpdateResponse.AUTH_FAILED;
        }

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://api.socialmod.de:8080/updateMedia").openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("playerName", LabyMod.getInstance().getPlayerName());
            urlConnection.setRequestProperty("uuid", LabyMod.getInstance().getPlayerUUID().toString());
            for (Map.Entry<SocialMediaType, String> entry : medias.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey().name().toLowerCase(), entry.getValue());
            }

            return UpdateResponseHelper.getResponseById(urlConnection.getResponseCode());
        } catch (IOException ex) {
            ex.printStackTrace();
            return UpdateResponse.INTERNAL_SERVER_ERROR;
        }
    }

    private static boolean authorize() {
        final Session session = Minecraft.getMinecraft().getSession();
        if (session == null) {
            return false;
        }

        try {
            Minecraft.getMinecraft().getSessionService().joinServer(session.getProfile(), session.getToken(), "06e3be5c89714baebf12c133c262e5d2");
            return true;
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static JsonObject getJsonObject(final String playerName) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://api.socialmod.de:8080/getMedias?name=" + playerName).openConnection();
            urlConnection.setReadTimeout(2500);
            urlConnection.setConnectTimeout(2500);
            if (urlConnection.getResponseCode() != 200) {
                return null;
            }

            final String content = new String(IOUtils.toByteArray(urlConnection.getInputStream()), StandardCharsets.UTF_8);
            return JSON_PARSER.parse(content).getAsJsonObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}