package slipp.domain;

import slipp.dto.UserUpdatedDto;

import java.util.Objects;

public class TestEntity {
    private String userId;
    private int age;
    private long grade;

    public TestEntity() {
    }

    public TestEntity(String userId, int age, long grade) {
        this.userId = userId;
        this.age = age;
        this.grade = grade;
    }

    public void update(int updateAage, long updateGrade) {
        this.age = updateAage;
        this.grade = updateGrade;
    }

    public String getUserId() {
        return userId;
    }

    public int getAge() {
        return age;
    }

    public long getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity that = (TestEntity) o;
        return age == that.age &&
                grade == that.grade &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, age, grade);
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "userId='" + userId + '\'' +
                ", age=" + age +
                ", grade=" + grade +
                '}';
    }
}
