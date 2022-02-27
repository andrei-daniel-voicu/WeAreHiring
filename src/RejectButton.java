import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class RejectButton extends JButton implements Command {
    Mediator mediator;

    public RejectButton(Mediator mediator) {
        this.mediator = mediator;

        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File("./res/icons/reject.png")));
            setIcon(icon);
        } catch (IOException e) {
            Log.print("./res/icons/reject.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            setText("Reject");
        }

        addActionListener(mediator);
    }

    @Override
    public void execute() {
        mediator.rejectButtonPressed();
    }
}
