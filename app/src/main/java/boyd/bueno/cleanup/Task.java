package boyd.bueno.cleanup;

public class Task {
    String label;
    String note;
    int id;

    public Task() {}

    public Task(String label, int id) {
        this.label = label;
        this.id = id;
    }

    public Task(String label, String note, int id) {
        this.label = label;
        this.id = id;
        this.note = note;
    }

    @Override
    public String toString() { return label; }
}
