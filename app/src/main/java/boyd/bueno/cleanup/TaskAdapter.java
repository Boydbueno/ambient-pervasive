package boyd.bueno.cleanup;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mTextView;
        public ViewHolder(CardView v) {
            super(v);
            mTextView = v;
        }
    }

    public TaskAdapter(ArrayList<Task> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView titleView = (TextView) holder.mTextView.findViewById(R.id.title);
        TextView noteView = (TextView) holder.mTextView.findViewById(R.id.note);
        titleView.setText(mDataset.get(position).toString());
        noteView.setText(mDataset.get(position).note);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void swap(int firstPosition, int secondPosition){
        Collections.swap(mDataset, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }
}
