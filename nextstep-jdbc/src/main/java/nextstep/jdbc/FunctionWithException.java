package nextstep.jdbc;

@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {
    R apply(final T t) throws E;
}
