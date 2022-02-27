import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class AdminTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel,
                                                  final boolean expanded, final boolean leaf,
                                                  final int row, final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = ((DefaultMutableTreeNode) (value));
        Object obj = node.getUserObject();
        switch (node.getLevel()) {
            case 2 -> {
                if (obj instanceof User) {
                    setText(((User) (obj)).getResume().getInfo().getName());
                } else {
                    setText(((Company) (obj)).getName());
                }
            }
            case 3 -> {
                String text = DepartmentFactory.getDepartmentName((Department) obj);
                setText(text);
            }
            case 5 -> {
                if (obj instanceof Employee) {
                    setText(((Employee) obj).getResume().getInfo().getName());
                } else {
                    setText(((Job) obj).getName());
                }
            }
        }
        return this;
    }
}
