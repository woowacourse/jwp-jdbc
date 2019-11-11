package sql;

public class QueryResult<T> {
    private String name;
    private T result;

    public QueryResult(String name, T result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public T getResult() {
        return result;
    }
}
