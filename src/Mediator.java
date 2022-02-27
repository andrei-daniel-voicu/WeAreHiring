import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mediator implements ActionListener {
    private GUI gui;
    private LoginPage loginPage;
    private UserPage userPage;
    private ManagerPage managerPage;
    private EmployeePage employeePage;
    private RecruiterPage recruiterPage;
    private AdminPage adminPage;

    public void loginButtonPressed() {
        String username = loginPage.getUsernameFieldText();
        String password = loginPage.getPasswordFieldText();
        if (username.equals("admin") && password.equals("admin")) {
            adminPage.createTree(Application.getInstance().getCompanies(),
                    Application.getInstance().getUsers());
            gui.changePage(adminPage);
            return;
        }
        Consumer consumer = Application.getInstance().getConsumerByCredentials(username,
                password);

        if (consumer != null) {
            gui.changeConsumer(consumer);
            switch (Application.getInstance().getType(consumer)) {
                case "User" -> {
                    User user = (User) consumer;
                    userPage.setProfileInfoText(user.toString());
                    userPage.setNotificationList(user.getNotifications());
                    userPage.createTree(user.getInterestedCompanies());
                    gui.changePage(userPage);
                }
                case "Manager" -> {
                    Manager manager = (Manager) consumer;
                    Company company = Application.getInstance().getCompany(manager.getCompanyName());
                    managerPage.setProfileInfoText(manager.toString());
                    managerPage.setRequestList(manager.getRequests());
                    managerPage.createTree(company.getDepartments());
                    managerPage.setRecruiterList(company.getRecruiters());
                    gui.changePage(managerPage);
                }

                case "Recruiter" -> {
                    Recruiter recruiter = (Recruiter) consumer;
                    Company company = Application.getInstance().getCompany(recruiter.getCompanyName());
                    recruiterPage.setProfileInfoText(recruiter.toString());
                    recruiterPage.createTree(company.getDepartments());
                    recruiterPage.createTree(recruiter);
                    gui.changePage(recruiterPage);
                }
                case "Employee" -> {
                    Employee employee = (Employee) consumer;
                    Company company = Application.getInstance().getCompany(employee.getCompanyName());
                    employeePage.setProfileInfoText(employee.toString());
                    employeePage.createTree(company.getDepartments());
                    gui.changePage(employeePage);
                }
            }
        } else {
            loginPage.setErrorLoginIcon();
            loginPage.setUsernameFieldText("");
            loginPage.setPasswordFieldText("");
        }
    }

    public void searchButtonPressed() {
        ProfilePage profilePage = (ProfilePage) gui.getPage();
        String name = profilePage.getSearchFieldText();
        Consumer consumer = Application.getInstance().getConsumerByName(name);
        if (consumer != null) {
            profilePage.setSearchInfoText(consumer.toString());
        } else {
            profilePage.setSearchInfoText("");
        }
        profilePage.setSearchFieldText("");
    }

    public void jobApplyButtonPressed() {
        Job job = userPage.getSelectedJob();
        User user = (User) gui.getCurrentConsumer();
        job.apply(user);
        userPage.createTree(user.getInterestedCompanies());
    }

    public void hireButtonPressed() {
        Request<Job, Consumer> request = managerPage.getSelectedRequest();
        Company company = Application.getInstance().getCompany(request.getKey().getCompany());
        company.getManager().hireUser(request);
        managerPage.setRequestList(company.getManager().getRequests());
        managerPage.createTree(company.getDepartments());
    }

    public void rejectButtonPressed() {
        Request<Job, Consumer> request = managerPage.getSelectedRequest();
        Company company = Application.getInstance().getCompany(request.getKey().getCompany());
        company.getManager().rejectUser(request);
        managerPage.setRequestList(company.getManager().getRequests());
    }

    public void setLoginPage(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void setUserPage(UserPage userPage) {
        this.userPage = userPage;
    }

    public void setManagerPage(ManagerPage managerPage) {
        this.managerPage = managerPage;
    }

    public void setEmployeePage(EmployeePage employeePage) {
        this.employeePage = employeePage;
    }

    public void setRecruiterPage(RecruiterPage recruiterPage) {
        this.recruiterPage = recruiterPage;
    }

    public void setAdminPage(AdminPage adminPage) {
        this.adminPage = adminPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((Command) e.getSource()).execute();
    }
}
