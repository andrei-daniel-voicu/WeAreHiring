import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Education implements Comparable<Education> {
    private String level;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double grade;

    public Education(String level, String name, LocalDate startDate, LocalDate endDate, Double grade) {
        try {
            if (endDate != null)
                if (startDate.compareTo(endDate) > 0)
                    throw new InvalidDatesException();
            this.startDate = startDate;
            this.endDate = endDate;
            this.name = name;
            this.level = level;
            this.grade = grade;
        } catch (InvalidDatesException e) {
            Log.print(e.getMessage(), Log.MessageType.ERROR, Log.Color.RED);
            System.exit(1);
        }
    }

    @Override
    public int compareTo(Education o) {
        if (o == null)
            return 0;
        if (o.endDate == null || endDate == null) {
            return startDate.compareTo(o.startDate);
        } else {
            if (endDate.equals(o.endDate)) {
                return o.grade.compareTo(grade);
            }
            return o.endDate.compareTo(endDate);
        }
    }

    public Double getGrade() {
        return grade;
    }


    public String getLevel() {
        return level;
    }


    public String getName() {
        return name;
    }


    public LocalDate getEndDate() {
        return endDate;
    }

    public String toString() {
        String string = "";
        string += "\tlevel: " + level + "\n";
        string += "\tname: " + name + "\n";
        string += "\tstart_date: " + startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\n";
        if (endDate == null) {
            string += "\tend_date: null\n";
        } else {
            string += "\tend_date: " + endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\n";
        }
        string += "\tgrade: " + grade + "\n";
        return string;
    }

    public String toJSON() {
        String string = "";
        string += "\t\t\t\t{\n";
        string += "\t\t\t\t\t\"level\": \"" + level + "\",\n";
        string += "\t\t\t\t\t\"name\": \"" + name + "\",\n";
        string += "\t\t\t\t\t\"start_date\": \"" + startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\",\n";
        if (endDate == null) {
            string += "\t\t\t\t\t\"end_date\": null,\n";
        } else {
            string += "\t\t\t\t\t\"end_date\": \"" + endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\",\n";
        }
        string += "\t\t\t\t\t\"grade\": " + grade;
        string += "\n\t\t\t\t}";
        return string;
    }
}
