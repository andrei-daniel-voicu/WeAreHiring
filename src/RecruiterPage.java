import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecruiterPage extends EmployeePage {
    private final JTree friendsTree;
    private final DefaultMutableTreeNode root;

    public RecruiterPage(Mediator mediator) {
        super(mediator);

        JPanel friendsPanel = new JPanel(new BorderLayout());
        root = new DefaultMutableTreeNode("Friends");
        friendsTree = new JTree(root);
        JScrollPane friendsTreeScroll = new JScrollPane(friendsTree);
        friendsPanel.add(friendsTreeScroll, BorderLayout.CENTER);

        try {
            ImageIcon friendsIcon = new ImageIcon(ImageIO.read(new File("./res/icons/friends.png")));
            insertTab(friendsPanel, 2, friendsIcon);
        } catch (IOException e) {
            Log.print("./res/icons/friends.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            insertTab(friendsPanel, 2, "Friends");
        }
    }

    public void createTree(Recruiter recruiter) {
        root.removeAllChildren();
        ArrayList<Consumer> neighborsQ = new ArrayList<>();
        ArrayList<Consumer> visited = new ArrayList<>();
        ArrayList<DefaultMutableTreeNode> parents = new ArrayList<>();
        neighborsQ.add(recruiter);
        visited.add(recruiter);
        parents.add(root);
        while (neighborsQ.size() != 0) {
            Consumer current = neighborsQ.remove(0);
            DefaultMutableTreeNode rootNode = parents.remove(0);
            for (Consumer neighbor : current.getRelatives()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    neighborsQ.add(neighbor);
                    DefaultMutableTreeNode neighborNode = new DefaultMutableTreeNode(neighbor.getResume()
                            .getInfo().getName());
                    rootNode.add(neighborNode);
                    parents.add(neighborNode);
                }
            }
        }
        friendsTree.expandRow(0);
        DefaultTreeModel model = (DefaultTreeModel) friendsTree.getModel();
        model.reload();
    }

    @Override
    public String getPageName() {
        return "RECRUITER_PAGE";
    }
}
