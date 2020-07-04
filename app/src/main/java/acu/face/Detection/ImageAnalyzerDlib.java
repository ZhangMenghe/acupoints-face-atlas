package acu.face.Detection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.Image;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.view.PreviewView;

import com.google.protobuf.InvalidProtocolBufferException;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.List;

import acu.face.R;

public class ImageAnalyzerDlib implements ImageAnalysis.Analyzer {
    private final WeakReference<Activity> actRef;

    private final static String TAG = ImageAnalyzerDlib.class.getSimpleName();
    private final static int INPUT_TENSOR_WIDTH = 256, INPUT_TENSOR_HEIGHT = 256;

    IDLibFaceDetector mLandmarksDetector;
//    private final SparseArray<DLibFace> mDetFaces = new SparseArray<>();
    private static Matrix SCALE_MAT = null;
    PreviewView previewView;

    public ImageAnalyzerDlib(Activity activity){
        actRef = new WeakReference<>(activity);
        previewView = activity.findViewById(R.id.background_texture_view);
        mLandmarksDetector = new DLibLandmarks68Detector();
        mLandmarksDetector.prepareFaceDetector();

        Resources res = activity.getResources();
        mLandmarksDetector.prepareFaceLandmarksDetector(
                activity.getFilesDir().getAbsolutePath()
                +'/' + res.getString(R.string.asset_copy_folder)
                        + '/' + res.getString(R.string.dlib_face_detector_68_file));
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        Bitmap bm = previewView.getBitmap();
        if(bm == null) return;

        if(SCALE_MAT == null){
            SCALE_MAT = new Matrix();
            float scaleWidth = ((float) INPUT_TENSOR_WIDTH) / bm.getWidth();
            float scaleHeight = ((float) INPUT_TENSOR_HEIGHT) / bm.getHeight();
            SCALE_MAT.postScale(scaleWidth, scaleHeight);
            Log.e(TAG, "====size: " +  bm.getWidth() + " " + bm.getHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(
                bm, 0, 0, bm.getWidth(), bm.getHeight(), SCALE_MAT, false);
//        mDetFaces.clear();
        try{
            List<DLibFace> detFaces = mLandmarksDetector.findFacesAndLandmarks(bitmap);
//            for (int i = 0; i < detFaces.size(); ++i) mDetFaces.put(i, detFaces.get(i));
//            Log.e(TAG, "===analyze: " + detFaces.size() );
            for(DLibFace face:detFaces){
                Log.e(TAG, "===face " + face.getAllLandmarks().size() );
//                Log.e(TAG, "====face: " + face.getBound().left + " " +face.getBound().right + " " + );
            }
        }catch (InvalidProtocolBufferException err) {
            err.printStackTrace();
        }
        image.close();
    }
}
