import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserPage extends ProfilePage {
    private final DefaultListModel<Notification> notificationListModel;

    private final JTree jobTree;
    private final DefaultMutableTreeNode root;
    private final JTextArea jobInfo;
    private final JobApplyButton applyButton;

    public UserPage(Mediator mediator) {
        super(mediator);
        JPanel notificationPanel = new JPanel();
        notificationListModel = new DefaultListModel<>();
        JList<Notification> notificationList = new JList<>(notificationListModel);
        JScrollPane notificationScroll = new JScrollPane(notificationList);
        notificationPanel.add(notificationScroll);

        JPanel jobPanel = new JPanel(new BorderLayout());
        root = new DefaultMutableTreeNode("Jobs");
        jobTree = new JTree(root);
        JScrollPane jobScroll = new JScrollPane(jobTree);
        jobTree.setCellRenderer(new JobTreeCellRenderer());
        jobInfo = new JTextArea(10, 30);
        jobInfo.setEditable(false);
        JScrollPane jobInfoScroll = new JScrollPane(jobInfo);
        applyButton = new JobApplyButton(mediator);
        jobPanel.add(jobScroll, BorderLayout.CENTER);
        jobPanel.add(jobInfoScroll, BorderLayout.PAGE_END);
        jobPanel.add(applyButton, BorderLayout.EAST);
        jobTree.addTreeSelectionListener(e -> {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jobTree.getLastSelectedPathComponent();
                    if (node != null)
                        if (node.getLevel() == 3) {
                            Job job = (Job) node.getUserObject();
                            jobInfo.setText(job.toString());
                            applyButton.setVisible(job.meetsRequirements((User) GUI.getInstance().getCurrentConsumer()));
                            repaint();
                        } else
                            jobInfo.setText("");
                    else {
                        jobInfo.setText("");
                        applyButton.setVisible(false);
                    }
                }
        );

        try {
            ImageIcon notificationIcon = new ImageIcon(ImageIO.read(new File("./res/icons/notifications.png")));
            insertTab(notificationPanel, 1, notificationIcon);
        } catch (IOException e) {
            Log.print("./res/icons/notifications.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(notificationPanel, 1, "Notifications");
        }

        try {
            ImageIcon jobIcon = new ImageIcon(ImageIO.read(new File("./res/icons/jobs.png")));
            insertTab(jobPanel, 2, jobIcon);
        } catch (IOException e) {
            Log.print("./res/icons/jobs.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(jobPanel, 2, "Jobs");
        }

    }

    public void setNotificationList(ArrayList<Notification> notifications) {
        notificationListModel.clear();
        if (notifications != null)
            notificationListModel.addAll(notifications);
    }

    public void createTree(ArrayList<String> companies) {
        root.removeAllChildren();
        User currUser = (User) GUI.getInstance().getCurrentConsumer();
        for (String companyName : companies) {
            Company company = Application.getInstance().getCompany(companyName);
            DefaultMutableTreeNode comp = new DefaultMutableTreeNode(company);
            root.add(comp);
            for (Department department : company.getDepartments()) {
                DefaultMutableTreeNode dep = new DefaultMutableTreeNode(department);
                comp.add(dep);
                for (Job job : department.getJobs()) {
                    DefaultMutableTreeNode jo = new DefaultMutableTreeNode(job);
                    if (!job.getCandidates().contains(currUser)) {
                        dep.add(jo);
                    }
                }
            }
        }
        jobTree.expandRow(0);
        DefaultTreeModel model = (DefaultTreeModel) jobTree.getModel();
        model.reload();
    }

    public Job getSelectedJob() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jobTree.getLastSelectedPathComponent();
        if (node.getLevel() == 3) {
            return (Job) (node.getUserObject());
        }
        return null;
    }

    @Override
    public String getPageName() {
        return "USER_PAGE";
    }
}
