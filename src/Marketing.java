public class Marketing extends Department {
    @Override
    public double getTotalSalaryBudget() {
        double salary = 0;
        for (Employee employee : getEmployees()) {
            if (employee.getSalary() > 5000)
                salary += employee.getSalary() * 1.1;
            else if (employee.getSalary() < 3000) {
                salary += employee.getSalary();
            } else {
                salary += employee.getSalary() * 1.16;
            }
        }
        return salary;
    }
}
