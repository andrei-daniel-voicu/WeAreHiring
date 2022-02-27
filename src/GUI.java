import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class GUI {
    private static GUI instance;
    private final JFrame frame;
    private final Dimension windowSize;
    private Page currentPage;
    private Consumer currentConsumer;

    private final Container mainPanel;
    private final CardLayout cardLayout;
    private final LoginPage loginPage;
    private final AdminPage adminPage;
    private final EmployeePage employeePage;
    private final UserPage userPage;
    private final ManagerPage managerPage;
    private final RecruiterPage recruiterPage;
    private final Mediator mediator;

    private GUI() {
        Application.getInstance().setWithGUI(true);

        frame = new JFrame("We Are Hiring");
        mainPanel = frame.getContentPane();
        mediator = new Mediator();
        cardLayout = new CardLayout();

        loginPage = new LoginPage(mediator);
        adminPage = new AdminPage(mediator);
        managerPage = new ManagerPage(mediator);
        userPage = new UserPage(mediator);
        employeePage = new EmployeePage(mediator);
        recruiterPage = new RecruiterPage(mediator);

        windowSize = new Dimension(800, 600);
    }

    public static GUI getInstance() {
        if (instance == null)
            instance = new GUI();
        return instance;
    }

    public void OnStart() {
        Log.print("Application started", Log.MessageType.System, Log.Color.PURPLE);

        HashMap<String, String> paths = new HashMap<>();
        paths.put("companies", "./res/application/companies.json");
        paths.put("jobs", "./res/application/jobs.json");
        paths.put("relatives", "./res/application/relatives.json");
        paths.put("consumers", "./res/application/consumers.json");
        Application.getInstance().loadData(paths);
        frame.setPreferredSize(windowSize);

        mainPanel.setLayout(cardLayout);
        mainPanel.add(loginPage, loginPage.getPageName());
        mainPanel.add(userPage, userPage.getPageName());
        mainPanel.add(adminPage, adminPage.getPageName());
        mainPanel.add(employeePage, employeePage.getPageName());
        mainPanel.add(managerPage, managerPage.getPageName());
        mainPanel.add(recruiterPage, recruiterPage.getPageName());

        mediator.setGui(this);
        mediator.setLoginPage(loginPage);
        mediator.setUserPage(userPage);
        mediator.setManagerPage(managerPage);
        mediator.setRecruiterPage(recruiterPage);
        mediator.setEmployeePage(employeePage);
        mediator.setAdminPage(adminPage);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                OnExit();
            }
        });
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        changePage(loginPage);
    }

    public void setEnterButton(JButton button) {
        frame.getRootPane().setDefaultButton(button);
    }

    public void changePage(Page page) {
        cardLayout.show(mainPanel, page.getPageName());
        currentPage = page;
        page.reset();
        setEnterButton(page.getEnterButton());
    }

    public void changeConsumer(Consumer consumer) {
        this.currentConsumer = consumer;
    }

    public Consumer getCurrentConsumer() {
        return currentConsumer;
    }

    public Page getPage() {
        return currentPage;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public void OnExit() {
        Log.print("Exiting application", Log.MessageType.System, Log.Color.PURPLE);

        HashMap<String, String> paths = new HashMap<>();
        paths.put("companies", "./res/application/companies.json");
        paths.put("jobs", "./res/application/jobs.json");
        paths.put("relatives", "./res/application/relatives.json");
        paths.put("consumers", "./res/application/consumers.json");
        Application.getInstance().saveData(paths);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> getInstance().OnStart());
    }
}
