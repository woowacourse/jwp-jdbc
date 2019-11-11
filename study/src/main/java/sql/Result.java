package sql;

public class Result {
    private int respondent;
    private String hobby;
    private String openSource;
    private String country;

    public Result(int respondent, String hobby, String openSource, String country) {
        this.respondent = respondent;
        this.hobby = hobby;
        this.openSource = openSource;
        this.country = country;
    }

    public int getRespondent() {
        return respondent;
    }

    public String getHobby() {
        return hobby;
    }

    public String getOpenSource() {
        return openSource;
    }

    public String getCountry() {
        return country;
    }
}
