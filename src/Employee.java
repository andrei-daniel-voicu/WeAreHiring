import java.util.ArrayList;

public class Employee extends Consumer {
    private String companyName;
    private Integer salary;

    public Employee(Resume resume, String companyName, int salary) {
        super(resume);
        this.companyName = companyName;
        this.salary = salary;
    }

    public Employee(Resume resume, ArrayList<Consumer> relatives) {
        super(resume, relatives);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        String string = "";
        string += getResume().getInfo().toString();
        string += "Salary: " + salary + "\n";
        string += getResume().toString(false);
        return string;
    }

    @Override
    public String toJSON() {
        String string = "";
        string += getResume().getInfo().toJSON();
        string += "\t\t\t\"salary\": " + salary + ",\n";
        string += getResume().toJSON(false);
        return string;
    }
}

