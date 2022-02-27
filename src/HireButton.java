import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class HireButton extends JButton implements Command {
    Mediator mediator;

    public HireButton(Mediator mediator) {
        this.mediator = mediator;

        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File("./res/icons/hire.png")));
            setIcon(icon);
        } catch (IOException e) {
            Log.print("./res/icons/hire.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            setText("Hire");
        }

        addActionListener(mediator);
    }

    @Override
    public void execute() {
        mediator.hireButtonPressed();
    }
}
