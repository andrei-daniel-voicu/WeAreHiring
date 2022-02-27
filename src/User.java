import java.util.ArrayList;

public class User extends Consumer implements Observer {
    private final ArrayList<String> interestedCompanies;
    private final ArrayList<Notification> notifications;

    public User(Resume resume) {
        super(resume);
        interestedCompanies = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    public Employee convert() {
        Employee employee = new Employee(getResume(), getRelatives());
        Application.getInstance().replaceRelatives(this, employee);
        Application.getInstance().remove(this);
        Application.getInstance().moveConsumerCredentials(this, employee);
        return employee;
    }

    public Double getTotalScore() {
        return expYears() * 1.5 + meanGPA();
    }

    public ArrayList<String> getInterestedCompanies() {
        return interestedCompanies;
    }

    public void add(String company) {
        interestedCompanies.add(company);
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResume().getInfo().toString());
        stringBuilder.append("Favorite Companies: ");
        int index = 0;
        for (String interested : interestedCompanies) {
            if (index != 0)
                stringBuilder.append(", ");
            stringBuilder.append(interested);
            index++;
        }
        stringBuilder.append("\n");
        stringBuilder.append(getResume().toString(false));
        return stringBuilder.toString();
    }

    @Override
    public String toJSON() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResume().getInfo().toJSON());
        stringBuilder.append("\t\t\t\"interested_companies\": [");
        int index = 0;
        for (String interested : interestedCompanies) {
            if (index != 0)
                stringBuilder.append(", ");
            stringBuilder.append("\"").append(interested).append("\"");
            index++;
        }
        stringBuilder.append("],\n");
        stringBuilder.append(getResume().toJSON(false));
        return stringBuilder.toString();
    }

    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }
}
