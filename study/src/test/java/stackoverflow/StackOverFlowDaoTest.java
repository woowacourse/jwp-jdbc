package stackoverflow;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StackOverFlowDaoTest {
    private StackOverFlowDao stackOverFlowDao = new StackOverFlowDao();

    @Test
    void Coding_As_A_Hobby() {
        Request1 request1 = stackOverFlowDao.request1();

        assertThat(request1.getYes()).isEqualTo("80.8%");
        assertThat(request1.getNo()).isEqualTo("19.2%");
    }

    @Test
    void Years_Of_Professional_Coding_Experience_by_Developer_Type() {
        Map<String, String> request2 = stackOverFlowDao.request2();

        assertThat(request2.get("Back-end developer")).isEqualTo("6.2");
        assertThat(request2.get("Engineering manager")).isEqualTo("10.2");
        assertThat(request2.get("Game or graphics developer")).isEqualTo("4.6");
    }

    @Test
    void Years_Of_Professional_Coding_Experience_by_Developer_Type_야매_Ver() {
        Map<String, String> request2 = stackOverFlowDao.request2_Illegal();

        assertThat(request2.get("Back-end developer")).isEqualTo("6.2");
        assertThat(request2.get("Engineering manager")).isEqualTo("10.2");
        assertThat(request2.get("Game or graphics developer")).isEqualTo("4.6");
    }
}