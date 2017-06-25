package uk.tudorsirbu.track.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

import static uk.tudorsirbu.track.util.Constants.PREFS_NAME;
import static uk.tudorsirbu.track.util.Constants.USER_ID;

/**
 * Created by tudorsirbu on 25/06/2017.
 */

public class UserManager {
    private SharedPreferences mSharedPrefs;

    public UserManager(Context context) {
        mSharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     *
     * @return String UUID for the current session
     */
    public String getUserId(){
        String userId = mSharedPrefs.getString(USER_ID, null);

        if(userId == null){
            userId = generateUserId();
        }

        return userId;
    }

    /**
     * Generates a UUID for the user and saves it in the Shared Preferences
     * @return String user id generated
     */
    private String generateUserId(){
        String userId = UUID.randomUUID().toString();

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(USER_ID, userId);
        editor.apply();

        return userId;
    }
}
