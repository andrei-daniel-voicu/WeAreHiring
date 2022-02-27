import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Information {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final LocalDate birthDate;
    private final String gender;
    private final ArrayList<Language> languages;

    public Information(String firstName, String lastName, String email, String phone,
                       LocalDate birthDate, String gender, ArrayList<Language> languages) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.languages = languages;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void add(Language language) {
        languages.add(language);
    }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: ").append(firstName)
                .append(" ").append(lastName).append("\n");
        stringBuilder.append("Email: ").append(email).append("\n");
        stringBuilder.append("Phone: ").append(phone).append("\n");
        stringBuilder.append("Birthdate: ")
                .append(birthDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).append("\n");
        stringBuilder.append("Gender: ").append(gender).append("\n");
        stringBuilder.append("Languages: ");
        int index = 0;
        for (Language language : languages) {
            if (index != 0)
                stringBuilder.append(", ");
            stringBuilder.append(language.toString());
            index++;
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public String toJSON() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t\t\t\"name\": \"").append(firstName)
                .append(" ").append(lastName).append("\",\n");
        stringBuilder.append("\t\t\t\"email\": \"").append(email).append("\",\n");
        stringBuilder.append("\t\t\t\"phone\": \"").append(phone).append("\",\n");
        stringBuilder.append("\t\t\t\"date_of_birth\": \"")
                .append(birthDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).append("\",\n");
        stringBuilder.append("\t\t\t\"genre\": \"").append(gender).append("\",\n");
        stringBuilder.append("\t\t\t\"languages\": [");
        int index = 0;
        for (Language language : languages) {
            if (index != 0)
                stringBuilder.append(", ");
            stringBuilder.append("\"").append(language.getLanguage()).append("\"");
            index++;
        }
        stringBuilder.append("],\n");
        stringBuilder.append("\t\t\t\"languages_level\": [");
        index = 0;
        for (Language language : languages) {
            if (index != 0)
                stringBuilder.append(", ");
            stringBuilder.append("\"").append(language.getLevel()).append("\"");
            index++;
        }
        stringBuilder.append("],\n");
        return stringBuilder.toString();
    }
}
