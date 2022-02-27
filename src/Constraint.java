public class Constraint {
    private final Double min;
    private final Double max;

    public Constraint(Double min, Double max) {
        this.min = min;
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    @Override
    public String toString() {
        String string = "";
        string += "\tMin: " + min + "\n";
        string += "\tMax: " + max + "\n";
        string += "\n";
        return string;
    }

    public String toJSON() {
        String string = "";
        string += "\t\t\t{\n";
        string += "\t\t\t\t\"min\": " + min + ",\n";
        string += "\t\t\t\t\"max\": " + max + "\n";
        string += "\t\t\t}";
        return string;
    }
}
