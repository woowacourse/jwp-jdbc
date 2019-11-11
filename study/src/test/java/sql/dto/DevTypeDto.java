package sql.dto;

public class DevTypeDto {
    private String devType;
    private String yearsOfProf;

    public DevTypeDto(String devType, String yearsOfProf) {
        this.devType = devType;
        this.yearsOfProf = yearsOfProf;
    }

    @Override
    public String toString() {
        return devType + ": " + yearsOfProf;
    }
}
