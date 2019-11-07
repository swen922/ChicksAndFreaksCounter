package com.horovod.android.chicksandfreakscounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class ChicksAndFreaksWidget extends AppWidgetProvider {

    private static int counterFreaks = 0;
    private static int counterChicks = 0;
    private static int myWidgetId;

    private static Intent intentOpenApp;
    private static Intent intentAddFreak;
    private static Intent intentAddChick;

    private static PendingIntent pendingIntentApp;
    private static PendingIntent pendingIntentFreak;
    private static PendingIntent pendingIntentChick;


    private static void updateWidget(Context context, AppWidgetManager manager, int widgetId) {

        myWidgetId = widgetId;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_screen);


        if (!Data.getDudes().isEmpty()) {
            counterFreaks = 0;
            for (Dude dude : Data.getDudes()) {
                if (dude.getDudeType().equals(DudeType.FREAK.toString())) {
                    counterFreaks++;
                }
            }
            counterChicks = Data.getDudes().size() - counterFreaks;
            remoteViews.setTextViewText(R.id.widget_counter_freaks, String.valueOf(counterFreaks));
            remoteViews.setTextViewText(R.id.widget_counter_chicks, String.valueOf(counterChicks));
        }
        else {
            remoteViews.setTextViewText(R.id.widget_counter_freaks, String.valueOf(0));
            remoteViews.setTextViewText(R.id.widget_counter_chicks, String.valueOf(0));
        }

        if (intentOpenApp == null || pendingIntentApp == null) {
            intentOpenApp = new Intent(context, MainActivity.class);
            intentOpenApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntentApp = PendingIntent.getActivity(context, 0, intentOpenApp, PendingIntent.FLAG_CANCEL_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_header, pendingIntentApp);
        }

        if (intentAddFreak == null || pendingIntentFreak == null) {
            intentAddFreak = new Intent(context, MainActivity.class);
            intentAddFreak.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentAddFreak.setAction(Data.KEY_CREATE_FREAK);
            pendingIntentFreak = PendingIntent.getActivity(context, 1, intentAddFreak, PendingIntent.FLAG_CANCEL_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_counter_freaks, pendingIntentFreak);
        }

        if (intentAddChick == null || pendingIntentChick == null) {
            intentAddChick = new Intent(context, MainActivity.class);
            intentAddChick.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentAddChick.setAction(Data.KEY_CREATE_CHICK);
            pendingIntentChick = PendingIntent.getActivity(context, 2, intentAddChick, PendingIntent.FLAG_CANCEL_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_counter_chicks, pendingIntentChick);
        }

        manager.updateAppWidget(widgetId, remoteViews);

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget onUpdate");

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("BLOGGGG Widget |||| ", "inside ChicksAndFreaksWidget onReceive");

        super.onReceive(context, intent);

        if (Data.KEY_UPDATE_WIDGET.equalsIgnoreCase(intent.getAction())) {

            Log.i("BLOGGGG Widget |||| ", "intent.getAction() = " + intent.getAction());

            int freaks = counterFreaks;
            try {
                freaks = Integer.parseInt(intent.getStringExtra(Data.KEY_NUMBER_FREAK));
            } catch (NumberFormatException e) {

            }
            counterFreaks = freaks;

            int chicks = counterChicks;
            try {
                chicks = Integer.parseInt(intent.getStringExtra(Data.KEY_NUMBER_CHICK));
            } catch (NumberFormatException e) {

            }
            counterChicks = chicks;

            updateWidget(context, AppWidgetManager.getInstance(context), myWidgetId);

        }


    }



}
