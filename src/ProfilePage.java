import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class ProfilePage extends Page {
    private final JTabbedPane tabbedPane;

    private final JPanel profilePanel;
    private final JTextArea profileInfo;

    private final JTextField searchField;
    private final JTextArea searchInfo;
    private final SearchButton searchButton;

    private final JPanel signOutPanel;

    public ProfilePage(Mediator mediator) {
        super(mediator);
        tabbedPane = new JTabbedPane();

        profilePanel = new JPanel();
        profileInfo = new JTextArea(30, 30);
        JScrollPane profileScroll = new JScrollPane(profileInfo);
        profileInfo.setEditable(false);
        profilePanel.add(profileScroll);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField(30);
        searchInfo = new JTextArea(30, 60);
        searchInfo.setEditable(false);
        JScrollPane searchScroll = new JScrollPane(searchInfo);
        searchButton = new SearchButton(mediator);
        searchPanel.add(searchField, BorderLayout.LINE_START);
        searchPanel.add(searchButton, BorderLayout.CENTER);
        searchPanel.add(searchScroll, BorderLayout.PAGE_END);

        signOutPanel = new JPanel();

        try {
            ImageIcon profileIcon = new ImageIcon(ImageIO.read(new File("./res/icons/profile.png")));
            insertTab(profilePanel, 0, profileIcon);
        } catch (IOException e) {
            Log.print("./res/icons/profile.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(profilePanel, 0, "Profile");
        }

        try {
            ImageIcon searchIcon = new ImageIcon(ImageIO.read(new File("./res/icons/search.png")));
            insertTab(searchPanel, 1, searchIcon);
        } catch (IOException e) {
            Log.print("./res/icons/search.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(searchPanel, 1, "Search");
        }

        try {
            ImageIcon signOutIcon = new ImageIcon(ImageIO.read(new File("./res/icons/sign-out.png")));
            insertTab(signOutPanel, 2, signOutIcon);
        } catch (IOException e) {
            Log.print("./res/icons/sign-out.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(signOutPanel, 2, "sign-out");
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

    public void setProfileInfoText(String info) {
        profileInfo.setText(info);
    }

    public String getSearchFieldText() {
        return searchField.getText();
    }

    public void setSearchFieldText(String text) {
        searchField.setText(text);
    }

    public void setSearchInfoText(String text) {
        searchInfo.setText(text);
    }

    public void insertTab(Component component, int index, ImageIcon icon) {
        tabbedPane.insertTab(null, icon, component, null, index);
    }

    public void insertTab(Component component, int index, String title) {
        tabbedPane.insertTab(title, null, component, null, index);
    }

    @Override
    public void reset() {
        searchField.setText("");
        searchInfo.setText("");
        tabbedPane.setSelectedComponent(profilePanel);
        setFocusCycleRoot(true);
    }

    @Override
    public JButton getEnterButton() {
        return searchButton;
    }
}
