package sql;

public class CodingAsAHobbyDto {
    private String hobby;
    private Double average;

    public CodingAsAHobbyDto(String hobby, Double average) {
        this.hobby = hobby;
        this.average = average;
    }

    public String getHobby() {
        return hobby;
    }

    public Double getAverage() {
        return average;
    }
}
