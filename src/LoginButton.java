import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LoginButton extends JButton implements Command {
    Mediator mediator;
    private ImageIcon loginIcon;
    private ImageIcon loginIconRed;

    public LoginButton(Mediator mediator) {
        this.mediator = mediator;

        try {
            loginIcon = new ImageIcon(ImageIO.read(new File("./res/icons/sign-in.png")));
        } catch (IOException e) {
            Log.print("./res/icons/sign-in.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
        }

        try {
            loginIconRed = new ImageIcon(ImageIO.read(new File("./res/icons/sign-in-red.png")));
        } catch (IOException e) {
            Log.print("./res/icons/sign-in-red.png not found! Fallback to text", Log.MessageType.ERROR, Log.Color.RED);
        }

        addActionListener(mediator);
    }

    public void setErrorIcon() {
        if (loginIconRed == null) {
            setIcon(null);
            setBackground(Color.RED);
            setText("sign-in");
        } else {
            setIcon(loginIconRed);
            setText(null);
        }
    }

    public void resetIcon() {
        if (loginIcon == null) {
            setIcon(null);
            setBackground(null);
            setText("sign-in");
        } else {
            setIcon(loginIcon);
            setBackground(null);
            setText(null);
        }
    }

    @Override
    public void execute() {
        mediator.loginButtonPressed();
    }
}
