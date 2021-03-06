package acu.face;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import acu.face.Utils.fileUtils;


public class GLActivity extends AppCompatActivity {
    final static String TAG = "BASIC_Activity";
    // Resource
    //SET ME!! folder name in asset folder that would be copied to runtime folder
    protected String ass_copy_src;
    //DST folder that runtime can use, constains what u copied from asset
    protected String ass_copy_dst = null;

    final static boolean skipLoadingResource = true;

    protected boolean viewportChanged = false;
    protected int viewportWidth;
    protected int viewportHeight;
    // Opaque native pointer to the native application instance.

    //Surface view
    protected GLSurfaceView surfaceView;
    protected long nativeAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        setupSurfaceView();
        JNIInterface.assetManager = getAssets();
        nativeAddr = JNIInterface.JNIonCreate(JNIInterface.assetManager);
        setupTouchDetector();
    }
    protected void checkPermissions(){}
    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
        JNIInterface.JNIonResume(getApplicationContext(), this);
    }
    @Override
    protected void onPause(){
        super.onPause();
        surfaceView.onPause();
        JNIInterface.JNIonPause();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Synchronized to avoid racing onDrawFrame.
        synchronized (this) {
            JNIInterface.JNIonDestroy();
            nativeAddr = 0;
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Standard Android full-screen functionality.
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
    private void setupTouchDetector() {}

    private void setupSurfaceView(){
        surfaceView = (GLSurfaceView) findViewById(R.id.surfaceview);
        // Set up renderer.
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setEGLContextClientVersion(3);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        surfaceView.setRenderer(new GLActivity.Renderer());
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    protected void copyFromAssets(){
        ass_copy_src = getString(R.string.asset_copy_folder);
        ass_copy_dst = getFilesDir().getAbsolutePath() + "/" + ass_copy_src;

        File destDir = new File(ass_copy_dst);

        //Skip copying if files exist
        if(skipLoadingResource && destDir.exists()) return;
        fileUtils.deleteDirectory(destDir);
        try{
            fileUtils.copyFromAsset(getAssets(), ass_copy_src, ass_copy_dst);
        }catch (Exception e){
            Log.e(TAG, "copyFromAssets: Failed to copy from asset folder");
        }
    }

    protected boolean setupResource(){
        copyFromAssets();
        return true;
    }
    protected void updateOnFrame(){}
    protected class Renderer implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            if(setupResource()){
                JNIInterface.JNIonGLSurfaceCreated();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            viewportWidth = width;
            viewportHeight = height;
            viewportChanged = true;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // Synchronized to avoid racing onDestroy.
            updateOnFrame();
            synchronized (this) {
                if (nativeAddr == 0) {
                    return;
                }
                if (viewportChanged) {
                    int displayRotation = getWindowManager().getDefaultDisplay().getRotation();
                    JNIInterface.JNIonSurfaceChanged(displayRotation, viewportWidth, viewportHeight);
                    viewportChanged = false;
                }
                JNIInterface.JNIdrawFrame();
                updateOnFrame();
            }
        }
    }
}
