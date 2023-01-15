package one.stoorx.listenerAgent.loadingTool;

import lombok.NonNull;

import java.util.function.Supplier;

public class Util {
    public static <T> T getOrElse(@NonNull T[] array, int index, Supplier<T> elseFn) {
        if (index < array.length) {
            return array[index];
        } else return elseFn.get();
    }

    public static <T> T getOrNull(@NonNull T[] array, int index) {
        if (index < array.length) {
            return array[index];
        } else return null;
    }
}
