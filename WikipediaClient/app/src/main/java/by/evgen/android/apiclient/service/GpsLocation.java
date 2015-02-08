package by.evgen.android.apiclient.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by User on 25.11.2014.
 */
public class GpsLocation implements LocationListener {

    private LocationManager mManager;
    private static String mCoords;
    private Callbacks iCallbacks;

    public interface Callbacks {
        void onShowKor (String latitude);
    }

    public void getLocation(Callbacks callbacks, Context сontext) {
        iCallbacks = callbacks;
        mManager = (LocationManager) сontext.getSystemService(Context.LOCATION_SERVICE);
        if (mManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            mManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        else {
            if (mManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
        Log.d("id", "Coordinates changed");
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            mCoords = location.getLatitude()+"|"+location.getLongitude();
        } else if (location.getProvider().equals(
                LocationManager.GPS_PROVIDER)) {
            mCoords = location.getLatitude()+"|"+location.getLongitude();
        }
        if (mCoords != null) {
            mManager.removeUpdates(this);
            iCallbacks.onShowKor(mCoords);
        }
        Log.d("id", mCoords);
    }

}
