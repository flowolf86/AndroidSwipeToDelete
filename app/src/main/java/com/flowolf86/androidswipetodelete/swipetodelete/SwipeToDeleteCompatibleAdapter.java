package com.flowolf86.androidswipetodelete.swipetodelete;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by github.com/flowolf86/ on 20.07.15
 *
 * A swipe to delete compatible adapter
 */
public abstract class SwipeToDeleteCompatibleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * get all adapter data
     * @return
     */
    public abstract List<?> getData();

    /**
     * convinience method. get the size of the adapter data
     */
    public abstract int getDataSize();

    /**
     * add data at index to adapter
     * @param index
     * @param data
     * @return
     */
    public abstract boolean addData(int index, @NonNull Object data);

    /**
     * adapt the list height to match the new number of list items
     * @param context
     * @param view
     */
    public abstract void adaptHeight(@NonNull Context context, @NonNull RecyclerView view);
}