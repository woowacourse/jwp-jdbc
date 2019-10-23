package nextstep.jdbc.mapper;

import nextstep.jdbc.support.Name;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultSetMapperTest {
    private ResultSetMapper resultSetMapper = ResultSetMapper.getInstance();

    @Test
    void VO_매핑_테스트() throws SQLException {
        // given
        final ResultSet rs = mock(ResultSet.class);
        final String expected = "자바";
        when(rs.getString("name")).thenReturn(expected);

        // when
        final Name name = resultSetMapper.map(rs, "name", Name.class);
        final String actual = name.getName();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void Primitive_매핑_테스트() throws SQLException {
        // given
        final ResultSet rs = mock(ResultSet.class);
        final String expected = "자바";
        when(rs.getString("name")).thenReturn(expected);

        // when
        final String actual = resultSetMapper.map(rs, "name", String.class);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}