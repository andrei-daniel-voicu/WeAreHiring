import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SearchButton extends JButton implements Command {
    Mediator mediator;

    public SearchButton(Mediator mediator) {
        this.mediator = mediator;

        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File("./res/icons/searchButton.png")));
            setIcon(icon);
        } catch (IOException e) {
            Log.print("./res/icons/searchButton.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
            setText("Search");
        }

        addActionListener(mediator);
    }

    @Override
    public void execute() {
        mediator.searchButtonPressed();
    }
}
