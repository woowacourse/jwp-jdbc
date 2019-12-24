package sql;

public class RespondentHobbyDto {
    private final long respondentId;
    private final long hobbyId;

    public RespondentHobbyDto(long respondentId, long hobbyId) {
        this.respondentId = respondentId;
        this.hobbyId = hobbyId;
    }

    public long getRespondentId() {
        return respondentId;
    }

    public long getHobbyId() {
        return hobbyId;
    }
}
