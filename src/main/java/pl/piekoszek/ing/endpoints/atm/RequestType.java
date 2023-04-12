package pl.piekoszek.ing.endpoints.atm;

public enum RequestType {
    STANDARD("STANDARD"),

    PRIORITY("PRIORITY"),

    SIGNAL_LOW("SIGNAL_LOW"),

    FAILURE_RESTART("FAILURE_RESTART");

    RequestType(String value) {
    }
}