package sql;

import java.util.Objects;

public class YearsOfProfessional {
    private String devtype;
    private double average;

    public YearsOfProfessional() {
    }

    public YearsOfProfessional(final String devtype, final double average) {
        this.devtype = devtype;
        this.average = average;
    }

    public String getDevtype() {
        return devtype;
    }

    public void setDevtype(final String devtype) {
        this.devtype = devtype;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(final double average) {
        this.average = average;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final YearsOfProfessional that = (YearsOfProfessional) o;
        return Double.compare(that.average, average) == 0 &&
                Objects.equals(devtype, that.devtype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(devtype, average);
    }

    @Override
    public String toString() {
        return "YearsOfProfessional{" +
                "devtype='" + devtype + '\'' +
                ", average=" + average +
                '}';
    }
}
