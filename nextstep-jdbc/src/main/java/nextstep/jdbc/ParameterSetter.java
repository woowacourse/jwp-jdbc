package nextstep.jdbc;

import java.util.List;

public interface ParameterSetter {
    List set(Object... parameters);
}
