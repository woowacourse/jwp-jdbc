package sql;

public class HobbyStatistic {
    private final String yesOrNo;
    private final float percentage;

    public HobbyStatistic(String yesOrNo, float percentage) {
        this.yesOrNo = yesOrNo;
        this.percentage = percentage;
    }

    public String getYesOrNo() {
        return yesOrNo;
    }

    public float getPercentage() {
        return percentage;
    }
}
