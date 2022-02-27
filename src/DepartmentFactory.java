import java.util.ArrayList;

public class DepartmentFactory {
    private enum DepartmentType {
        Finance,
        IT,
        Management,
        Marketing
    }

    public static Department createDepartment(DepartmentType name) {
        return switch (name) {
            case Finance -> new Finance();
            case IT -> new IT();
            case Management -> new Management();
            case Marketing -> new Marketing();
        };
    }

    public static ArrayList<Department> createAllDepartments() {
        ArrayList<Department> departments = new ArrayList<>();
        for (DepartmentType depType : DepartmentType.values()) {
            departments.add(createDepartment(depType));
        }
        return departments;
    }

    public static String getDepartmentName(Department department) {
        return department.getClass().getSimpleName();
    }
}
