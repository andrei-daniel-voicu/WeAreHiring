import javax.swing.*;
import java.awt.*;

class RecruiterListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
        Recruiter recruiter = (Recruiter) value;

        String text = (index + 1) + ". " + recruiter.getResume().getInfo().getName() +
                ", " + recruiter.getRating();
        setText(text);
        return this;
    }

}