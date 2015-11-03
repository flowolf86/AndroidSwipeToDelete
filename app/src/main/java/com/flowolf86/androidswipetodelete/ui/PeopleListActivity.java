
package com.flowolf86.androidswipetodelete.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.flowolf86.androidswipetodelete.R;
import com.flowolf86.androidswipetodelete.swipetodelete.SwipeToDeleteCompatibleInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import github.cesarferreira.rxpeople.models.FakeUser;

/**
 * Created by github.com/flowolf86/ on 20.07.15
 *
 * The main activity of the demo application. Holds one fragment with a recyclerview.
 */
public class PeopleListActivity extends BaseActivity implements SwipeToDeleteCompatibleInterface {

    private SimpleListFragment mPeopleListFragment;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_people_list);
        super.setupToolbar(R.id.toolbar, false);
        setToolbarTitle("SwipeToDelete and Undo");

        setupFragment();
    }

    private void setupFragment() {
        mPeopleListFragment = (SimpleListFragment) getSupportFragmentManager().findFragmentById(R.id.people_list_fragment);
        mPeopleListFragment.refresh();
    }

    /*
        Swipe compatible interface
     */

    @Override
    public void onElementSelected(@NonNull final View view, @NonNull final Object object) {

        if(object instanceof FakeUser){
            // TODO Handle click here...
        }else{
            throw new IllegalStateException("Selected element is not of type FakeUser. Instead: " + object.getClass());
        }
    }

    @Override
    public void onSingleElementSwiped(@NonNull final Object object) {

        if(object instanceof FakeUser){

            // TODO Delete stuff from database here
        }else{
            throw new IllegalStateException("Swiped element is not of type FakeUser. Instead: " + object.getClass());
        }
    }

    @Override
    public void onMultipleElementsSwiped(@NonNull Map<Integer, Object> allObjects) {

        List<FakeUser> peopleList = new ArrayList<>();
        for(Map.Entry<Integer, ?> entry : allObjects.entrySet()){

            if(!(entry.getValue() instanceof FakeUser)){
                continue;
            }

            peopleList.add((FakeUser) entry.getValue());
        }

        // TODO Delete multiple stuff from database here
    }
}
