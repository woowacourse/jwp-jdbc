package sql;

import java.math.BigDecimal;

public class ResponseAndRatioDto {
    private String response;
    private BigDecimal ratio;

    public ResponseAndRatioDto() {
    }

    public ResponseAndRatioDto(String response, BigDecimal ratio) {
        this.response = response;
        this.ratio = ratio;
    }

    public String getResponse() {
        return response;
    }

    public BigDecimal getRatio() {
        return ratio;
    }
}
