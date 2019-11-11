package sql;

public class CodingExperience {
    private final String devType;
    private final float averageCodingExperience;

    public CodingExperience(String devType, float averageCodingExperience) {
        this.devType = devType;
        this.averageCodingExperience = averageCodingExperience;
    }

    public String getDevType() {
        return devType;
    }

    public float getAverageCodingExperience() {
        return averageCodingExperience;
    }
}
