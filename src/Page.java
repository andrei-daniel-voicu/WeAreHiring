import javax.swing.*;

public abstract class Page extends JPanel {
    Mediator mediator;

    public Page(Mediator mediator) {
        this.mediator = mediator;
    }

    abstract void reset();

    abstract JButton getEnterButton();

    abstract String getPageName();
}