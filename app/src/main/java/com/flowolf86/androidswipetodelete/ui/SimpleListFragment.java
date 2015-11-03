
package com.flowolf86.androidswipetodelete.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flowolf86.androidswipetodelete.R;
import com.flowolf86.androidswipetodelete.adapter.SimpleListAdapter;
import com.flowolf86.androidswipetodelete.swipetodelete.SwipeToDeleteCompatibleInterface;
import com.flowolf86.androidswipetodelete.swipetodelete.SwipeToDeleteHandler;
import com.flowolf86.androidswipetodelete.view.SwipeableRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.cesarferreira.rxpeople.RxPeople;
import github.cesarferreira.rxpeople.models.FakeUser;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by github.com/flowolf86/ on 20.07.15
 *
 * A list fragment demonstrating the effects of the swipe to delete handler. Can be refreshed via swipe down.
 * Demo data powered by RxPeople.
 */
public class SimpleListFragment extends Fragment implements SwipeableRecyclerView.RecyclerViewSwipeListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeToDeleteHandler mSwipeToDeleteHandler;

    public SimpleListFragment() { }

    public static SimpleListFragment newInstance() {
        return new SimpleListFragment();
    }

    @Bind(R.id.swipe_to_refresh) SwipeRefreshLayout mSwipeToRefresh;
    @Bind(R.id.people_list) SwipeableRecyclerView mRecyclerView;
    @Bind(R.id.empty) TextView mEmptyView;

    private SimpleListAdapter mSimpleListAdapter;
    private SwipeToDeleteCompatibleInterface mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mCallback = (SwipeToDeleteCompatibleInterface) getActivity();
        } catch (final ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnElementSelectedListener");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_simple_list, container, false);

        ButterKnife.bind(this, view);
        prepareRecyclerView();
        prepareSwipeToDeleteHandler(view);
        prepareSwipeToRefreshHandler();

        return view;
    }

    /**
     * Sets up the recycler view with the necessary adapters, listeners and animators
     */
    private void prepareRecyclerView() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mSimpleListAdapter = new SimpleListAdapter(null, mCallback);
        mRecyclerView.setAdapter(mSimpleListAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSwipeListener(this);
    }

    /**
     * Glue the adapter to the swipe to delete handler
     *
     * @param view
     */
    private void prepareSwipeToDeleteHandler(@NonNull View view) {

        mSwipeToDeleteHandler = new SwipeToDeleteHandler(getContext(), view, new Handler(), mSimpleListAdapter, mCallback, mRecyclerView);
        mSwipeToDeleteHandler.setup(mSimpleListAdapter.getData());
    }

    /**
     * Set up the swipe to refresh layout
     */
    private void prepareSwipeToRefreshHandler() {

        mSwipeToRefresh.setColorSchemeColors(R.color.colorAccent);
        mSwipeToRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onSwipe(int position) {

        // Add the object to the delete queue
        mSwipeToDeleteHandler.addDelete(position, mSimpleListAdapter.getData().get(position));
    }


    @Override
    public void onPause() {

        // Finish swipe to delete handler
        mSwipeToDeleteHandler.finish();
        super.onPause();
    }

    /**
     * Refresh the fragment with 50 random people powered by RxPeople
     */
    public void refresh() {

        RxPeople.with(getContext())
                .amount(50)
                .intoObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FakeUser>>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        setEmptyView(e.getMessage());
                    }

                    @Override
                    public void onNext(List<FakeUser> people) {
                        setDataView(people);
                    }
                });
    }

    @UiThread
    private void setDataView(List<FakeUser> people) {

        mSwipeToRefresh.setRefreshing(false);

        mSwipeToDeleteHandler.reset(people);
        if (mSimpleListAdapter != null) {
            mSimpleListAdapter.swapData(people);
            mSimpleListAdapter.adaptHeight(getContext(), mRecyclerView);
        }
    }

    @UiThread
    private void setEmptyView(String message) {
        mEmptyView.setText(message);
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
