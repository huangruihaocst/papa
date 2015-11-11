package com.Activities.papa.attendance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class NetworkStateChangeBroadcastReceiver extends BroadcastReceiver {
    static final String TAG = "WifiStateChangeBR";
    public NetworkStateChangeBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager cm =
//                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//        if (isConnected) {
//            checkGatewayAndSignIn(context);
//        }
//        Log.w(TAG, "wifi changed");
    }

    private static String intToIp(int i) {
        return ((i >> 24 ) & 0xFF ) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ( i & 0xFF) ;
    }
    private static String macToString(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        if (mac == null)
            return "";
        for (int i = mac.length - 1; i >= 0; --i) {
            sb.append(String.format("%02x", mac[i]));
            if (i != 0)
                sb.append(":");
        }
        return sb.toString();
    }
    private static String ipArrayToString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        if (arr == null)
            return "";
        for (int i = arr.length - 1; i >= 0; --i) {
            sb.append(String.format("%d", arr[i]));
            if (i != 0)
                sb.append(".");
        }
        return sb.toString();
    }

    private void checkGatewayAndSignIn(Context context) {
        WifiManager wifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        Log.w(TAG, "IP Address=" + intToIp(dhcpInfo.gateway));
        List<NetworkInterface> interfaces = null;
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                try {
                    byte[] mac = networkInterface.getHardwareAddress();
                    Log.w(TAG, macToString(mac));
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress x = inetAddresses.nextElement();
                    if (x.getAddress() != null) {
                        Log.w(TAG, ipArrayToString(x.getAddress()));
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if (checkInClassroom(dhcpInfo, interfaces)) {
            Attendance.getInstance().trySignIn(new OnSignInSuccessListener() {
                @Override
                public void onSignInSuccess() {
                    Log.w(TAG, "Sign in success");
                }
            }, context);
        }
    }

    private boolean checkInClassroom(DhcpInfo dhcpInfo, List<NetworkInterface> networkInterfaces) {
        return true;
    }
}
