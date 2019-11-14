package sql;

public class DevTypeDto {
    private String devType;
    private double averageOfYears;

    public DevTypeDto(String devType, double averageOfYears) {
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
