import javax.swing.*;
import java.awt.*;

class RequestListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
        Request<Job, Consumer> request = (Request<Job, Consumer>) value;
        Job job = request.getKey();
        Consumer user = request.getValue1();
        Consumer recruiter = request.getValue2();

        String text = "<Job: " + job.getName() + ", User: " + user.getResume().getInfo().getName() +
                ", Recruiter: " + recruiter.getResume().getInfo().getName() + ">";
        setText(text);
        return this;
    }

}