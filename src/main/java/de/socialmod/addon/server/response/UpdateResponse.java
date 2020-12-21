package de.socialmod.addon.server.response;

import de.socialmod.addon.utils.Constants;
import net.labymod.main.LabyMod;

public enum UpdateResponse {

    SUCCESS(
            Constants.PREFIX + "§7Die §eSocialMedias §7für §6%player% §7wurden §aerfolgreich gespeichert§7!",
            200
    ),

    FORBIDDEN(
            Constants.PREFIX + "§7Deine §eSocialMedias §7konnten §cnicht §7geupdated werden, da du dauerhaft §cgesperrt§7 wurdest!",
            401
    ),

    TOO_MANY_REQUESTS(
            Constants.PREFIX +"§7Du kannst deine §eSocialMedias §7nur alle §610 Sekunden §7updaten!",
            429
    ),

    NAME_TOO_LONG(
            Constants.PREFIX +"§7Dein %media% Name §7ist §czu lang§7! §8(§7maximal 32 Zeichen§8)",
            418
    ),

    UNKNOWN_ERROR(
            Constants.PREFIX +"§7Ein §cunbekannter Fehler §7ist aufgetreten!",
            -1
    ),

    INTERNAL_SERVER_ERROR(
            Constants.PREFIX +"§7Derzeit ist unser Server leider §cnicht §7erreichbar. Versuche es bitte später erneut!",
            500
    ),

    AUTH_FAILED(
            Constants.PREFIX +"§7Authentifizierung §cfehlgeschlagen§7! Bitte starte dein Spiel neu.",
            403
    ),

    BAD_REQUEST(
            Constants.PREFIX +"§7Deine Anfrage konnte §cnicht §7richtig verarbeitet werden!",
            400
    );

    private final String message;
    private final int code;

    UpdateResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message.replace("%player%", LabyMod.getInstance().getPlayerName());
    }

    public int getCode() {
        return this.code;
    }
}