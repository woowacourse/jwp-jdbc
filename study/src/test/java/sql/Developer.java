package sql;

public class Developer {
    private String type;
    private double years;

    public Developer(String type, double years) {
        this.type = type;
        this.years = years;
    }

    public String getType() {
        return type;
    }

    public double getYears() {
        return years;
    }
}
