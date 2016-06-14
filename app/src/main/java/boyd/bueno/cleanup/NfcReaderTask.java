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

    private RecyclerView.Adapter adapter;
    private ArrayList<Task> data;

    public NfcReaderTask(ArrayList<Task> data, RecyclerView.Adapter adapter) {
        this.data = data;
        this.adapter = adapter;
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
                    data.clear();
                    data.add(new Task("Deur 1 afdoen", "Denk aan de deurklink, beide kanten", 1));
                    data.add(new Task("Stoel + tafel 1 schoonmaken", 2));
                    data.add(new Task("Stoel + tafel 2 schoonmaken", 3));
                    data.add(new Task("Stoel + tafel 3 schoonmaken", 4));
                    data.add(new Task("Stoel + tafel 4 schoonmaken", 5));
                    data.add(new Task("Stoel + tafel 5 schoonmaken", 6));
                    data.add(new Task("Deur 2 afdoen", 7));
                    adapter.notifyDataSetChanged();
                    break;
                case 2000:
                    data.clear();
                    data.add(new Task("Deur 1 afdoen", "Denk aan de deurklink", 1));
                    data.add(new Task("Stoel + tafel 1 schoonmaken", 2));
                    data.add(new Task("Stoel + tafel 2 schoonmaken", 3));
                    data.add(new Task("Stoel + tafel 3 schoonmaken", 4));
                    data.add(new Task("Stoel + tafel 4 schoonmaken", 5));
                    data.add(new Task("Raam links schoonmaken", 6));
                    data.add(new Task("Deur 2 afdoen", 7));
                    data.add(new Task("Raam rechts schoonmaken", 8));
                    data.add(new Task("Stoel + tafel 5 schoonmaken", 9));
                    data.add(new Task("Stoel + tafel 6 schoonmaken", 10));
                    data.add(new Task("Stoel + tafel 7 schoonmaken", 11));
                    data.add(new Task("Deur 3 afdoen", 12));
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    Task taskToRemove = null;
                    for (Task task : data) {
                        if (task.id == id) {
                            taskToRemove = task;
                            Log.e("test", "Task to remove: " + taskToRemove.toString());
                        }
                    }
                    if (data.indexOf(taskToRemove) != -1) {
                        int indexOf = data.indexOf(taskToRemove);
                        data.remove(indexOf);

                        adapter.notifyItemRemoved(indexOf);
                        Log.e("test", String.valueOf(taskToRemove.toString()));
                    }

            }
            Log.e("test", "scanned tag with id " + id);
        }
    }
}