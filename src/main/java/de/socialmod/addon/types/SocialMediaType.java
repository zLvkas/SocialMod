package de.socialmod.addon.types;

import net.minecraft.util.ResourceLocation;

public enum SocialMediaType {

    SNAPCHAT(
            "https://www.snapchat.com/add/",
            "§eSnapchat",
            true,
            2
    ),

    INSTAGRAM(
            "https://www.instagram.com/",
            "§dInstagram",
            true,
            3
    ),

    TIKTOK(
            "https://www.tiktok.com/@",
            "§3TikTok",
            true,
            7
    ),

    TWITTER(
            "https://twitter.com/",
            "§bTwitter",
            true,
            4
    ),

    TWITCH(
            "https://www.twitch.tv/",
            "§5Twitch",
            true,
            5
    ),

    YOUTUBE(
            "https://www.youtube.com/results?search_query=",
            "§cYouTube",
            true,
            6
    ),

    DISCORD(
            null,
            "§9Discord",
            true,
            8
    ),

    STEAM(
            null,
            "§1Steam",
            true,
            9
    );

    private final String url;
    private final String displayName;
    private final ResourceLocation resourceLocation;
    private final boolean openWebpage;
    private final int id;

    SocialMediaType(String url, String displayName, boolean openWebpage, int id) {
        this.url = url;
        this.displayName = displayName;
        this.openWebpage = openWebpage;
        this.id = id;

        this.resourceLocation = new ResourceLocation("socialmod/textures/logos/" + this.name().toLowerCase() + ".png");
    }

    public String getUrl() {
        return this.url;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean shouldOpenWebpage() {
        return this.openWebpage;
    }

    public int getId() {
        return this.id;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }
}