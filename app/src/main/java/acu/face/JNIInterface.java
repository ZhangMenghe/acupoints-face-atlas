package acu.face;

import android.content.res.AssetManager;

public class JNIInterface {
    private static final String TAG = "JNIInterface";
    static AssetManager assetManager;
    public static native long JNIonCreate(AssetManager asset_manager);

}
