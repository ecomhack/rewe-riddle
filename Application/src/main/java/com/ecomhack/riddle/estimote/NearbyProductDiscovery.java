package com.ecomhack.riddle.estimote;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ecomhack.riddle.ApplicationState;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by stephanwels1 on 09.05.15.
 */
public class NearbyProductDiscovery {

    // should be retrieved dynamically from a database...
    private static HashMap<Integer, String> beaconMajorToProduct;

    static {
        beaconMajorToProduct = new HashMap<>();
        beaconMajorToProduct.put(38286, "beacon_blue");
        beaconMajorToProduct.put(33910, "beacon_light_green");
        beaconMajorToProduct.put(46775, "beacon_white");
    }

    private final Context context;

    public NearbyProductDiscovery(final Context context){
        this.context = Objects.requireNonNull(context, "context must not be null");
        storeNearProducts(new HashSet<String>());
    }

    public void discoverProducts(List<Beacon> nearbyBeacons){
        Set<String> nearProducts = new HashSet<>();
        Set<String> reachableProducts = new HashSet<>();
        for (Beacon beacon : nearbyBeacons){
            if (beaconMajorToProduct.containsKey(beacon.getMajor())){
                reachableProducts.add(beaconMajorToProduct.get(beacon.getMajor()));
                double distanceMeters = Utils.computeAccuracy(beacon);
                if (distanceMeters<2){
                    // If we are less than 2 meters away from the beacon, we got it
                    nearProducts.add(beaconMajorToProduct.get(beacon.getMajor()));
                }
            }
        }
        storeNearProducts(nearProducts);
        ApplicationState.setProductsInRange(reachableProducts);
    }

    private void storeNearProducts(Set<String> nearProducts) {
        Log.d("riddle", "Nearby Products: " + nearProducts.toString());
        ApplicationState.setNearProducts(nearProducts);
    }


    public void close(){
        storeNearProducts(new HashSet<String>());
    }

    public String getBeaconName(int major) {
        return beaconMajorToProduct.get(major);
    }
}
