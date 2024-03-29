//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.aerilys.geowarfare.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.aerilys.geowarfare.android.R.layout;
import com.aerilys.geowarfare.android.api.foursquare.Venue;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.googlecode.androidannotations.api.SdkVersionHelper;

public final class CheckinActivity_
    extends CheckinActivity
{

    private Handler handler_ = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_checkin);
    }

    private void init_(Bundle savedInstanceState) {
    }

    private void afterSetContentView_() {
        bindDatas();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static CheckinActivity_.IntentBuilder_ intent(Context context) {
        return new CheckinActivity_.IntentBuilder_(context);
    }

    @Override
    public void manageUnitsPutArmyCompleted(final String result, final int unitsCount) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.manageUnitsPutArmyCompleted(result, unitsCount);
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void selectVenueCompleted() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.selectVenueCompleted();
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void changeLoading() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.changeLoading();
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void loadVenuesCompleted() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.loadVenuesCompleted();
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void openShare(final Venue venue) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.openShare(venue);
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void openInFoursquare(final String venueId) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.openInFoursquare(venueId);
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void manageUnitsPutArmy(final int unitsCount) {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.manageUnitsPutArmy(unitsCount);
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void selectVenue(final int position) {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.selectVenue(position);
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void loadVenues() {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    CheckinActivity_.super.loadVenues();
                } catch (RuntimeException e) {
                    Log.e("CheckinActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, CheckinActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public CheckinActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

    }

}
