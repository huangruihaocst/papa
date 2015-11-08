package com.Activities.papa.attendence;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WifiMonitoringService extends Service {
    public WifiMonitoringService() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
