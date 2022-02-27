import java.util.ArrayList;

public class Job {
    private String name;
    private String company;
    private String department;
    private Boolean openFlag;
    private Constraint gradYear;
    private Constraint expYears;
    private Constraint mean;
    private ArrayList<User> candidates;
    private Integer positions;
    private Integer salary;

    public Job(String name, String company, String department, Constraint gradYear,
               Constraint expYears, Constraint mean, Integer positions, Integer salary) {
        this.name = name;
        this.company = company;
        this.department = department;
        this.gradYear = gradYear;
        this.expYears = expYears;
        this.mean = mean;
        this.positions = positions;
        if (this.positions == 0)
            this.openFlag = false;
        else
            this.openFlag = true;
        this.salary = salary;
        candidates = new ArrayList<>();
    }

    public void apply(User user) {
        if (openFlag) {
            if (meetsRequirements(user)) {
                Company company = Application.getInstance().getCompany(this.company);
                Recruiter recruiter = company.getRecruiter(user);
                recruiter.evaluate(this, user);
                candidates.add(user);
                Information info = user.getResume().getInfo();
                String message = info.getName() + " a aplicat pe postul de " + name + " la " + this.company;
                Log.print(message, Log.MessageType.Event, Log.Color.YELLOW);
            } else {
                Information info = user.getResume().getInfo();
                String message = info.getName() + " nu intalneste criteriile pentru a candida pe postul de "
                        + name + " la " + company;
                Log.print(message, Log.MessageType.Event, Log.Color.RED);
            }
        } else {
            String message = "Job-ul de " + name + " de la " + company + " nu mai este disponibil";
            Log.print(message, Log.MessageType.Event, Log.Color.BLACK);
        }
    }

    private int compare(Double number, Double constraint) {
        if (constraint == null)
            return 0;
        Double diff = number - constraint;
        if (diff == 0)
            return 0;
        return diff > 0 ? 1 : -1;
    }

    private boolean meetsGradYear(User user) {
        Integer gradYear = user.getGraduationYear();
        if (gradYear == null)
            gradYear = 0;
        return compare(gradYear.doubleValue(), this.gradYear.getMin()) >= 0
                && compare(gradYear.doubleValue(), this.gradYear.getMax()) <= 0;
    }

    private boolean meetsExpYears(User user) {
        Integer expYears = user.expYears();
        return compare(expYears.doubleValue(), this.expYears.getMin()) >= 0
                && compare(expYears.doubleValue(), this.expYears.getMax()) <= 0;
    }

    private boolean meetsMean(User user) {
        Double meanGPA = user.meanGPA();
        return compare(meanGPA, mean.getMin()) >= 0
                && compare(meanGPA, mean.getMax()) <= 0;
    }

    public boolean meetsRequirements(User user) {
        return meetsGradYear(user) && meetsExpYears(user) && meetsMean(user);
    }

    public void removeCandidate(User user) {
        candidates.remove(user);
    }

    public void removeAllCandidates() {
        candidates.clear();
    }

    public Boolean getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Boolean openFlag) {
        this.openFlag = openFlag;
    }

    public Constraint getGradYear() {
        return gradYear;
    }

    public void setGradYear(Constraint gradYear) {
        this.gradYear = gradYear;
    }

    public Constraint getExpYears() {
        return expYears;
    }

    public void setExpYears(Constraint expYears) {
        this.expYears = expYears;
    }

    public Constraint getMean() {
        return mean;
    }

    public void setMean(Constraint mean) {
        this.mean = mean;
    }

    public ArrayList<User> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<User> candidates) {
        this.candidates = candidates;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getPositions() {
        return positions;
    }

    public void setPositions(Integer positions) {
        this.positions = positions;
    }

    public String toJSON() {
        String string = "";
        string += "\t\t{\n";
        string += "\t\t\t\"name\": \"" + name + "\",\n";
        string += "\t\t\t\"company\": \"" + company + "\",\n";
        string += "\t\t\t\"department\": \"" + department + "\",\n";
        string += "\t\t\t\"positions\": " + positions + ",\n";
        string += "\t\t\t\"salary\": " + salary + ",\n";
        string += "\t\t\t\"gradYear\": \n" + gradYear.toJSON() + ",\n";
        string += "\t\t\t\"expYears\": \n" + expYears.toJSON() + ",\n";
        string += "\t\t\t\"mean\": \n" + mean.toJSON() + "\n";
        string += "\t\t}";
        return string;
    }

    @Override
    public String toString() {
        String string = "";
        string += "Name: " + name + "\n";
        string += "Company: " + company + "\n";
        string += "Department: " + department + "\n";
        string += "Positions: " + positions + "\n";
        string += "Salary: " + salary + "\n";
        string += "Graduation year: \n" + gradYear;
        string += "Experience years: \n" + expYears;
        string += "Mean: \n" + mean;
        return string;
    }
}
