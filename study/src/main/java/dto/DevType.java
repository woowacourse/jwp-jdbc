package dto;

public class DevType {
    private final String devType;
    private final double avg;

    public DevType(String devType, double avg) {
        this.devType = devType;
        this.avg = avg;
    }

    public String getDevType() {
        return devType;
    }

    public double getAvg() {
        return avg;
    }
}
