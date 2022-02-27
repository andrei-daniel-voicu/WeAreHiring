import javax.swing.*;
import java.awt.*;

public class LoginPage extends Page {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final LoginButton loginButton;

    public LoginPage(Mediator mediator) {
        super(mediator);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new LoginButton(mediator);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        add(loginButton, gbc);

        setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                if (aComponent == passwordField) {
                    return usernameField;
                }
                return passwordField;
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                if (aComponent == passwordField) {
                    return usernameField;
                }
                return passwordField;
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return usernameField;
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return passwordField;
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return usernameField;
            }
        });
    }

    public String getUsernameFieldText() {
        return usernameField.getText();
    }

    public void setUsernameFieldText(String text) {
        usernameField.setText(text);
    }

    public String getPasswordFieldText() {
        return new String(passwordField.getPassword());
    }

    public void setPasswordFieldText(String text) {
        passwordField.setText(text);
    }

    public void setErrorLoginIcon() {
        loginButton.setErrorIcon();
    }

    @Override
    public void reset() {
        setUsernameFieldText("");
        setPasswordFieldText("");
        loginButton.resetIcon();
        setFocusCycleRoot(true);
        usernameField.requestFocus();
    }

    @Override
    public JButton getEnterButton() {
        return loginButton;
    }

    @Override
    public String getPageName() {
        return "LOGIN_PAGE";
    }
}
