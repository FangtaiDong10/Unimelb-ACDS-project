package kv.common;

public class KVJsonResult {
    private final Boolean success;
    private final Object data;
    private final String message;

    public KVJsonResult(Boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static KVJsonResult ok() {
        return new KVJsonResult(true, null, null);
    }

    public static KVJsonResult ok(Object data) {
        return new KVJsonResult(true, data, null);
    }

    public static KVJsonResult error() {
        return new KVJsonResult(false, null, null);
    }

    public static KVJsonResult error(String message) {
        return new KVJsonResult(false, null, message);
    }

    public Boolean getSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
