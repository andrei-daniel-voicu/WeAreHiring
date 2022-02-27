public class Management extends Department {
    @Override
    public double getTotalSalaryBudget() {
        return getEmployees().stream().mapToDouble(Employee::getSalary).sum() * 1.16;

    }
}
