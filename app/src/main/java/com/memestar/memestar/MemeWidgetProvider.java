package com.memestar.memestar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.memestar.memestar.ui.MainActivity;


public class MemeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        int memes[] = new int[]{R.drawable.meme_1,R.drawable.meme_2,R.drawable.meme_3,R.drawable.meme_4};

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setImageViewResource(R.id.imageView, memes[MainActivity.memeIndex]);

            Intent intent = new Intent(context, MemeWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
            MainActivity.memeIndex++;
            if (MainActivity.memeIndex == 4) {
                MainActivity.memeIndex = 0;
            }
        }
    }
}
