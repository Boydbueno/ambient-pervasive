package boyd.bueno.cleanup;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Background task for reading the data.
 */
class NfcReaderTask extends AsyncTask<Tag, Void, String> {

    private ArrayList<Task> tasks;
    private RecyclerView.Adapter taskAdapter;

    private ArrayList<Protocol> protocols;
    private RecyclerView.Adapter protocolsAdapter;

    public NfcReaderTask() {
        this.tasks = Storage.getInstance().tasks;
        this.protocols = Storage.getInstance().protocols;

        this.taskAdapter = Storage.getInstance().taskAdapter;
        this.protocolsAdapter = Storage.getInstance().protocolAdapter;
    }

    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e("test", "Unsupported Encoding", e);
                }
            }
        }

        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {
        int id = 0;
        try {
            id = new JSONObject(result).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result != null) {
            switch(id) {
                case 1000:
                    tasks.clear();
                    tasks.add(new Task("Deur 1 afdoen", "Denk aan de deurklink, beide kanten", 1));
                    tasks.add(new Task("Stoel + tafel 1 schoonmaken", 2));
                    tasks.add(new Task("Stoel + tafel 2 schoonmaken", 3));
                    tasks.add(new Task("Stoel + tafel 3 schoonmaken", 4));
                    tasks.add(new Task("Linker raam schoonmaken", "Inclusief kozijnen", 5));
                    tasks.add(new Task("Rechter raam schoonmaken", "Inclusief kozijnen", 6));
                    tasks.add(new Task("Stoel + tafel 4 schoonmaken", 7));
                    tasks.add(new Task("Stoel + tafel 5 schoonmaken", 8));
                    tasks.add(new Task("Deur 2 afdoen", 9));

                    if (taskAdapter != null) {
                        taskAdapter.notifyDataSetChanged();
                    }

                    protocols.clear();
                    protocols.add(new Protocol("8.1 Reinigen"));
                    protocols.add(new Protocol("8.3 Stofzuigen"));
                    protocols.add(new Protocol("8.4 Desinfecteren"));
                    protocols.add(new Protocol("9.3 Emmers"));
                    protocols.add(new Protocol("9.4 Materiaalwagen"));
                    protocols.add(new Protocol("9.5 Moppen"));
                    protocols.add(new Protocol("9.9 Vaatdoeken"));

                    if (protocolsAdapter != null) {
                        protocolsAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2000:
                    tasks.clear();
                    tasks.add(new Task("Deur 1 afdoen", "Denk aan de deurklink", 1));
                    tasks.add(new Task("Stoel + tafel 1 schoonmaken", 2));
                    tasks.add(new Task("Stoel + tafel 2 schoonmaken", 3));
                    tasks.add(new Task("Stoel + tafel 3 schoonmaken", 4));
                    tasks.add(new Task("Stoel + tafel 4 schoonmaken", 5));
                    tasks.add(new Task("Raam links schoonmaken", 6));
                    tasks.add(new Task("Deur 2 afdoen", 7));
                    tasks.add(new Task("Raam rechts schoonmaken", 8));
                    tasks.add(new Task("Stoel + tafel 5 schoonmaken", 9));
                    tasks.add(new Task("Stoel + tafel 6 schoonmaken", 10));
                    tasks.add(new Task("Stoel + tafel 7 schoonmaken", 11));
                    tasks.add(new Task("Deur 3 afdoen", 12));

                    if (taskAdapter != null) {
                        taskAdapter.notifyDataSetChanged();
                    }

                    protocols.clear();
                    protocols.add(new Protocol("8.1 Reinigen"));
                    protocols.add(new Protocol("8.2 Sanitair reinigen"));
                    protocols.add(new Protocol("8.3 Stofzuigen"));
                    protocols.add(new Protocol("8.4 Desinfecteren"));
                    protocols.add(new Protocol("9.3 Emmers"));
                    protocols.add(new Protocol("9.4 Materiaalwagen"));
                    protocols.add(new Protocol("9.5 Moppen"));
                    protocols.add(new Protocol("9.6 Schrobautomaat / zuigmachine"));
                    protocols.add(new Protocol("9.9 Vaatdoeken"));

                    if (protocolsAdapter != null) {
                        protocolsAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    Task taskToRemove = null;
                    for (Task task : tasks) {
                        if (task.id == id) {
                            taskToRemove = task;
                            Log.e("test", "Task to remove: " + taskToRemove.toString());
                        }
                    }
                    if (tasks.indexOf(taskToRemove) != -1) {
                        int indexOf = tasks.indexOf(taskToRemove);
                        tasks.remove(indexOf);

                        taskAdapter.notifyItemRemoved(indexOf);
                        Log.e("test", String.valueOf(taskToRemove.toString()));
                    }

            }
            Log.e("test", "scanned tag with id " + id);
        }
    }
}