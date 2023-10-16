package kv.common;

public enum KVMessageEnum {
    Failed("Failed.");

    private final String message;

    KVMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}