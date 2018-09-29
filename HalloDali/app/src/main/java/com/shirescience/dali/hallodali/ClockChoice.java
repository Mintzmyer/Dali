package com.shirescience.dali.hallodali;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RemoteViews;

public class ClockChoice extends Activity implements OnClickListener {
    //count of designs
    private int numDesigns;
    //image buttons for each design
    private ImageButton[] designBtns;
    //identifiers for each clock element
    private int[] designs;
    private SharedPreferences clockPrefs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_choice);
        numDesigns = this.getResources().getInteger(R.integer.num_clocks);
        designBtns = new ImageButton[numDesigns];
        designs = new int[numDesigns];
        for(int d=0; d<numDesigns; d++){
            designs[d] = this.getResources().getIdentifier
                    ("AnalogClock"+d, "id", getPackageName());
            designBtns[d]=(ImageButton)findViewById(this.getResources().getIdentifier
                    ("design_"+d, "id", getPackageName()));
            designBtns[d].setOnClickListener(this);

        }
        clockPrefs = getSharedPreferences("CustomClockPrefs", 0);

    }
    public void onClick(View v) {
        int picked = -1;
        for(int c=0; c<numDesigns; c++){
            if(v.getId()==designBtns[c].getId()){
                picked=c;
                break;
            }
        }
        int pickedClock = designs[picked];
        RemoteViews remoteViews = new RemoteViews
                (this.getApplicationContext().getPackageName(),
                        R.layout.clock_widget_layout);
        for(int d=0; d<designs.length; d++){
            if(d!=pickedClock)
                remoteViews.setViewVisibility(designs[d], View.INVISIBLE);
        }
        remoteViews.setViewVisibility(pickedClock, View.VISIBLE);
        //get component name for widget class
        ComponentName comp = new ComponentName(this, ClockWidget.class);
        //get AppWidgetManager
        AppWidgetManager appWidgetManager =
                AppWidgetManager.getInstance(this.getApplicationContext());
        //update
        appWidgetManager.updateAppWidget(comp, remoteViews);
        SharedPreferences.Editor custClockEdit = clockPrefs.edit();
        custClockEdit.putInt("clockdesign", picked);
        custClockEdit.commit();
        finish();
    }
}
