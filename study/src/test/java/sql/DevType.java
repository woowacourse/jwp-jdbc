package sql;

import java.util.Objects;

public class DevType {
    private final String devType;
    private final double yearsCodingProf;

    public DevType(String devType, double yearsCodingProf) {
        this.devType = devType;
        this.yearsCodingProf = yearsCodingProf;
    }

    public String getDevType() {
        return devType;
    }

    public double getYearsCodingProf() {
        return yearsCodingProf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevType devType1 = (DevType) o;
        return Double.compare(devType1.yearsCodingProf, yearsCodingProf) == 0 &&
                Objects.equals(devType, devType1.devType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(devType, yearsCodingProf);
    }
}
