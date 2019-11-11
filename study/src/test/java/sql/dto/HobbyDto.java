package sql.dto;

public class HobbyDto {
    private final String hobby;
    private final String percentage;

    public HobbyDto(String hobby, String percentage) {
        this.hobby = hobby;
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return hobby + ": " + percentage;
    }
}
