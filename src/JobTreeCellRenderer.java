import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class JobTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel,
                                                  final boolean expanded, final boolean leaf,
                                                  final int row, final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = ((DefaultMutableTreeNode) (value));
        switch (node.getLevel()) {
            case 1 -> {
                Company company = ((Company) node.getUserObject());
                String string = company.getName();
                setText(string);
            }
            case 2 -> {
                Department dep = (Department) node.getUserObject();
                String string = DepartmentFactory.getDepartmentName(dep);
                setText(string);
            }
            case 3 -> {
                Job job = (Job) node.getUserObject();
                String string = job.getName();
                setText(string);
            }
        }
        if (node.getLevel() == 3) {
            Job job = (Job) node.getUserObject();
            if (!job.meetsRequirements((User) GUI.getInstance().getCurrentConsumer())) {
                setBorderSelectionColor(Color.RED);
            } else {
                setBorderSelectionColor(Color.GREEN);

            }
        } else {
            setBorderSelectionColor(Color.BLACK);
        }
        return this;
    }
}
