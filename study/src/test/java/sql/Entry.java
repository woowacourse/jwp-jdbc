package sql;

import java.util.Objects;

public class Entry {
    private final String key;
    private final Number value;

    Entry(final String key, final Number value) {
        this.key = key;
        this.value = value;
    }

    String getKey() {
        return key;
    }

    Number getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        final Entry entry = (Entry) another;
        return Objects.equals(key, entry.key) &&
                Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "Entry {" +
                "key: \"" + key + "\"" +
                ", value: " + value +
                "}";
    }
}
