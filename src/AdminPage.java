import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AdminPage extends Page {
    private final DefaultMutableTreeNode root;
    private final JPanel signOutPanel;
    private final JTree adminTree;
    private final JPanel adminTreePanel;
    private final JTabbedPane tabbedPane;
    private final JTextField salaryTextField;
    private boolean movable;
    private Employee selectedEmployee;

    public AdminPage(Mediator mediator) {
        super(mediator);
        tabbedPane = new JTabbedPane();
        movable = false;
        salaryTextField = new JTextField(20);
        salaryTextField.setEditable(false);
        adminTreePanel = new JPanel(new BorderLayout());
        root = new DefaultMutableTreeNode("Application");
        adminTree = new JTree(root);
        JScrollPane treeScroll = new JScrollPane(adminTree);
        adminTree.setCellRenderer(new AdminTreeCellRenderer());
        adminTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) adminTree.getLastSelectedPathComponent();
            if (node != null) {
                salaryTextField.setText("");
                salaryTextField.setEditable(false);
                movable = false;
                Object obj = node.getUserObject();
                if (node.getLevel() == 3) {
                    Department department = (Department) obj;
                    salaryTextField.setText(String.valueOf(department.getTotalSalaryBudget()));
                } else if (node.getLevel() == 2) {
                    if (obj instanceof Company) {
                        Company company = (Company) obj;
                        salaryTextField.setText("Manager: " +
                                company.getManager().getResume().getInfo().getName());
                    }
                } else if (node.getLevel() == 5) {
                    if (obj instanceof Employee) {
                        if (!(obj instanceof Manager) && !(obj instanceof Recruiter)) {
                            selectedEmployee = (Employee) obj;
                            salaryTextField.setEditable(true);
                            movable = true;
                        }
                    }
                }
            }
        });
        salaryTextField.addActionListener(e -> {
            if (movable) {
                Company company = Application.getInstance().getCompany(selectedEmployee.getCompanyName());
                Department department = company.getDepartment(salaryTextField.getText());
                if (department != null) {
                    company.move(selectedEmployee, department);
                    createTree(Application.getInstance().getCompanies(),
                            Application.getInstance().getUsers());
                }
                salaryTextField.setText("");
            }
        });
        adminTreePanel.setPreferredSize(new Dimension(600, 400));
        adminTreePanel.add(treeScroll, BorderLayout.CENTER);
        adminTreePanel.add(salaryTextField, BorderLayout.PAGE_END);
        signOutPanel = new JPanel();

        try {
            ImageIcon adminTreeIcon = new ImageIcon(ImageIO.read(new File("./res/icons/adminTree.png")));
            insertTab(adminTreePanel, 0, adminTreeIcon);
        } catch (IOException e) {
            Log.print("./res/icons/adminTree.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(adminTreePanel, 0, "AdminTree");
        }

        try {
            ImageIcon signOutIcon = new ImageIcon(ImageIO.read(new File("./res/icons/sign-out.png")));
            insertTab(signOutPanel, 1, signOutIcon);
        } catch (IOException e) {
            Log.print("./res/icons/sign-out.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(signOutPanel, 1, "sign-out");
        }

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == signOutPanel) {
                GUI.getInstance().changeConsumer(null);
                GUI.getInstance().changePage(GUI.getInstance().getLoginPage());
            }
        });
        add(tabbedPane);
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
    }

    public void createTree(ArrayList<Company> companies, ArrayList<User> users) {
        root.removeAllChildren();
        DefaultMutableTreeNode companiesNode = new DefaultMutableTreeNode("Companies");
        DefaultMutableTreeNode usersNode = new DefaultMutableTreeNode("Users");

        root.add(companiesNode);
        root.add(usersNode);
        for (Company comp : companies) {
            DefaultMutableTreeNode company = new DefaultMutableTreeNode(comp);
            companiesNode.add(company);
            for (Department dep : comp.getDepartments()) {
                DefaultMutableTreeNode department = new DefaultMutableTreeNode(dep);
                company.add(department);
                DefaultMutableTreeNode employees = new DefaultMutableTreeNode("Employees");
                DefaultMutableTreeNode jobs = new DefaultMutableTreeNode("Jobs");
                department.add(employees);
                department.add(jobs);
                for (Employee emp : dep.getEmployees()) {
                    DefaultMutableTreeNode employee = new DefaultMutableTreeNode(emp);
                    employees.add(employee);
                }
                for (Job jo : dep.getJobs()) {
                    DefaultMutableTreeNode job = new DefaultMutableTreeNode(jo);
                    jobs.add(job);
                }
            }
        }
        for (User user : users) {
            DefaultMutableTreeNode us = new DefaultMutableTreeNode(user);
            usersNode.add(us);
        }
        adminTree.expandRow(0);
        DefaultTreeModel model = (DefaultTreeModel) adminTree.getModel();
        model.reload();
    }

    public void insertTab(Component component, int index, ImageIcon icon) {
        tabbedPane.insertTab(null, icon, component, null, index);
    }

    public void insertTab(Component component, int index, String title) {
        tabbedPane.insertTab(title, null, component, null, index);
    }

    @Override
    void reset() {
        tabbedPane.setSelectedComponent(adminTreePanel);
        salaryTextField.setText("");
        setFocusCycleRoot(true);
    }

    @Override
    JButton getEnterButton() {
        return null;
    }


    @Override
    public String getPageName() {
        return "ADMIN_PAGE";
    }
}
