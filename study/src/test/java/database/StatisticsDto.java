package database;

public class StatisticsDto {
    private String devType;
    private Double average;

    public StatisticsDto(String devType, Double average) {
        this.devType = devType;
        this.average = average;
    }

    public String getDevType() {
        return devType;
    }

    public Double getAverage() {
        return average;
    }
}
