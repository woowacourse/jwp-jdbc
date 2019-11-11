package sql;

public class Data<T> {
    private final String name;
    private final T value;

    Data(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
