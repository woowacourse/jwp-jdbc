package sql;

public class Hobby {
    private String status;
    private double ratio;

    public Hobby(String status, double ratio) {
        this.status = status;
        this.ratio = ratio;
    }

    public String getStatus() {
        return status;
    }

    public double getRatio() {
        return ratio;
    }
}
