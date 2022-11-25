package com.example.happymeals;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.IngredientStorageActivity;


public class NotificationManagerClass {
    String title;
    String body;
    Context context;
    String channelId;
    int notificationId;
    Drawable icon;

    public NotificationManagerClass(String title, String body, Context context, String channeldId, int notificationId) {
        this.title = title;
        this.body = body;
        this.context = context;
        this.channelId = channeldId;
        this.notificationId = notificationId;
    }

    public void addNotification() {
        // create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(body);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ingredients_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // ingredient icon is temporary. To be updated with app's logo
        Intent notificationIntent = new Intent(context, IngredientStorageActivity.class);
        notificationIntent.putExtra("MissingCheck", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add a notification
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(notificationId, builder.build());
    }


}
