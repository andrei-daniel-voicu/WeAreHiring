import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JSONParser {
    private final ArrayList<Employee> employeesOrder;
    private final ArrayList<Recruiter> recruitersOrder;
    private final ArrayList<User> usersOrder;
    private final ArrayList<Manager> managersOrder;

    public JSONParser() {
        employeesOrder = new ArrayList<>();
        recruitersOrder = new ArrayList<>();
        usersOrder = new ArrayList<>();
        managersOrder = new ArrayList<>();
    }

    private Information parseInformation(JSONObject obj) {
        Consumer.Resume.ResumeBuilder resumeBuilder = new Consumer.Resume.ResumeBuilder();
        String name = obj.getString("name");
        String[] names = name.split(" ");
        String firstName = names[0];
        String lastName = names[1];
        String email = obj.getString("email");
        String phone = obj.getString("phone");
        String gender = obj.getString("genre");
        String date = obj.getString("date_of_birth");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthDate = LocalDate.parse(date, format);
        JSONArray languages = obj.getJSONArray("languages");
        JSONArray languages_level = obj.getJSONArray("languages_level");
        ArrayList<Language> languagesArr = new ArrayList<>();
        for (int i = 0; i < languages.length(); i++) {
            languagesArr.add(new Language(languages.getString(i), languages_level.getString(i)));
        }
        return new Information(firstName, lastName, email, phone, birthDate, gender, languagesArr);
    }

    private Education parseEducation(JSONObject obj) {
        String level = obj.getString("level");
        String schoolName = obj.getString("name");
        String date = obj.getString("start_date");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startDate = LocalDate.parse(date, format);
        LocalDate endDate;
        if (obj.isNull("end_date")) {
            endDate = null;
        } else {
            date = obj.getString("end_date");
            endDate = LocalDate.parse(date, format);
        }
        Double grade = obj.getDouble("grade");
        return new Education(level, schoolName, startDate, endDate, grade);
    }

    private Experience parseExperience(JSONObject obj) {
        String companyName = obj.getString("company");
        String position = obj.getString("position");
        String department = null;
        if (obj.has("department")) {
            department = obj.getString("department");
        }
        String date = obj.getString("start_date");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startDate = LocalDate.parse(date, format);
        LocalDate endDate;
        if (obj.isNull("end_date")) {
            endDate = null;
        } else {
            date = obj.getString("end_date");
            endDate = LocalDate.parse(date, format);
        }
        return new Experience(companyName, position, department, startDate, endDate);
    }

    private Consumer.Resume parseResume(JSONObject jsonObj) {
        Consumer.Resume.ResumeBuilder resumeBuilder = new Consumer.Resume.ResumeBuilder();
        resumeBuilder.info(parseInformation(jsonObj));
        JSONArray educations = jsonObj.getJSONArray("education");
        for (int j = 0; j < educations.length(); j++) {
            resumeBuilder.education(parseEducation(educations.getJSONObject(j)));
        }
        JSONArray experiences = jsonObj.getJSONArray("experience");
        for (int j = 0; j < experiences.length(); j++) {
            resumeBuilder.experience(parseExperience(experiences.getJSONObject(j)));
        }
        return resumeBuilder.build();
    }

    private void parseEmployees(JSONObject jsonObj) {
        JSONArray employees = jsonObj.getJSONArray("employees");
        for (int i = 0; i < employees.length(); i++) {
            Consumer.Resume resume = parseResume(employees.getJSONObject(i));
            String companyName = null;
            String depName = null;
            if (resume.getExperience() != null)
                for (Experience exp : resume.getExperience()) {
                    if (exp.getEndDate() == null) {
                        companyName = exp.getCompany();
                        depName = exp.getDepartment();
                        break;
                    }
                }
            int salary = employees.getJSONObject(i).getInt("salary");
            Employee employee = new Employee(resume, companyName, salary);
            Company company = Application.getInstance().getCompany(companyName);
            company.add(employee, company.getDepartment(depName));
            employeesOrder.add(employee);
        }
    }

    private void parseRecruiters(JSONObject jsonObj) {
        JSONArray recruiters = jsonObj.getJSONArray("recruiters");
        for (int i = 0; i < recruiters.length(); i++) {
            Consumer.Resume resume = parseResume(recruiters.getJSONObject(i));
            String companyName = null;
            if (resume.getExperience() != null)
                for (Experience exp : resume.getExperience()) {
                    if (exp.getEndDate() == null) {
                        companyName = exp.getCompany();
                        break;
                    }
                }
            int salary = recruiters.getJSONObject(i).getInt("salary");
            double rating = 5.0;
            if (recruiters.getJSONObject(i).has("rating")) {
                rating = recruiters.getJSONObject(i).getDouble("rating");
            }
            Recruiter recruiter = new Recruiter(resume, companyName, salary, rating);
            Company company = Application.getInstance().getCompany(companyName);
            company.add(recruiter, "IT");
            company.add(recruiter);
            recruitersOrder.add(recruiter);
        }
    }

    private void parseUsers(JSONObject jsonObj) {
        JSONArray users = jsonObj.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            Consumer.Resume resume = parseResume(users.getJSONObject(i));
            User user = new User(resume);
            JSONArray interested = users.getJSONObject(i).getJSONArray("interested_companies");
            for (int j = 0; j < interested.length(); j++) {
                String companyName = interested.getString(j);
                user.add(companyName);
                Application.getInstance().getCompany(companyName).addObserver(user);
            }
            Application.getInstance().add(user);
            usersOrder.add(user);
        }
    }

    private void parseManagers(JSONObject jsonObj) {
        JSONArray managers = jsonObj.getJSONArray("managers");
        for (int i = 0; i < managers.length(); i++) {
            Consumer.Resume resume = parseResume(managers.getJSONObject(i));
            String companyName = null;
            if (resume.getExperience() != null)
                for (Experience exp : resume.getExperience()) {
                    if (exp.getEndDate() == null) {
                        companyName = exp.getCompany();
                        break;
                    }
                }
            int salary = managers.getJSONObject(i).getInt("salary");
            Manager manager = new Manager(resume, companyName, salary);
            managersOrder.add(manager);
            Company company = Application.getInstance().getCompany(companyName);
            company.setManager(manager);
        }
    }

    private int getIndex(Consumer consumer) {
        String type = Application.getInstance().getType(consumer);
        return switch (type) {
            case "Employee" -> employeesOrder.indexOf(consumer);
            case "Recruiter" -> recruitersOrder.indexOf(consumer);
            case "User" -> usersOrder.indexOf(consumer);
            case "Manager" -> managersOrder.indexOf(consumer);
            default -> -1;
        };
    }

    public void parseCompanies(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        JSONObject jsonObj = new JSONObject(content);
        JSONArray companies = jsonObj.getJSONArray("companies");
        for (int i = 0; i < companies.length(); i++) {
            Company company = new Company(companies.getJSONObject(i).getString("name"));
            company.setDepartments(DepartmentFactory.createAllDepartments());
            Application.getInstance().add(company);
        }
    }

    public void parseRelatives(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        JSONObject jsonObj = new JSONObject(content);
        String key = "";
        char type;
        int order;

        ArrayList<Consumer> consumers = Application.getInstance().getConsumers();
        for (Consumer consumer : consumers) {
            type = Application.getInstance().getType(consumer).charAt(0);
            order = getIndex(consumer);
            key = Character.toString(type) + (order + 1);
            if (jsonObj.has(key)) {
                JSONArray relatives = jsonObj.getJSONArray(key);
                for (int j = 0; j < relatives.length(); j++) {
                    String relative = relatives.getString(j);
                    String[] subs = relative.split("[ERU]");
                    int nr = Integer.parseInt(subs[1]);
                    switch (relative.charAt(0)) {
                        case 'E' -> consumer.add(employeesOrder.get(nr - 1));
                        case 'R' -> consumer.add(recruitersOrder.get(nr - 1));
                        case 'U' -> consumer.add(usersOrder.get(nr - 1));
                        case 'M' -> consumer.add(managersOrder.get(nr - 1));
                    }
                }
            }
        }
    }

    public void parseConsumers(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        JSONObject jsonObj = new JSONObject(content);

        parseEmployees(jsonObj);
        parseRecruiters(jsonObj);
        parseUsers(jsonObj);
        parseManagers(jsonObj);
    }

    public void parseJobs(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        JSONObject jsonObj = new JSONObject(content);
        JSONArray jobs = jsonObj.getJSONArray("jobs");
        String name, company, department;
        Double min, max;
        int positions, salary;
        Constraint gradYear, expYears, mean;
        JSONObject constraint;

        for (int i = 0; i < jobs.length(); i++) {
            name = jobs.getJSONObject(i).getString("name");
            company = jobs.getJSONObject(i).getString("company");
            department = jobs.getJSONObject(i).getString("department");
            positions = jobs.getJSONObject(i).getInt("positions");
            salary = jobs.getJSONObject(i).getInt("salary");
            constraint = jobs.getJSONObject(i).getJSONObject("gradYear");
            min = constraint.isNull("min") ? null : constraint.getDouble("min");
            max = constraint.isNull("max") ? null : constraint.getDouble("max");

            gradYear = new Constraint(min, max);
            constraint = jobs.getJSONObject(i).getJSONObject("expYears");
            min = constraint.isNull("min") ? null : constraint.getDouble("min");
            max = constraint.isNull("max") ? null : constraint.getDouble("max");

            expYears = new Constraint(min, max);
            constraint = jobs.getJSONObject(i).getJSONObject("mean");
            min = constraint.isNull("min") ? null : constraint.getDouble("min");
            max = constraint.isNull("max") ? null : constraint.getDouble("max");
            mean = new Constraint(min, max);
            Job job = new Job(name, company, department, gradYear, expYears, mean, positions, salary);
            Application.getInstance().getCompany(company).getDepartment(department).add(job);
        }
    }
}
