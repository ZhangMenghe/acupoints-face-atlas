package acu.face;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
//import androidx.camera.core.PreviewConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import acu.face.Detection.ImageAnalyzerDlib;

public class MainActivity extends GLActivity
        implements DisplayManager.DisplayListener{
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("protobuf-lite");
        System.loadLibrary("native-lib");
    }
    final static String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA};

    private ImageCapture imageCapture;
    private ImageAnalysis imageAnalysis;
    private Executor analyze_executor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startBackgroundThread();
        analyze_executor = Executors.newSingleThreadExecutor();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    REQUEST_CODE_CAMERA_PERMISSION);
        } else {
            setupCameraX();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getSystemService(DisplayManager.class).registerDisplayListener(this, null);
    }
    @Override
    protected void onPause(){
        super.onPause();
        getSystemService(DisplayManager.class).unregisterDisplayListener(this);
    }
    @Override
    protected void updateOnFrame(){
        super.updateOnFrame();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //override, do nothing
    }

    private void setupCameraX() {
        PreviewView previewView = findViewById(R.id.background_texture_view);
        ListenableFuture cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // Camera provider is now guaranteed to be available
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider)cameraProviderFuture.get();

                // Set up the view finder use case to display camera preview
                Preview preview = new Preview.Builder().build();

                // Set up the capture use case to allow users to take photos
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                imageAnalysis =
                        new ImageAnalysis.Builder()
//                                .setTargetResolution(new Size(520, 520))
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build();
                imageAnalysis.setAnalyzer(analyze_executor, new ImageAnalyzerDlib(this));
                // Choose the camera by requiring a lens facing
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();

                // Attach use cases to the camera with the same lifecycle owner
                Camera camera = cameraProvider.bindToLifecycle(
                        ((LifecycleOwner) this),
                        cameraSelector,
                        preview,
                        imageCapture,
                        imageAnalysis);

                // Connect the preview use case to the previewView
                preview.setSurfaceProvider(
                        previewView.createSurfaceProvider());//(CameraInfo)(camera.getCameraInfo()))
            } catch (InterruptedException | ExecutionException e) {
                // Currently no exceptions thrown. cameraProviderFuture.get() should
                // not block since the listener is being called, so no need to
                // handle InterruptedException.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                        this,
                        "CAMERA permission must be granted",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            } else {
                setupCameraX();
            }
        }
    }

    // DisplayListener methods
    @Override
    public void onDisplayAdded(int displayId) {}

    @Override
    public void onDisplayRemoved(int displayId) {}

    @Override
    public void onDisplayChanged(int displayId) {
        viewportChanged = true;
    }
}

