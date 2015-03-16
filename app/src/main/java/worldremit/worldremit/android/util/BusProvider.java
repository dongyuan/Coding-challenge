package worldremit.worldremit.android.util;

import com.squareup.otto.Bus;

/**
 * Maintains a singleton instance for obtaining the bus. Ideally this would be replaced with a more efficient means
 * such as through injection directly into interested classes.
 *
 * Created by ericyuan on 14/03/2015.
 */
public class BusProvider {
    private static final Bus sBus = new Bus();

    public static synchronized Bus getInstance() {
        return sBus;
    }

    private BusProvider() {
        // No instances.
    }

}
