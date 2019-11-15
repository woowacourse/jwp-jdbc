package sql;

public class Hobby {
    private final String hobby;
    private final double respondents;

    public Hobby(String hobby, double respondents) {
        this.hobby = hobby;
        this.respondents = respondents;
    }

    public String getHobby() {
        return hobby;
    }

    public double getRespondents() {
        return respondents;
    }
}
