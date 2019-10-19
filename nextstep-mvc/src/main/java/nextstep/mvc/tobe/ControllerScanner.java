package nextstep.mvc.tobe;

import com.google.common.collect.Maps;
import nextstep.mvc.SingletonRegistry;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final SingletonRegistry singletonRegistry;
    private final Reflections reflections;

    public ControllerScanner(SingletonRegistry singletonRegistry, Object... basePackage) {
        this.singletonRegistry = singletonRegistry;
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> preInitiatedControllers = this.reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(preInitiatedControllers);
    }

    Map<Class<?>, Object> instantiateControllers(Set<Class<?>> preInitiatedControllers) {
        final Map<Class<?>, Object> controllers = Maps.newHashMap();
        try {
            for (Class<?> clazz : preInitiatedControllers) {
                final Object[] args = Stream.of(
                        clazz.getDeclaredConstructors()[0].getParameterTypes()
                ).map(this.singletonRegistry::getInstance)
                .toArray();
                controllers.put(clazz, clazz.getDeclaredConstructors()[0].newInstance(args));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
        }
        return controllers;
    }
}