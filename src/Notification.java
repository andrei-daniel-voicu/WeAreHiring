public class Notification {
    public String message;

    public Notification(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
