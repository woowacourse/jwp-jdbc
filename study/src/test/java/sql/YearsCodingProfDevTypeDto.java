package sql;

import java.util.Objects;

public class YearsCodingProfDevTypeDto {
    private String devType;
    private Double average;

    public YearsCodingProfDevTypeDto(String devType, Double average) {
        this.devType = devType;
        this.average = average;
    }

    public String getDevType() {
        return devType;
    }

    public Double getAverage() {
        return average;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearsCodingProfDevTypeDto that = (YearsCodingProfDevTypeDto) o;
        return Objects.equals(devType, that.devType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(devType);
    }
}
