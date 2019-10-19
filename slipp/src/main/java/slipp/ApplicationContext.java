package slipp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ApplicationContext {
    private static Map<Class<?>, Object> beans = new HashMap<>();

    public static void register(Object object) {
        beans.put(object.getClass(), object);
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) Optional.ofNullable(beans.get(clazz))
                .orElseThrow(() -> new NotFoundBeanException(clazz.toString()));
    }
}
