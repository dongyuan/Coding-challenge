package worldremit.worldremit.android;

import android.app.Application;

import com.squareup.otto.Bus;

import worldremit.worldremit.android.network.WorldRemitService;
import worldremit.worldremit.android.util.BusProvider;

/**
 * Created by ericyuan on 14/03/2015.
 */
public class WRApplication extends Application{
    private WorldRemitService mWorldRemitClient;
    private Bus mBus;

    @Override
    public void onCreate() {
        super.onCreate();
        // initialisation
        mBus = BusProvider.getInstance();

        // the API URL defined in {Configuration.xml}, which make it easy to change based on the flavour.
        mWorldRemitClient = new WorldRemitService(getString(R.string.worldremit_api_url));

        // listen for the network request events
        mBus.register(mWorldRemitClient);
    }
}
