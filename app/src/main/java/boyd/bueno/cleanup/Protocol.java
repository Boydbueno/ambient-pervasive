package boyd.bueno.cleanup;

public class Protocol {
    String text;

    public Protocol() {}

    public Protocol(String text) {
        this.text = text;
    }

    @Override
    public String toString() { return text; }
}
