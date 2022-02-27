import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Experience implements Comparable<Experience> {
    private LocalDate startDate;
    private LocalDate endDate;
    private String position;
    private String company;
    private String department;

    public Experience(String company, String position, String department, LocalDate startDate, LocalDate endDate) {
        try {
            if (endDate != null)
                if (startDate.compareTo(endDate) > 0)
                    throw new InvalidDatesException();
            this.startDate = startDate;
            this.endDate = endDate;
            this.position = position;
            this.company = company;
            this.department = department;
        } catch (InvalidDatesException e) {
            Log.print(e.getMessage(), Log.MessageType.ERROR, Log.Color.RED);
            System.exit(1);
        }
    }

    @Override
    public int compareTo(Experience o) {
        if (o == null)
            return 0;
        if (o.endDate == null || endDate == null || endDate.equals(o.endDate)) {
            if (company.equals(o.company)) {
                return startDate.compareTo(o.startDate);
            }
            return company.compareTo(o.company);
        }
        return o.endDate.compareTo(endDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String companyName) {
        this.company = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        String string = "";
        string += "\tCompany: " + company + "\n";
        string += "\tPosition: " + position + "\n";
        if (department != null)
            string += "\tDepartament: " + department + "\n";
        string += "\tstart_date: " + startDate.format(DateTimeFormatter.ofPattern("d.MM.yyyy")) + "\n";
        if (endDate == null) {
            string += "\tend_date: null\n";
        } else {
            string += "\tend_date: " + endDate.format(DateTimeFormatter.ofPattern("d.MM.yyyy")) + "\n";
        }
        return string;
    }

    public String toJSON() {
        String string = "";
        string += "\t\t\t\t{\n";
        string += "\t\t\t\t\t\"company\": \"" + company + "\",\n";
        string += "\t\t\t\t\t\"position\": \"" + position + "\",\n";
        if (department != null)
            string += "\t\t\t\t\t\"department\": \"" + department + "\",\n";
        string += "\t\t\t\t\t\"start_date\": \"" + startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\",\n";
        if (endDate == null) {
            string += "\t\t\t\t\t\"end_date\": null\n";
        } else {
            string += "\t\t\t\t\t\"end_date\": \"" + endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\"";
        }
        string += "\n\t\t\t\t}";
        return string;
    }
}
