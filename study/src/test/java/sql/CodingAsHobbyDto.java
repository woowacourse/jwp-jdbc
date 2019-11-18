package sql;

public class CodingAsHobbyDto {
    private String hobby;
    private double percent;

    public CodingAsHobbyDto(String hobby, String percent) {
        this.hobby = hobby;
        this.percent = Double.parseDouble(percent);
    }

    public String getHobby() {
        return hobby;
    }

    public double getPercent() {
        return percent;
    }

    @Override
    public String toString() {
        return "CodingAsHobbyDto{" +
                "hobby='" + hobby + '\'' +
                ", percent=" + percent +
                '}';
    }
}
