package acu.face;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.FloatBuffer;

public class ImageAnalyzer implements ImageAnalysis.Analyzer {
    private final WeakReference<Activity> actRef;

    private final static String TAG = ImageAnalyzer.class.getSimpleName();
    private long mLastAnalysisResultTime = 0;
    private final static int INPUT_TENSOR_WIDTH = 256, INPUT_TENSOR_HEIGHT = 256;
    private Module mModule = null;
    private String mModuleAssetName;
    private FloatBuffer mInputTensorBuffer;
    private Tensor mInputTensor;

    ImageAnalyzer(Activity activity){
        actRef = new WeakReference<>(activity);
    }
    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public void analyze(@NonNull ImageProxy image) {
//        if (image.getImage() == null || SystemClock.elapsedRealtime() - mLastAnalysisResultTime < 500) {
//            return;
//        }
//        if(mModule == null){
//            final String model_name = actRef.get().getResources().getString(R.string.pmodel_front_face_landmark);
//            final String model_path = actRef.get().getFilesDir().getAbsolutePath() + "/models/" + model_name;
//            File my = new File(model_path);
//            if(!my.exists())
//                Log.e(TAG, "===file not exist " );
//            mModule = Module.load(model_path);
//            mInputTensorBuffer =
//                    Tensor.allocateFloatBuffer(3 * INPUT_TENSOR_WIDTH * INPUT_TENSOR_HEIGHT);
//            mInputTensor = Tensor.fromBlob(mInputTensorBuffer, new long[]{1, 3, INPUT_TENSOR_HEIGHT, INPUT_TENSOR_WIDTH});
//        }
//
//        boolean success = false;
//
//        int rotationDegrees = image.getImageInfo().getRotationDegrees();
//        final long startTime = SystemClock.elapsedRealtime();
//        TensorImageUtils.imageYUV420CenterCropToFloatBuffer(
//                image.getImage(), rotationDegrees,
//                INPUT_TENSOR_WIDTH, INPUT_TENSOR_HEIGHT,
//                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
//                TensorImageUtils.TORCHVISION_NORM_STD_RGB,
//                mInputTensorBuffer, 0);
//
//        final long moduleForwardStartTime = SystemClock.elapsedRealtime();
//        final Tensor outputTensor = mModule.forward(IValue.from(mInputTensor)).toTensor();
//        int[] out_data = outputTensor.getDataAsIntArray();
//        long[] oshape = outputTensor.shape();


//        final long moduleForwardDuration = SystemClock.elapsedRealtime() - moduleForwardStartTime;
//
//        final float[] scores = outputTensor.getDataAsFloatArray();
//        final int[] ixs = Utils.topK(scores, TOP_K);
//
//        final String[] topKClassNames = new String[TOP_K];
//        final float[] topKScores = new float[TOP_K];
//        for (int i = 0; i < TOP_K; i++) {
//            final int ix = ixs[i];
//            topKClassNames[i] = Constants.IMAGENET_CLASSES[ix];
//            topKScores[i] = scores[ix];
//        }
//        final long analysisDuration = SystemClock.elapsedRealtime() - startTime;

//        image.close();
//        if(success){
//            mLastAnalysisResultTime = SystemClock.elapsedRealtime();
//            Log.e(TAG, "===analyze success " );
////            runOnUiThread(() -> applyToUiAnalyzeImageResult(result));
//
//        }
    }
}
