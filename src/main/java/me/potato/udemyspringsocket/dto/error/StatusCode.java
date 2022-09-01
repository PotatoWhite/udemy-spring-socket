package me.potato.udemyspringsocket.dto.error;

public enum StatusCode {
    EC001("given number is not within range"),
    EC002("your usage limit exceeded");

    private final String description;

    StatusCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // search string in enum name
    public static StatusCode fromString(String name) {
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.name().equals(name)) {
                return statusCode;
            }
        }
        return null;
    }

}
