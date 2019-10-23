package nextstep.jdbc.support;

public class UserUpdateDto {
    private Name name;
    private int age;

    public UserUpdateDto() {
    }

    public UserUpdateDto(final Name name, final int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name.getName();
    }

    public int getAge() {
        return age;
    }
}
