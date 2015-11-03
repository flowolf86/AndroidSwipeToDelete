
package com.flowolf86.androidswipetodelete.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flowolf86.androidswipetodelete.R;
import com.flowolf86.androidswipetodelete.swipetodelete.SwipeToDeleteCompatibleAdapter;
import com.flowolf86.androidswipetodelete.swipetodelete.SwipeToDeleteCompatibleInterface;
import com.flowolf86.androidswipetodelete.util.GuiUtils;
import com.flowolf86.androidswipetodelete.view.SwipeableRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.cesarferreira.rxpeople.models.FakeUser;

/**
 * Created by github.com/flowolf86/ on 20.07.15
 *
 * A swipe to delete compatible adapter implementation
 */
public class SimpleListAdapter extends SwipeToDeleteCompatibleAdapter {

    private List<FakeUser> mData;
    private SwipeToDeleteCompatibleInterface mSelectionCallback = null;

    public SimpleListAdapter(@Nullable final List<FakeUser> people, @NonNull final SwipeToDeleteCompatibleInterface callback) {

        this.mData = people== null ? new ArrayList<FakeUser>(0) : people;
        this.mSelectionCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_person, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setItemData(holder, mData.get(position));
    }

    private void setItemData(final RecyclerView.ViewHolder viewHolder, final FakeUser person) {

        ((ViewHolder)viewHolder).personTitle.setText(person.getFullName());
        ((ViewHolder)viewHolder).personSubtitle.setText(person.getEmail());
        setupImage(((ViewHolder) viewHolder).personImage, person.getPicture().thumbnail);
    }

    /**
     * Load list item image using picasso
     *
     * @param image
     * @param imageUrl
     */
    private void setupImage(@NonNull final ImageView image, @NonNull final String imageUrl) {

        image.setImageBitmap(null);
        Picasso.with(image.getContext()).cancelRequest(image);
        Picasso.with(image.getContext()).load(imageUrl).into(image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    public List<?> getData(){
        return mData;
    }

    /**
     * adds the data at an adjusted index. This is important because the absolute index can change depending
     * on how many items we swipe to delete. If the stored index is bigger than the size of our current
     * data list, we set it to max, therefore adding the item at the end of the list
     *
     * @param index
     * @param data
     * @return
     */
    public boolean addData(int index, @NonNull Object data) {

        if(data instanceof FakeUser){

            int adjustedIndex = index < mData.size() ? index : mData.size();
            if(adjustedIndex < 0){
                adjustedIndex = 0;
            }

            mData.add(adjustedIndex, (FakeUser) data);
            notifyItemInserted(adjustedIndex);
            return true;
        }
        return false;
    }

    public int getDataSize() {
        return mData.size();
    }

    /**
     * update the data contained within this adapter
     */
    public void swapData(@Nullable final List<FakeUser> objects) {

        this.mData = objects == null ? new ArrayList<FakeUser>(0) : objects;
        this.notifyDataSetChanged();
    }

    /**
     * adapt to the new height of the list after we swiped or added an element
     * @param context
     * @param view
     */
    public void adaptHeight(@NonNull Context context, @NonNull RecyclerView view) {
        view.getLayoutParams().height = GuiUtils.calculateScrollableHeightDependOnChildren(context, getDataSize(), R.dimen.list_item_height);
    }

    /**
     * View holder pattern for person
     */
    final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SwipeableRecyclerView.SwipeableViewHolder {

        @Bind(R.id.person_title) TextView personTitle;
        @Bind(R.id.person_subtitle) TextView personSubtitle;
        @Bind(R.id.person_avatar) ImageView personImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSelectionCallback.onElementSelected(v, mData.get(getLayoutPosition()));
        }
    }
}

