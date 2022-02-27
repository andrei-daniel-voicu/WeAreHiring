import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class EmployeeTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel,
                                                  final boolean expanded, final boolean leaf,
                                                  final int row, final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = ((DefaultMutableTreeNode) (value));
        switch (node.getLevel()) {
            case 1 -> {
                Department dep = (Department) node.getUserObject();
                String string = DepartmentFactory.getDepartmentName(dep);
                setText(string);
            }
            case 2 -> {
                Employee employee = (Employee) node.getUserObject();
                String string = employee.getResume().getInfo().getName();
                setText(string);
            }
        }
        return this;
    }
}
