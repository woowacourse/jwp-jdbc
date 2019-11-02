package sql;

public class YpceOfDevTypeDto {
    private String devType;
    private double averageOfYears;

    public YpceOfDevTypeDto(String devType, double averageOfYears) {
        this.devType = devType;
        this.averageOfYears = averageOfYears;
    }

    public String getDevType() {
        return devType;
    }

    public double getAverageOfYears() {
        return averageOfYears;
    }
}
