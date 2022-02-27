import java.time.LocalDate;
import java.util.ArrayList;

public class Manager extends Employee {
    private final ArrayList<Request<Job, Consumer>> requests;

    public Manager(Resume resume, String company, int salary) {
        super(resume, company, salary);
        requests = new ArrayList<>();
    }

    public void process(Job job) {
        while (job.getPositions() != 0) {
            double maxScore = 0;
            Request<Job, Consumer> bestRequest = null;
            for (Request<Job, Consumer> request : requests) {
                if (request.getKey() == job) {
                    if (request.getScore() > maxScore) {
                        maxScore = request.getScore();
                        bestRequest = request;
                    }
                }
            }
            if (maxScore == 0)
                break;
            hireUser(bestRequest);
        }
        closeNotification(job);
    }

    public void processAll() {
        for (Job job : Application.getInstance().getCompany(getCompanyName()).getJobs()) {
            process(job);
        }
    }

    public void addRequest(Request<Job, Consumer> request) {
        requests.add(request);
    }

    public void removeAllRequests(User user) {
        for (Manager manager : Application.getInstance().getManagers()) {
            manager.getRequests().removeIf(request -> request.getValue1() == user);
        }
    }

    public void removeAllRequests(Job job) {
        requests.removeIf(request -> request.getKey() == job);
    }

    public void removeObserver(User user) {
        for (Company company : Application.getInstance().getCompanies()) {
            company.removeObserver(user);
        }
    }

    public void hireUser(Request<Job, Consumer> request) {
        Job job = request.getKey();
        User user = (User) request.getValue1();

        job.removeCandidate(user);
        job.setPositions(job.getPositions() - 1);

        Employee employee = user.convert();
        employee.setCompanyName(getCompanyName());
        employee.setSalary(job.getSalary());
        Company company = Application.getInstance().getCompany(getCompanyName());
        Department department = company.getDepartment(job.getDepartment());
        department.add(employee);
        String departmentName = DepartmentFactory.getDepartmentName(department);
        Experience experience = new Experience(getCompanyName(), job.getName(), departmentName, LocalDate.now(), null);
        employee.add(experience);
        removeObserver(user);

        Information info = user.getResume().getInfo();
        String message = info.getName() + " a fost acceptat ca " + job.getName() + " la " + getCompanyName();
        Log.print(message, Log.MessageType.Event, Log.Color.BLUE);
        removeAllRequests(user);
        removeAllRequests(job);
        closeNotification(job);
    }

    public void rejectUser(Request<Job, Consumer> request) {
        Job job = request.getKey();
        User user = (User) request.getValue1();
        requests.remove(request);

        Company company = Application.getInstance().getCompany(getCompanyName());
        String message = "Ai fost respins de la jobul de " + job.getName() + " de la compania " + getCompanyName();
        Notification notification = new Notification(message);
        notification.setMessage(message);
        company.notifyObserver(user, notification);
        message = user.getResume().getInfo().getName() + " a fost respins de la jobul de " +
                job.getName() + " de la compania " + getCompanyName();
        Log.print(message, Log.MessageType.Event, Log.Color.RED);
    }

    public void closeNotification(Job job) {
        if (job.getOpenFlag()) {
            job.setOpenFlag(false);
            Company company = Application.getInstance().getCompany(getCompanyName());
            String message = "Jobul de " + job.getName() + " de la compania " +
                    getCompanyName() + " a fost inchis.";
            Log.print(message, Log.MessageType.Info, Log.Color.YELLOW);
            Notification notification = new Notification(message);
            company.notifyAllObservers(notification);
            job.removeAllCandidates();
        }
    }

    public ArrayList<Request<Job, Consumer>> getRequests() {
        return requests;
    }
}
