package nextstep.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SingletonRegistry {
    private final Map<Class<?>, Object> instances;

    public SingletonRegistry(Object... instances) {
        this.instances = new HashMap<>() {{
            Stream.of(instances).forEach(x -> put(x.getClass(), x));
        }};
    }

    public SingletonRegistry register(Object instance) {
        this.instances.put(instance.getClass(), instance);
        return this;
    }

    public SingletonRegistry register(Class<?> clazz, Object instance) {
        Class parent = instance.getClass();
        do {
            if (parent == clazz) {
                this.instances.put(clazz, instance);
                break;
            }
            parent = parent.getSuperclass();
        }
        while (parent != Object.class);
        return this;
    }

    public Object getInstance(Class<?> type) {
        return this.instances.get(type);
    }
}