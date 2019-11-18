package sql;

public class ProfessionalCodingExperienceStatistics {
    private String devtype;
    private Double average;

    public String getDevtype() {
        return devtype;
    }

    public Double getAverage() {
        return average;
    }

    @Override
    public String toString() {
        return "ProfessionalCodingExperienceStatistics{" +
                "devtype='" + devtype + '\'' +
                ", average=" + average +
                '}';
    }
}