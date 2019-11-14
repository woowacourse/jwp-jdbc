package sql;

import java.util.Objects;

public class YearsOfProfessionalCodingExperience {
    private final String devType;
    private final String yearsOfProf;

    public YearsOfProfessionalCodingExperience(final String devType, final String yearsOfProf) {
        this.devType = devType;
        this.yearsOfProf = yearsOfProf;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearsOfProfessionalCodingExperience that = (YearsOfProfessionalCodingExperience) o;
        return Objects.equals(devType, that.devType) && Objects.equals(yearsOfProf, that.yearsOfProf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(devType, yearsOfProf);
    }

    @Override
    public String toString() {
        return "YearsOfProfessionalCodingExperience{" + "devType='" + devType + '\'' + ", yearsOfProf='" + yearsOfProf + '\'' + '}';
    }
}
