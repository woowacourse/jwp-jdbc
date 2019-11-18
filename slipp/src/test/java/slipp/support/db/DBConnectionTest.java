package slipp.support.db;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DBConnectionTest {
    private DBConnection dbConnection = new DBConnection();

    @Test
    void getDbDriver() {
        assertThat(dbConnection.getDbDriver()).isEqualTo("org.h2.Driver");
    }

    @Test
    void getDbUrl() {
        assertThat(dbConnection.getDbUrl()).isEqualTo("jdbc:h2:mem:jwp-framework");

    }

    @Test
    void getDbUsername() {
        assertThat(dbConnection.getDbUsername()).isEqualTo("sa");

    }

    @Test
    void getDbPw() {
        assertThat(dbConnection.getDbPw()).isEqualTo("");

    }
}