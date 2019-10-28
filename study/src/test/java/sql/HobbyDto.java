package sql;

public class HobbyDto {
    private String hobby;
    private String percent;

    public HobbyDto(String hobby, String percent) {
        this.hobby = hobby;
        this.percent = percent;
    }

    public String getHobby() {
        return hobby;
    }

    public String getPercent() {
        return percent;
    }

    @Override
    public String toString() {
        return "HobbyDto{" +
                "hobby='" + hobby + '\'' +
                ", percent='" + percent + '\'' +
                '}';
    }
}
