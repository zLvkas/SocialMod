package de.socialmod.addon.server.response;

public class UpdateResponseHelper {

    public static UpdateResponse getResponseById(int code) {
        for (UpdateResponse value : UpdateResponse.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        return UpdateResponse.UNKNOWN_ERROR;
    }

}