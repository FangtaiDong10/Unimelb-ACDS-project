package kv.common;

public class KVException extends RuntimeException {
    public static void display() {
        throw new KVException(KVMessageEnum.Failed);
    }

    public static void display(KVMessageEnum KVMessageEnum) {
        throw new KVException(KVMessageEnum);
    }

    public KVException(KVMessageEnum KVMessageEnum) {
        super(KVMessageEnum.getMessage());
    }
}
