package com.shirescience.dali.hallodali;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

public class ClockWidget extends AppWidgetProvider {
    RemoteViews views;
    //preferences
    private SharedPreferences custClockPrefs;
    //number of possible designs
    private int numClocks;
    //IDs of Analog Clock elements
    int[] clockDesigns;

    public void onReceive(Context context, Intent intent) {
        numClocks = context.getResources().getInteger(R.integer.num_clocks);
        clockDesigns = new int[numClocks];
        for(int d=0; d<numClocks; d++){
            clockDesigns[d]=context.getResources().getIdentifier
                    ("AnalogClock"+d, "id", context.getPackageName());
        }
        //find out the action
        String action = intent.getAction();
        //is it time to update
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
        {
            views = new RemoteViews(context.getPackageName(),
                    R.layout.clock_widget_layout);
            custClockPrefs = context.getSharedPreferences("CustomClockPrefs", 0);
            int chosenDesign = custClockPrefs.getInt("clockdesign", -1);
            if(chosenDesign>=0){
                for(int d=0; d<numClocks; d++){
                    if(d!=chosenDesign)
                        views.setViewVisibility(clockDesigns[d], View.INVISIBLE);
                }
                views.setViewVisibility(clockDesigns[chosenDesign], View.VISIBLE);

            }
            Intent choiceIntent = new Intent(context, ClockChoice.class);
            PendingIntent clickPendIntent = PendingIntent.getActivity
                    (context, 0, choiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.custom_clock_widget, clickPendIntent);
            AppWidgetManager.getInstance(context).updateAppWidget
                    (intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);
        }

    }

}
