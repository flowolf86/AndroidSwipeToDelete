package com.flowolf86.androidswipetodelete.swipetodelete;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.Map;

/**
 * Created by github.com/flowolf86/ on 20.07.15
 *
 * A swipe to delete compatible adapter
 */
public interface SwipeToDeleteCompatibleInterface {

    /**
     * The onClickListener callback. We pass the view here to be able to
     * use material design meaningful transitions
     *
     * @param view
     * @param object
     */
    void onElementSelected(@NonNull View view, @NonNull final Object object);

    void onSingleElementSwiped(@NonNull final Object object);

    void onMultipleElementsSwiped(@NonNull final Map<Integer, Object> allTrips);
}
