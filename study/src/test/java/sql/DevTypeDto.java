package sql;

public class DevTypeDto {
    private String devType;
    private String years;

    public DevTypeDto(String devType, String years) {
        this.devType = devType;
        this.years = years;
    }

    public String getDevType() {
        return devType;
    }

    public String getYears() {
        return years;
    }

    @Override
    public String toString() {
        return "DevTypeDto{" +
                "devType='" + devType + '\'' +
                ", years='" + years + '\'' +
                '}';
    }
}
