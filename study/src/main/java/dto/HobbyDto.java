package dto;

public class HobbyDto {
    private final String hobby;
    private final double ratio;

    public HobbyDto(String hobby, double ratio) {
        this.hobby = hobby;
        this.ratio = ratio;
    }

    public String getHobby() {
        return hobby;
    }

    public double getRatio() {
        return ratio;
    }
}
