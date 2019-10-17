package nextstep.jdbc;

public class DBConnection {
    private String driver;
    private String url;
    private String userName;
    private String password;

    public DBConnection(String driver, String url, String userName, String password) {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public static DBConnection getInstance() {
        return new DBConnection("", "", "", "");
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
