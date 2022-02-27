import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        Application application = Application.getInstance();

        HashMap<String, String> paths = new HashMap<>();
        paths.put("companies", "./res/input/companies.json");
        paths.put("jobs", "./res/input/jobs.json");
        paths.put("relatives", "./res/input/relatives.json");
        paths.put("consumers", "./res/input/consumers.json");
        application.loadData(paths);
        for (User user : application.getUsers()) {
            for (Job job : application.getJobs(user.getInterestedCompanies())) {
                job.apply(user);
            }
        }
        for (Manager manager : application.getManagers()) {
            manager.processAll();
        }
        paths.clear();
        paths.put("companies", "./res/output/companies.json");
        paths.put("jobs", "./res/output/jobs.json");
        paths.put("relatives", "./res/output/relatives.json");
        paths.put("consumers", "./res/output/consumers.json");
        application.saveData(paths);
    }
}
