package acu.face;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

public class JNIInterface {
    private static final String TAG = "JNIInterface";
    static AssetManager assetManager;
    public static native long JNIonCreate(AssetManager asset_manager);
    public static native void JNIonGLSurfaceCreated();
    public static native void JNIonPause();
    public static native void JNIonResume(Context context, Activity activity);
    public static native void JNIonDestroy();
    public static native void JNIonSurfaceChanged(int rotation, int width, int height);
    public static native void JNIdrawFrame();
}
