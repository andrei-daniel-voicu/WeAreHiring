import java.time.LocalDate;

public class Finance extends Department {
    @Override
    public double getTotalSalaryBudget() {
        int salary = 0;
        for (Employee employee : getEmployees()) {
            Experience experience = employee.getResume().getExperience().first();
            if (LocalDate.now().getYear() - experience.getStartDate().getYear() < 1) {
                salary += employee.getSalary() * 1.1;
            } else {
                salary += employee.getSalary() * 1.16;
            }
        }
        return salary;
    }
}
