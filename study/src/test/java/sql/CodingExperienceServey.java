package sql;

public class CodingExperienceServey {
    private final String job;
    private final float codingExperience;

    public CodingExperienceServey(String job, float codingExperience) {
        this.job = job;
        this.codingExperience = codingExperience;
    }

    public String getJob() {
        return job;
    }

    public float getCodingExperience() {
        return codingExperience;
    }
}
