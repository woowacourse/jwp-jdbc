package sql;

public class Result {
    private final String hobby;
    private final double respondents;

    public Result(String hobby, double respondents) {
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
