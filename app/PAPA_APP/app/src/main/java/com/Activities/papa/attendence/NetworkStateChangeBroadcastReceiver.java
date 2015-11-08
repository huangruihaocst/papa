package com.Activities.papa.attendence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class NetworkStateChangeBroadcastReceiver extends BroadcastReceiver {
    static final String TAG = "WifiStateChangeBR";
    public NetworkStateChangeBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.w(TAG, "wifi changed");

        getGatewayInfo();
    }

    private void getGatewayInfo() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                byte[] mac = networkInterface.getHardwareAddress();
                Log.w(TAG, String.valueOf(mac));
                Enumeration<InetAddress> addrs = networkInterface.getInetAddresses();
                for (InetAddress x = addrs.nextElement(); addrs.hasMoreElements(); x = addrs.nextElement()) {
                    if (x.getAddress() != null) {
                        Log.w(TAG, x.getAddress().toString());
                    }
                }
            }
        }
        catch (Exception ex) {
        }
    }
}
