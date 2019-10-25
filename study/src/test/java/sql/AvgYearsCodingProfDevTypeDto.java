package sql;

public class AvgYearsCodingProfDevTypeDto implements Comparable<AvgYearsCodingProfDevTypeDto> {
    private final String devType;
    private final double avgYears;

    public AvgYearsCodingProfDevTypeDto(String devType, double avgYears) {
        this.devType = devType;
        this.avgYears = avgYears;
    }

    public String getDevType() {
        return devType;
    }

    public double getAvgYears() {
        return avgYears;
    }

    @Override
    public int compareTo(AvgYearsCodingProfDevTypeDto o) {
        return devType.compareTo(o.devType);
    }
}
