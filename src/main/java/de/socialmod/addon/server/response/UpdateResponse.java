package de.socialmod.addon.server.response;

import de.socialmod.addon.SocialMod;

public enum UpdateResponse {

    SUCCESS(
            SocialMod.PREFIX + "§7Deine §eSocialMedias §7wurden §aerfolgreich gespeichert§7!",
            200
    ),

    NAME_TOO_LONG(
            SocialMod.PREFIX + "§7Dein %media% Name §7ist §czu lang§7! §8(§7maximal 30 Zeichen§8)",
            418
    ),

    UNKNOWN_ERROR(
            SocialMod.PREFIX + "§7Ein §cunbekannter Fehler §7ist aufgetreten!",
            -1
    ),

    INTERNAL_SERVER_ERROR(
            SocialMod.PREFIX + "§7Derzeit ist unser Server leider §cnicht §7erreichbar. Versuche es bitte später erneut!",
            500
    ),

    AUTH_FAILED(
            SocialMod.PREFIX + "§7Authentifizierung §cfehlgeschlagen§7! Bitte starte dein Spiel neu.",
            403
    ),

    BAD_REQUEST(
            SocialMod.PREFIX + "§7Deine Anfrage konnte §cnicht §7richtig verarbeitet werden!",
            400
    );

    private final String message;
    private final int code;

    UpdateResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }
}