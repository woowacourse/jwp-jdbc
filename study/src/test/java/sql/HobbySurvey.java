package sql;

public class HobbySurvey {
    private String hobby;
    private float ratio;

    public HobbySurvey(String hobby, float ratio) {
        this.hobby = hobby;
        this.ratio = ratio;
    }

    public String getHobby() {
        return hobby;
    }

    public float getRatio() {
        return ratio;
    }
}
