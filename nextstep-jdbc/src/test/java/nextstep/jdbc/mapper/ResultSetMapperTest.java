package nextstep.jdbc.mapper;

import nextstep.jdbc.support.Name;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultSetMapperTest {

    @Test
    void VO_매핑_테스트() throws SQLException {
        // given
        final ResultSet rs = mock(ResultSet.class);
        final String expected = "자바";
        when(rs.getString("name")).thenReturn(expected);

        // when
        // todo 제너릭 캐스팅
        final Name name = (Name) ResultSetMapper.getInstance().map(rs, "name", Name.class);
        final String actual = name.getName();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}