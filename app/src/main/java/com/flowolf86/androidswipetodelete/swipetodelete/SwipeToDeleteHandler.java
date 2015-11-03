package com.flowolf86.androidswipetodelete.swipetodelete;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.flowolf86.androidswipetodelete.util.GuiUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by github.com/flowolf86/ on 20.07.15
 *
 * A handler handling swipe to delete UNDO actions. This is not trivial
 * because of the ability to swipe multiple list elements and restore them
 * according to their original position in the recyclerview
 */
public class SwipeToDeleteHandler {

    public static final int USER_UNDO_TIMEFRAME = 4000;

    private Snackbar mCurrentActiveSnackbar = null;
    private Context mContext = null;
    private View mRootView = null;
    private Handler mUiHandler = null;
    private RecyclerView mRecyclerView = null;

    // Required stuff
    private SwipeToDeleteCompatibleInterface mInterface = null;
    private SwipeToDeleteCompatibleAdapter mAdapter = null;

    // Handler callback
    private final Runnable mHandlerCallback = new Runnable() {
        @Override
        public void run() {

            // Copy list to ensure multithreaded safety
            TreeMap<Integer, Object> mDeleteQueueCopy = new TreeMap<>(mDeleteQueue);

            mInterface.onMultipleElementsSwiped(mDeleteQueueCopy);
            setUpDeleteQueue();

            if(mCurrentActiveSnackbar != null){
                mCurrentActiveSnackbar.dismiss();
                mCurrentActiveSnackbar = null;
            }
        }
    };

    /**
     * The queue with trips that have to be deleted if the user swipes more than one trip while the other can still be undone
     *
     * key = position in list
     * value = data of list item at position key
     *
     */
    private ConcurrentSkipListMap<Integer, Object> mDeleteQueue = new ConcurrentSkipListMap<>();
    private ConcurrentSkipListMap<Integer, Object> mRestoreQueue = new ConcurrentSkipListMap<>();

    public <T extends SwipeToDeleteCompatibleAdapter> SwipeToDeleteHandler(@NonNull Context context, @NonNull View view, @NonNull Handler uiHandler,
                                                                           @NonNull T adapter, @NonNull SwipeToDeleteCompatibleInterface callback,
                                                                           @NonNull RecyclerView recyclerView) {
        mContext = context;
        mRootView = view;
        mUiHandler = uiHandler;
        mAdapter = adapter;
        mInterface = callback;
        mRecyclerView = recyclerView;
    }

    public void addDelete(final int currentPosition, final Object value){

        // We can not use the current position as key in the list here
        // because a new item may get the same position after the swipe
        // e.g. position 0. Therefore we have to use the key from the
        // restore map to store as key in the delete queue too
        int restoreIndex = getInitialListPosition(value);
        mDeleteQueue.put(restoreIndex, value);

        notifyUser(mRootView);

        mAdapter.getData().remove(value);
        mAdapter.notifyItemRemoved(currentPosition);
        mAdapter.adaptHeight(mContext, mRecyclerView);
    }

    public void setup(@Nullable List<?> data){
        setUpDeleteQueue();
        setUpRestoreQueue(data);
    }

    public void reset(@Nullable List<?> data){
        setup(data);
        mCurrentActiveSnackbar = null;
    }

    public void finish(){

        mInterface.onMultipleElementsSwiped(mDeleteQueue);

        // Hide snackbar so that it does not show up again if we quickly navigate to the fragment again
        if(mCurrentActiveSnackbar != null){
            mCurrentActiveSnackbar.dismiss();
            mCurrentActiveSnackbar = null;
        }
    }

    /**
     * This is an exact copy of the recyclerviews data with the exception of
     * having the list position as int key stored linked to the data.
     *
     * We need this for restoring the list elements later if the user wants
     * to undo his remove action. After a swipe of one element the others all
     * change position in list leading to potentially duplicated keys and the
     * disability to restore trip properly at their previous position
     */
    private void setUpRestoreQueue(@Nullable final List<?> data) {

        mRestoreQueue.clear();

        if(data != null){
            int i = 0;
            for(Object object : data){
                mRestoreQueue.put(i, object);
                i++;
            }
        }
    }

    /**
     * A simple delete queue setup is just clearing the list
     */
    private void setUpDeleteQueue() {
        mDeleteQueue.clear();
    }

    /**
     * Display a snackbar to the user and give him the chance to undo his actions
     *
     * @param context
     * @param view
     */
    private void notifyUser(final View view) {

        // If this happens within the old snackbar timeframe, remove the old callback and set a new one
        mUiHandler.removeCallbacks(mHandlerCallback);

        // Check if we already have an active snackbar. If yes, extend the duration, if no, show new one
        if(mCurrentActiveSnackbar == null){

            mCurrentActiveSnackbar = GuiUtils.displaySnackbar(view,
                    "Deleted ("+mDeleteQueue.size()+")",
                    "UNDO",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Remove handler callback
                            mUiHandler.removeCallbacks(mHandlerCallback);

                            // Restore the elements in reverse order
                            for (Map.Entry<Integer, Object> entry : mDeleteQueue.entrySet()) {
                                int insertIndex = getInitialListPosition(entry.getValue());
                                mAdapter.addData(insertIndex, entry.getValue());
                            }

                            mAdapter.adaptHeight(mContext, mRecyclerView);
                            setUpDeleteQueue();
                            mCurrentActiveSnackbar = null;
                        }
                    }, GuiUtils.SNACKBAR_NO_DELAY, Snackbar.LENGTH_INDEFINITE);
        } else {

            mCurrentActiveSnackbar.setText("Deleted ("+mDeleteQueue.size()+")");
        }

        mUiHandler.postDelayed(mHandlerCallback, USER_UNDO_TIMEFRAME);
    }

    private int getInitialListPosition(final Object value) {

        int initialIndex = Integer.MAX_VALUE;

        for(Map.Entry<Integer, Object> entry : mRestoreQueue.entrySet()){

            if(value.equals(entry.getValue())){
                initialIndex = entry.getKey();
                break;
            }
        }

        return initialIndex;
    }
}
