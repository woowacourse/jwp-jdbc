package sql;

public class CodingAsHobbyDto {
    private String answer;
    private Double proportion;

    public CodingAsHobbyDto(String answer, Double proportion) {
        this.answer = answer;
        this.proportion = proportion;
    }

    public String getAnswer() {
        return answer;
    }

    public Double getProportion() {
        return proportion;
    }
}
