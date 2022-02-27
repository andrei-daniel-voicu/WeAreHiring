import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Company implements Subject {
    private String name;
    private Manager manager;
    private ArrayList<Department> departments;
    private final ArrayList<Recruiter> recruiters;
    private final ArrayList<Observer> observers;

    public Company(String name) {
        this.name = name;
        departments = new ArrayList<>();
        recruiters = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public void add(Department department) {
        departments.add(department);
    }

    public void add(Recruiter recruiter) {
        recruiters.add(recruiter);
    }

    public void add(Employee employee, Department department) {
        department.add(employee);
    }

    public void add(Employee employee, String department) {
        for (Department dep : departments) {
            if (DepartmentFactory.getDepartmentName(dep).equals(department)) {
                dep.add(employee);
            }
        }
    }

    public void remove(Employee employee) {
        for (Department department : departments) {
            department.remove(employee);
            if (employee instanceof Recruiter) {
                remove((Recruiter) employee);
            }
        }
    }

    public void remove(Department department) {
        departments.remove(department);
    }

    public void remove(Recruiter recruiter) {
        recruiters.remove(recruiter);
    }

    public void move(Department source, Department destination) {
        destination.getEmployees().addAll(source.getEmployees());
        destination.getJobs().addAll(source.getJobs());
        remove(source);
    }

    public void move(Employee employee, Department newDepartment) {
        for (Department department : departments) {
            department.remove(employee);
        }
        newDepartment.add(employee);
        for (Experience experience : employee.getResume().getExperience()) {
            if (experience.getEndDate() == null) {
                experience.setDepartment(DepartmentFactory.getDepartmentName(newDepartment));
            }
        }
    }

    public boolean contains(Department department) {
        return departments.contains(department);
    }

    public boolean contains(Employee employee) {
        for (Department department : departments) {
            if (department.contains(employee))
                return true;
        }
        return false;
    }

    public boolean contains(Recruiter recruiter) {
        return recruiters.contains(recruiter);
    }

    public Recruiter getMaxRecruiter() {
        return Collections.max(recruiters, Comparator.comparing(Recruiter::getRating));
    }

    public Recruiter getRecruiter(User user) {
        int max = -2;
        Recruiter bestRecruiter = null;
        for (Recruiter recruiter : recruiters) {
            int degree = user.getDegreeInFriendship(recruiter);
            if (degree > max) {
                max = degree;
                bestRecruiter = recruiter;
            } else if (degree == max) {
                if (recruiter.getRating() > bestRecruiter.getRating())
                    bestRecruiter = recruiter;
            }
        }
        if (bestRecruiter == null) {
            bestRecruiter = getMaxRecruiter();
        }
        return bestRecruiter;
    }

    public ArrayList<Job> getJobs() {
        ArrayList<Job> openJobs = new ArrayList<>();
        for (Department department : departments) {
            openJobs.addAll(department.getJobs());
        }
        return openJobs;
    }

    public ArrayList<Job> getAllJobs() {
        ArrayList<Job> jobs = new ArrayList<>();
        for (Department department : departments) {
            jobs.addAll(department.getAllJobs());
        }
        return jobs;
    }

    public ArrayList<Employee> getEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        for (Department department : departments) {
            employees.addAll(department.getEmployees());
        }
        return employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        remove(this.manager);
        this.manager = manager;
        add(manager, "Management");
    }

    public Department getDepartment(String name) {
        for (Department department : departments) {
            String depName = DepartmentFactory.getDepartmentName(department);
            if (depName.equals(name))
                return department;
        }
        return null;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public ArrayList<Recruiter> getRecruiters() {
        return recruiters;
    }

    public String toJSON() {
        String string = "";
        string += "\t\t{\n";
        string += "\t\t\t\"name\": \"" + name + "\"\n";
        string += "\t\t}";
        return string;
    }

    @Override
    public void addObserver(User user) {
        observers.add(user);
    }

    @Override
    public void removeObserver(User user) {
        observers.remove(user);
    }

    @Override
    public void notifyAllObservers(Notification notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }

    @Override
    public void notifyObserver(Observer observer, Notification notification) {
        observer.update(notification);
    }
}
