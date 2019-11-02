package sql;

public class HobbyDto {
    private String hobby;
    private double ratio;

    public HobbyDto(String hobby, String ratio) {
        this.hobby = hobby;
        this.ratio = Double.parseDouble(ratio);
    }

    public String getHobby() {
        return hobby;
    }

    public double getRatio() {
        return ratio;
    }
}
