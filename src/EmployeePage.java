import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EmployeePage extends ProfilePage {
    private final JTree employeeTree;
    private final DefaultMutableTreeNode root;
    private final JPanel employeePanel;

    public EmployeePage(Mediator mediator) {
        super(mediator);
        employeePanel = new JPanel(new BorderLayout());
        root = new DefaultMutableTreeNode("Employees");
        employeeTree = new JTree(root);
        employeeTree.setCellRenderer(new EmployeeTreeCellRenderer());
        JScrollPane employeeScroll = new JScrollPane(employeeTree);
        employeePanel.add(employeeScroll, BorderLayout.CENTER);

        try {
            ImageIcon employeeIcon = new ImageIcon(ImageIO.read(new File("./res/icons/employees.png")));
            insertTab(employeePanel, 1, employeeIcon);
        } catch (IOException e) {
            Log.print("./res/icons/employees.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(employeePanel, 1, "Employees");
        }
    }

    public void createTree(ArrayList<Department> departments) {
        root.removeAllChildren();
        for (Department department : departments) {
            DefaultMutableTreeNode dep = new DefaultMutableTreeNode(department);
            root.add(dep);
            for (Employee employee : department.getEmployees()) {
                DefaultMutableTreeNode emp = new DefaultMutableTreeNode(employee);
                dep.add(emp);
            }
        }
        employeeTree.expandRow(0);
        DefaultTreeModel model = (DefaultTreeModel) employeeTree.getModel();
        model.reload();
    }

    @Override
    public String getPageName() {
        return "EMPLOYEE_PAGE";
    }
}
