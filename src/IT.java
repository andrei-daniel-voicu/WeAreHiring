public class IT extends Department {
    @Override
    public double getTotalSalaryBudget() {
        return getEmployees().stream().mapToDouble(Employee::getSalary).sum();
    }
}
