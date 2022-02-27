import java.util.ArrayList;

public abstract class Department {
    private final ArrayList<Employee> employees;
    private ArrayList<Job> jobs;

    public abstract double getTotalSalaryBudget();

    public Department() {
        employees = new ArrayList<>();
        jobs = new ArrayList<>();
    }

    public void add(Employee employee) {
        employees.add(employee);
    }

    public void remove(Employee employee) {
        employees.remove(employee);
    }

    public void add(Job job) {
        if (job.getOpenFlag()) {
            Company company = Application.getInstance().getCompany(job.getCompany());
            String message = "Compania " + company.getName() + " a adaugat un job nou: " + job.getName();
            Log.print(message, Log.MessageType.Info, Log.Color.YELLOW);
            Notification notification = new Notification(message);
            company.notifyAllObservers(notification);
        }
        jobs.add(job);
    }

    public boolean contains(Employee employee) {
        return employees.contains(employee);
    }

    public boolean contains(Job job) {
        return jobs.contains(job);
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public ArrayList<Job> getJobs() {
        ArrayList<Job> openJobs = new ArrayList<>();
        for (Job job : jobs) {
            if (job.getOpenFlag()) {
                openJobs.add(job);
            }
        }
        return openJobs;
    }

    public ArrayList<Job> getAllJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }
}
