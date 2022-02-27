import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ManagerPage extends EmployeePage {
    private final JList<Request<Job, Consumer>> requestList;
    private final DefaultListModel<Request<Job, Consumer>> requestListModel;
    private final HireButton hireButton;
    private final RejectButton rejectButton;

    private final DefaultListModel<Recruiter> leaderboardListModel;

    public ManagerPage(Mediator mediator) {
        super(mediator);

        JPanel requestPanel = new JPanel(new BorderLayout());
        requestListModel = new DefaultListModel<>();
        requestList = new JList<>(requestListModel);
        requestList.setCellRenderer(new RequestListCellRenderer());
        JScrollPane requestScroll = new JScrollPane(requestList);
        hireButton = new HireButton(mediator);
        rejectButton = new RejectButton(mediator);
        requestPanel.add(requestScroll, BorderLayout.CENTER);
        requestPanel.add(hireButton, BorderLayout.WEST);
        requestPanel.add(rejectButton, BorderLayout.EAST);

        JPanel leaderboardPanel = new JPanel();
        leaderboardListModel = new DefaultListModel<>();
        JList<Recruiter> leaderboardList = new JList<>(leaderboardListModel);
        leaderboardList.setCellRenderer(new RecruiterListCellRenderer());
        JScrollPane leaderboardScroll = new JScrollPane(leaderboardList);
        leaderboardPanel.add(leaderboardScroll);

        try {
            ImageIcon requestIcon = new ImageIcon(ImageIO.read(new File("./res/icons/requests.png")));
            insertTab(requestPanel, 1, requestIcon);
        } catch (IOException e) {
            Log.print("./res/icons/requests.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(requestPanel, 1, "Requests");
        }

        try {
            ImageIcon leaderboardIcon = new ImageIcon(ImageIO.read(new File("./res/icons/leaderboard.png")));
            insertTab(leaderboardPanel, 3, leaderboardIcon);
        } catch (IOException e) {
            Log.print("./res/icons/leaderboard.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(leaderboardPanel, 3, "Leaderboard");
        }
    }

    public void setRequestList(ArrayList<Request<Job, Consumer>> requests) {
        requestListModel.clear();
        hireButton.setVisible(false);
        rejectButton.setVisible(false);
        if (requests != null) {
            requestListModel.addAll(requests);
            hireButton.setVisible(requests.size() != 0);
            rejectButton.setVisible(requests.size() != 0);
        }
    }

    public void setRecruiterList(ArrayList<Recruiter> recruiters) {
        leaderboardListModel.clear();

        leaderboardListModel.addAll(recruiters.stream().sorted(Comparator.comparing(Recruiter::getRating).reversed()).
                collect(Collectors.toList()));
    }

    public Request<Job, Consumer> getSelectedRequest() {
        return requestList.getSelectedValue();
    }

    @Override
    public String getPageName() {
        return "MANAGER_PAGE";
    }

    @Override
    public void reset() {
        super.reset();
        requestList.setSelectedIndex(0);
    }
}
