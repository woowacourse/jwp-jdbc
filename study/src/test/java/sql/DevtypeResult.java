package sql;

public class DevtypeResult {
    private String devType;
    private String years;

    public DevtypeResult(String devType, String years) {
        this.devType = devType;
        this.years = years;
    }

    public String getDevType() {
        return devType;
    }

    public String getYears() {
        return years;
    }
}
