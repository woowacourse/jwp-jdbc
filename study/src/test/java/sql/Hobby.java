package sql;

public class Hobby {
    private final double yes;
    private final double no;

    public Hobby(final double yes, final double no) {
        this.yes = yes;
        this.no = no;
    }

    public double getYes() {
        return yes;
    }

    public double getNo() {
        return no;
    }

    @Override
    public String toString() {
        return "Hobby{" + "yes=" + yes + ", no=" + no + '}';
    }
}
