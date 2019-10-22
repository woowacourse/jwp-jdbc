package nextstep.jdbc.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ListMapperTest {
    @Test
    @DisplayName("결과가 List로 매핑되는지 확인")
    void mappedTest() throws Exception {
        RowMapper<Car> rowMapper = rs -> new Car(rs.getString("name"));

        ResultSet rs = mock(ResultSet.class);
        rs.moveToInsertRow();
        rs.updateString ("name", "Benz");
        rs.insertRow();
        rs.close();

        ListMapper<Car> listMapper = new ListMapper<>(rowMapper);
        assertThat(listMapper.mapped(rs)).isInstanceOf(ArrayList.class);
    }
}