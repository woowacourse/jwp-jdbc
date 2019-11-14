package sql;

import java.util.Objects;

public class Hobby {
    private String hobby;
    private Double percentage;

    public Hobby() {
    }

    public Hobby(final String hobby, final Double percentage) {
        this.hobby = hobby;
        this.percentage = percentage;
    }

    public String getHobby() {
        return hobby;
    }

    public Double getPercentage() {
        return percentage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Hobby hobby1 = (Hobby) o;
        return Objects.equals(hobby, hobby1.hobby) &&
                Objects.equals(percentage, hobby1.percentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hobby, percentage);
    }

    @Override
    public String toString() {
        return "Hobby{" +
                "hobby='" + hobby + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}
