package sql;

import java.util.Objects;

public class ExperienceResponse {
    private String devType;
    private String age;

    public ExperienceResponse(String devType, String age) {
        this.devType = devType;
        this.age = age;
    }

    public String getDevType() {
        return devType;
    }

    public String getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExperienceResponse that = (ExperienceResponse) o;
        return Objects.equals(devType, that.devType) &&
                Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(devType, age);
    }
}
