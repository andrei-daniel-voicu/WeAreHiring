import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class JobApplyButton extends JButton implements Command {
    Mediator mediator;

    public JobApplyButton(Mediator mediator) {
        this.mediator = mediator;

        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File("./res/icons/apply.png")));
            setIcon(icon);
        } catch (IOException e) {
            Log.print("./res/icons/apply.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            setText("Apply");
        }

        addActionListener(mediator);
        setVisible(false);
    }

    @Override
    public void execute() {
        mediator.jobApplyButtonPressed();
    }
}
