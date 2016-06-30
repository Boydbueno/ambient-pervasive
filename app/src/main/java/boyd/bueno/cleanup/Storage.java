package boyd.bueno.cleanup;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class Storage {
    public ArrayList<Task> tasks = new ArrayList<>();
    public RecyclerView.Adapter taskAdapter;

    public ArrayList<Protocol> protocols = new ArrayList<>();
    public RecyclerView.Adapter protocolAdapter;

    private static Storage instance = null;
    protected Storage() {}

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }

        return instance;
    }
}
