package boyd.bueno.cleanup;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class TaskTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TaskAdapter mTaskAdapter;

    public TaskTouchHelper(TaskAdapter taskAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mTaskAdapter = taskAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mTaskAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mTaskAdapter.remove(viewHolder.getAdapterPosition());
    }
}