package sql;

import java.util.Objects;

public class HobbyDto {
    private String yesOrNo;
    private double ratio;

    public HobbyDto(String yesOrNo, double ratio) {
        this.yesOrNo = yesOrNo;
        this.ratio = ratio;
    }

    public String getYesOrNo() {
        return yesOrNo;
    }

    public double getRatio() {
        return ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HobbyDto hobbyDto = (HobbyDto) o;
        return Double.compare(hobbyDto.ratio, ratio) == 0 &&
                Objects.equals(yesOrNo, hobbyDto.yesOrNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(yesOrNo, ratio);
    }
}
