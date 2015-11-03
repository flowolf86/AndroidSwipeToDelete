
package com.flowolf86.androidswipetodelete.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.flowolf86.androidswipetodelete.R;

/**
 * Created by github.com/flowolf86/ on 20.07.15
 *
 * Simple GuiUtils making life easier
 */
public class GuiUtils {

    public static final long SNACKBAR_NO_DELAY = 0;
    public static final long SNACKBAR_DEFAULT_DELAY = 300;
    public static final long SNACKBAR_MEDIUM_DELAY = 1000;
    public static final long SNACKBAR_LONG_DELAY = 1500;

    private GuiUtils() {}

    /**
     * Displays a snackbar on top of the given root layout
     * THe snackbar dissapears with the activity. It is therefore recommended to display
     * this snackbar on the new activity (if the snackbar goes hand in hand with an activity change)
     *
     * return the shown Snackbar
     *
     * @param rootView
     * @param snackbarText
     * @param actionCallbackText
     * @param actionCallback
     * @param displayDelay
     * @param snackbarDuration
     */
    public static @Nullable Snackbar displaySnackbar(@Nullable final View rootView, final String snackbarText, final String actionCallbackText,
                             final View.OnClickListener actionCallback, long displayDelay, final int snackbarDuration) {

        if(rootView != null) {
            final Snackbar snackbar = Snackbar.make(rootView, snackbarText, snackbarDuration);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (actionCallback != null && actionCallbackText != null) {
                        snackbar.setAction(actionCallbackText, actionCallback);
                    }
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(snackbar.getView().getContext(), R.color.colorPrimaryDark));
                    snackbar.setActionTextColor(ContextCompat.getColor(rootView.getContext(), R.color.colorAccent));
                    snackbar.show();
                }
            }, displayDelay);
            return snackbar;
        }
        return null;
    }

    /**
     * calculate the needed height of a listview (or any other view), which is need to display all the children
     *
     * @param amount of the shown views
     * @param context of the listview
     * @param dimenResId of the height of one list item
     *
     * @return listview/RecyclerView height as int
     */
    public static int calculateScrollableHeightDependOnChildren(@NonNull final Context context, int amount, int dimenResId)
    {
        int height =0;
        Resources resources= context.getResources();
        double densityFactor = GuiUtils.getDeviceDensityFactor(context);

        height += (int) (amount + 0.2) * (resources.getDimension(dimenResId) + densityFactor + 1);

        return height;
    }

    public static double getDeviceDensityFactor(final Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

}
