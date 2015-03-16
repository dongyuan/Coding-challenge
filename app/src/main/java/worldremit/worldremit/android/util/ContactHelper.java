package worldremit.worldremit.android.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by ericyuan on 14/03/2015.
 */
public class ContactHelper {
    /**
     * Get contact display name for the given Uri.
     *
     * @param context
     * @param contactUri
     *
     * @return a empty string if no contact found from the given Uri.
     */
    public static String retrieveContactName(Context context, Uri contactUri){
        String name = "";
        //Get Name
        Cursor cursor = context.getContentResolver().query(contactUri, null, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        return name;
    }

}
