package sql;

public class PortionResultDto {

    private final String value;
    private final double portion;

    public PortionResultDto(String value, double portion) {
        this.value = value;
        this.portion = portion;
    }

    public String getValue() {
        return value;
    }

    public double getPortion() {
        return portion;
    }
}
