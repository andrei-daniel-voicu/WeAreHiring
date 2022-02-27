import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSONWriter {
    public void writeFile(String path, String content) {
        File companiesFile = new File(path);
        try {
            FileWriter writer = new FileWriter(companiesFile);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            Log.print("An error occurred during save file creation!", Log.MessageType.ERROR, Log.Color.RED);
            System.exit(1);
        }
    }

    public void companiesToJSON(String path, ArrayList<Company> companies) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        int size = companies.size();

        stringBuilder.append("{\n\t\"companies\": [\n");
        for (Company company : companies) {
            stringBuilder.append(company.toJSON());
            if (index != size - 1)
                stringBuilder.append(",");
            stringBuilder.append("\n");
            index++;
        }
        stringBuilder.append("\t]\n}");
        writeFile(path, stringBuilder.toString());
    }

    public void jobsToJSON(String path, ArrayList<Job> jobs) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        int size = jobs.size();

        stringBuilder.append("{\n\t\"jobs\": [\n");
        for (Job job : jobs) {
            stringBuilder.append(job.toJSON());
            if (index != size - 1)
                stringBuilder.append(",");
            stringBuilder.append("\n");
            index++;
        }
        stringBuilder.append("\t]\n}");
        writeFile(path, stringBuilder.toString());
    }

    public void relativesToJSON(String path, ArrayList<Consumer> consumers) {
        Application application = Application.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        char type;
        String notation;
        int order;
        int size;
        int index = 0;

        size = 0;
        for (Consumer consumer : consumers) {
            if (consumer.getRelatives().size() != 0) {
                size++;
            }
        }
        stringBuilder.append("{\n");
        for (Consumer consumer : consumers) {
            if (consumer.getRelatives().size() != 0) {
                type = application.getType(consumer).charAt(0);
                order = application.getIndex(consumer) + 1;
                notation = "\t\"" + type + order + "\": [";
                stringBuilder.append(notation);
                int ind = 0;
                for (Consumer relative : consumer.getRelatives()) {
                    if (ind != 0)
                        stringBuilder.append(", ");
                    type = application.getType(relative).charAt(0);
                    order = application.getIndex(relative) + 1;
                    notation = "\"" + type + order + "\"";
                    stringBuilder.append(notation);
                    ind++;
                }
                stringBuilder.append("]");
                if (index != size - 1)
                    stringBuilder.append(",");
                stringBuilder.append("\n");
                index++;
            }
        }
        stringBuilder.append("}");
        writeFile(path, stringBuilder.toString());
    }

    public void consumersToJSON(String path, ArrayList<Employee> employees, ArrayList<Recruiter> recruiters,
                                ArrayList<User> users, ArrayList<Manager> managers) {
        StringBuilder stringBuilder = new StringBuilder();
        int index;
        int size;


        stringBuilder.append("{\n\t\"employees\": [");
        index = 0;
        size = employees.size();
        for (Employee employee : employees) {
            stringBuilder.append("\n\t\t{\n");
            stringBuilder.append(employee.toJSON());
            stringBuilder.append("\t\t}");
            if (index != size - 1)
                stringBuilder.append(",");
            index++;
        }
        stringBuilder.append("\n\t],\n");

        stringBuilder.append("\t\"recruiters\": [");
        index = 0;
        size = recruiters.size();
        for (Recruiter recruiter : recruiters) {
            stringBuilder.append("\n\t\t{\n");
            stringBuilder.append(recruiter.toJSON());
            stringBuilder.append("\t\t}");
            if (index != size - 1)
                stringBuilder.append(",");
            index++;
        }
        stringBuilder.append("\n\t],\n");

        stringBuilder.append("\t\"users\": [");
        index = 0;
        size = users.size();
        for (User user : users) {
            stringBuilder.append("\n\t\t{\n");
            stringBuilder.append(user.toJSON());
            stringBuilder.append("\t\t}");
            if (index != size - 1)
                stringBuilder.append(",");
            index++;
        }
        stringBuilder.append("\n\t],\n");

        stringBuilder.append("\t\"managers\": [");
        index = 0;
        size = managers.size();
        for (Manager manager : managers) {
            stringBuilder.append("\n\t\t{\n");
            stringBuilder.append(manager.toJSON());
            stringBuilder.append("\t\t}");
            if (index != size - 1)
                stringBuilder.append(",");
            index++;
        }
        stringBuilder.append("\n\t]");
        stringBuilder.append("\n}");
        writeFile(path, stringBuilder.toString());
    }
}
