import java.util.Objects;

public class Credential {
    private Consumer consumer;
    private final String username;
    private final String password;

    public Credential(Consumer consumer, String username, String password) {
        this.consumer = consumer;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credential)) return false;
        Credential that = (Credential) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
