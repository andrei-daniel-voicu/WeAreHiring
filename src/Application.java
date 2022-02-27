import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Application {
    private static Application instance = null;
    private final ArrayList<Company> companies;
    private final ArrayList<User> users;
    private ArrayList<Credential> credentials;
    private boolean withGUI;

    private Application() {
        companies = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static Application getInstance() {
        if (instance == null)
            instance = new Application();
        return instance;
    }

    public Company getCompany(String name) {
        Optional<Company> optComp = companies.stream().
                filter(c -> c.getName().equals(name)).findFirst();
        return optComp.orElse(null);
    }

    public void add(Company company) {
        companies.add(company);
    }

    public void add(User user) {
        users.add(user);
    }

    public boolean remove(Company company) {
        return companies.remove(company);
    }

    public void replaceRelatives(User user, Employee employee) {
        for (Consumer consumer : getConsumers()) {
            Collections.replaceAll(consumer.getRelatives(), user, employee);
        }
    }

    public void printRelatives() {
        System.out.println();
        for (Consumer consumer : Application.getInstance().getConsumers()) {
            System.out.print(consumer.getResume().getInfo().getName() + ": ");
            for (Consumer relative : consumer.getRelatives()) {
                System.out.print(relative.getResume().getInfo().getName() + ", ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean remove(User user) {
        return users.remove(user);
    }

    public ArrayList<Job> getJobs(List<String> companies) {
        ArrayList<Job> openJobs = new ArrayList<>();

        for (String string : companies)
            openJobs.addAll(getCompany(string).getJobs());
        return openJobs;
    }

    public ArrayList<Job> getAllJobs() {
        ArrayList<Job> jobs = new ArrayList<>();

        for (Company company : companies)
            jobs.addAll(company.getAllJobs());
        return jobs;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public ArrayList<Employee> getEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        for (Company company : companies) {
            ArrayList<Department> departments = company.getDepartments();
            for (Department department : departments) {
                ArrayList<Employee> emp = department.getEmployees();
                for (Employee employee : emp) {
                    if (!(employee instanceof Manager) && !(employee instanceof Recruiter))
                        employees.add(employee);
                }
            }
        }
        return employees;
    }

    public ArrayList<Recruiter> getRecruiters() {
        ArrayList<Recruiter> recruiters = new ArrayList<>();
        for (Company company : companies) {
            recruiters.addAll(company.getRecruiters());
        }
        return recruiters;
    }

    public ArrayList<Manager> getManagers() {
        ArrayList<Manager> managers = new ArrayList<>();
        for (Company company : companies) {
            managers.add(company.getManager());
        }
        return managers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Consumer> getConsumers() {
        ArrayList<Consumer> consumers = new ArrayList<>();
        ArrayList<Employee> employees = getEmployees();
        ArrayList<Recruiter> recruiters = getRecruiters();
        ArrayList<User> users = getUsers();
        ArrayList<Manager> managers = getManagers();

        if (employees != null)
            consumers.addAll(getEmployees());
        if (recruiters != null)
            consumers.addAll(getRecruiters());
        if (managers != null)
            consumers.addAll(getManagers());
        if (users != null)
            consumers.addAll(getUsers());
        return consumers;
    }

    public void saveData(HashMap<String, String> paths) {
        Log.print("Saving data...", Log.MessageType.System, Log.Color.PURPLE);

        JSONWriter writer = new JSONWriter();
        writer.companiesToJSON(paths.get("companies"), companies);
        writer.jobsToJSON(paths.get("jobs"), getAllJobs());
        writer.relativesToJSON(paths.get("relatives"), getConsumers());
        writer.consumersToJSON(paths.get("consumers"), getEmployees(),
                getRecruiters(), getUsers(), getManagers());

        Log.print("Data saved!", Log.MessageType.System, Log.Color.PURPLE);
    }

    public void loadData(HashMap<String, String> paths) {
        Log.print("Loading data...", Log.MessageType.System, Log.Color.PURPLE);

        JSONParser parser = new JSONParser();
        try {
            parser.parseCompanies(paths.get("companies"));
        } catch (IOException e) {
            Log.print("File " + paths.get("companies") + " not found!", Log.MessageType.ERROR, Log.Color.RED);
            System.exit(1);
        }

        try {
            parser.parseConsumers(paths.get("consumers"));
        } catch (IOException e) {
            Log.print("File " + paths.get("consumers") + " not found!", Log.MessageType.ERROR, Log.Color.RED);
            System.exit(1);
        }

        try {
            parser.parseJobs(paths.get("jobs"));
        } catch (IOException e) {
            Log.print("File " + paths.get("jobs") + " not found!", Log.MessageType.ERROR, Log.Color.RED);
            System.exit(1);
        }

        try {
            parser.parseRelatives(paths.get("relatives"));
        } catch (IOException e) {
            Log.print("File " + paths.get("relatives") + " not found!", Log.MessageType.ERROR, Log.Color.RED);
            System.exit(1);
        }

        Log.print("Data loaded!", Log.MessageType.System, Log.Color.PURPLE);

        if (withGUI) {
            generateCredentials();
        }
    }

    public void generateCredentials() {
        Log.print("Generating passwords...", Log.MessageType.System, Log.Color.PURPLE);

        credentials = new ArrayList<>();
        for (Consumer consumer : getConsumers()) {
            String email = consumer.getResume().getInfo().getEmail();
            String username = email.split("@")[0];
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate date = consumer.getResume().getInfo().getBirthDate();
            String password = date.format(format);
            credentials.add(new Credential(consumer, username, password));
        }

        Log.print("Passwords generated!", Log.MessageType.System, Log.Color.PURPLE);
    }

    public Consumer getConsumerByCredentials(String username, String password) {
        if (withGUI) {
            Credential toFind = new Credential(null, username, password);
            for (Credential credential : credentials) {
                if (credential.equals(toFind))
                    return credential.getConsumer();
            }
        }
        return null;
    }

    public void moveConsumerCredentials(Consumer source, Consumer destination) {
        if (withGUI)
            for (Credential credential : credentials) {
                if (credential.getConsumer() == source)
                    credential.setConsumer(destination);
            }
    }

    public Consumer getConsumerByName(String name) {
        for (Consumer consumer : getConsumers()) {
            if (consumer.getResume().getInfo().getName().equals(name))
                return consumer;
        }
        return null;
    }

    public void setWithGUI(boolean withGUI) {
        this.withGUI = withGUI;
    }

    public String getType(Consumer consumer) {
        return consumer.getClass().getSimpleName();
    }

    public int getIndex(Consumer consumer) {
        String type = getType(consumer);
        return switch (type) {
            case "Employee" -> getEmployees().indexOf(consumer);
            case "Recruiter" -> getRecruiters().indexOf(consumer);
            case "User" -> getUsers().indexOf(consumer);
            case "Manager" -> getManagers().indexOf(consumer);
            default -> -1;
        };
    }
}
