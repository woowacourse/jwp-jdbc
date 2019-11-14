package sql;

public class CodingExperienceByDeveloperTypeDto {
    private String DeveloperType;
    private double YearsOfCodingExperience;

    public CodingExperienceByDeveloperTypeDto(String developerType, String yearsOfCodingExperience) {
        DeveloperType = developerType;
        YearsOfCodingExperience = Double.parseDouble(yearsOfCodingExperience);
    }

    public String getDeveloperType() {
        return DeveloperType;
    }

    public double getYearsOfCodingExperience() {
        return YearsOfCodingExperience;
    }

    @Override
    public String toString() {
        return "CodingExperienceByDeveloperTypeDto{" +
                "DeveloperType='" + DeveloperType + '\'' +
                ", YearsOfCodingExperience=" + YearsOfCodingExperience +
                '}';
    }
}
