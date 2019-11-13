package sql;

import java.util.Objects;

public class HobbyResponse {
    private String hobby;
    private String ratio;

    public HobbyResponse(String hobby, String ratio) {
        this.hobby = hobby;
        this.ratio = ratio;
    }

    public String getHobby() {
        return hobby;
    }

    public String getRatio() {
        return ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HobbyResponse that = (HobbyResponse) o;
        return Objects.equals(hobby, that.hobby) &&
                Objects.equals(ratio, that.ratio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hobby, ratio);
    }
}
