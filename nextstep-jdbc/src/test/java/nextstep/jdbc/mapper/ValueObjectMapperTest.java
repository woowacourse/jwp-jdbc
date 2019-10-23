package nextstep.jdbc.mapper;

import nextstep.jdbc.support.Name;
import nextstep.jdbc.support.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValueObjectMapperTest {
    final ValueObjectMapper valueObjectMapper = new ValueObjectMapper();

    @Test
    void isMapping_필드_1개_True() {
        assertThat(valueObjectMapper.isMapping(Name.class)).isTrue();
    }

    @Test
    void isMapping_필드_1개_아닌경우_False() {
        assertThat(valueObjectMapper.isMapping(User.class)).isFalse();
    }
}