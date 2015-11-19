package com.Activities.papa.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeoFence {

    public static class Fence {
        public double latitude, longitude, radius;
        public Object additional;
        public Fence(double latitude, double longitude, double radius, Object additional) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.radius = radius;
            this.additional = additional;
        }
    }
    // The value in the HashMap is the times we are in the fence.
    // Less than 0: Times we are out of the fence.
    // 0: Confirm out of the fence.
    // Greater than 1: Times we are in the fence.
    // 1: Confirm in the fence.
    static final int CONFIRMATION_COUNT = 10;
    static final int IN_FENCE = 1;
    static final int OUT_OF_FENCE = 0;
    static final int INITIAL_CHECK_OUT_OF_FENCE = -1;
    static final int INITIAL_CHECK_IN_FENCE = 2;

    static int maybeInFence(int status) {
        if (status > 1) {
            return status - 1;
        }
        else if (status == 1) {
            return 0;
        }
        else {
            return -1;
        }
    }
    static int maybeOutOfFence(int status) {
        return status <= 0 ? -status : 1;
    }
    static int addOutCount(int status) {
        return status - 1;
    }
    static int addInCount(int status) {
        return status + 1;
    }

    HashMap<Fence, Integer> fences = new HashMap<>();
    OnEnterFenceListener enterFenceListener;
    OnLeaveFenceListener leaveFenceListener;

    public GeoFence(List<Fence> fences) {
        for (Fence fence : fences) {
            this.fences.put(fence, OUT_OF_FENCE);
        }
    }

    public void setOnEnterFenceListener(OnEnterFenceListener enterFenceListener) {
        this.enterFenceListener = enterFenceListener;
    }
    public void setOnLeaveFenceListener(OnLeaveFenceListener leaveFenceListener) {
        this.leaveFenceListener = leaveFenceListener;
    }

    public void flushLocation(double latitude, double longitude, double accuracy) {
        for (Fence fence : fences.keySet()) {
            int status = fences.get(fence);
            if (checkInFence(latitude, longitude, accuracy, fence)) {
                if (status <= OUT_OF_FENCE) {
                    fences.put(fence, INITIAL_CHECK_OUT_OF_FENCE);
                }
                else {
                    int count = status - 1;
                    if (count > CONFIRMATION_COUNT) {
                        enterFenceListener.onEnterFence(fence);
                        fences.put(fence, IN_FENCE);
                    }
                    else if (count > 0) {
                        fences.put(fence, addInCount(status));
                    }
                    else {
                        // do nothing.
                    }
                }
            }
            else {
                // not in fence
                if (status >= IN_FENCE) {
                    fences.put(fence, INITIAL_CHECK_OUT_OF_FENCE);
                }
                else {
                    int count = -status;
                    if (count > CONFIRMATION_COUNT) {
                        // check out of fence many times. confirm leave
                        leaveFenceListener.onLeaveFence(fence);
                        fences.put(fence, OUT_OF_FENCE);
                    }
                    else if (count > 0) {
                        // should check a bit more times
                        fences.put(fence, addOutCount(status));
                    }
                    else {
                        // already out of fence, do nothing
                    }
                }
            }

        }
    }

    public static boolean checkInFence(double latitude, double longitude, double accuracy, Fence fence) {
        double dis = distance(latitude, longitude, fence.latitude, fence.longitude);
        // compensate for location provider
        return dis < accuracy + fence.radius;
    }

    static double EarthRadius = 6371000;
    /**
     * Helper function, calculates the distance by latitude and longitude
     * @param latA src latitude
     * @param longA source longitude
     * @return distance
     */
    static double distance(double latA, double longA, double latB, double longB) {
        // R*arccos[sin(wA)sin(wB)+cos(wA)cos(wB)*cos(jA-jB)]
        return EarthRadius * Math.acos(
                Math.sin(longA) * Math.sin(latB) +
                        Math.cos(longA) * Math.cos(latB) * Math.cos(latA - longB)
        );
    }
}
