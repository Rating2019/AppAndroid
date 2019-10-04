package com.cristal.eduardo.ratingcontrol;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class MyAlarmService extends JobService {

    private static final String TAG = "JobSer2";
    private boolean jobCancelled = false;

    private PendingIntent pendingIntent;
    private final static String ChId = "NOTIFICACION";
    private final static int NotId = 1;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job2 started");
        doBackgroundWork(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled = true;
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    if (jobCancelled) {
                        return;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Job2 finished");
                CreaChannel();
                CreaNotification();
                jobFinished(params, false);
            }
        }).start();
    }

    private void CreaChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notification";
            NotificationChannel notificationChannel = new NotificationChannel(ChId, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void CreaNotification(){
        Intent intent = new Intent(this, MyAccess.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MyAccess.class)
                .addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), ChId);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("VERIFICACION DE PERMANENCIA")
                .setContentText("¿Continua viendo el mismo canal?")
                .setColor(Color.BLUE)
                .setLights(Color.MAGENTA, 1000, 1000)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NotId, builder.build());
    }
}
