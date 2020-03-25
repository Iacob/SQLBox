import java.util.function.BiConsumer;

public class TypedIterator {

    public static <T> void iterate(Object[] items, Class<T> clazz, BiConsumer<T, Integer> consumer) {
        if (items == null) {
            return;
        }

        int i = 0;
        for (Object item : items) {
            if (item != null) {
                if (clazz.isInstance(item)) {
                    consumer.accept(clazz.cast(item), i);
                    i++;
                }
            }
        }
    }
}
