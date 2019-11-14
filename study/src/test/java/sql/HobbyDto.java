package sql;

public class HobbyDto {
    private String yesOrNo;
    private double ratio;

    public HobbyDto(String yesOrNo, double ratio) {
        this.yesOrNo = yesOrNo;
        this.ratio = ratio;
    }

    public String getYesOrNo() {
        return yesOrNo;
    }

    public double getRatio() {
        return ratio;
    }

}
