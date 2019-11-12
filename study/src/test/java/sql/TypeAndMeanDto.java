package sql;

import java.math.BigDecimal;

public class TypeAndMeanDto {
    private String type;
    private BigDecimal mean;

    public TypeAndMeanDto() {
    }

    public TypeAndMeanDto(String type, BigDecimal mean) {
        this.type = type;
        this.mean = mean;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getMean() {
        return mean;
    }
}
