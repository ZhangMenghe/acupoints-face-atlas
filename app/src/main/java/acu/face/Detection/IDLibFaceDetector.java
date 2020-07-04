package acu.face.Detection;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.List;

public interface IDLibFaceDetector {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean isFaceDetectorReady();

    boolean isFaceLandmarksDetectorReady();

    /**
     * Prepare (deserialize the graph) the face detector.
     */
    void prepareFaceDetector();

    /**
     * Prepare the face landmarks detector.
     *
     * @param path The model (serialized graph) file.
     */
    void prepareFaceLandmarksDetector(String path);

    /**
     * Detect face bounds.
     *
     * @param bitmap The given photo.
     * @return A list of {@link DLibFace}.
     *
     * @throws InvalidProtocolBufferException Fired if the message cannot be
     * recognized
     */
    List<DLibFace> findFaces(Bitmap bitmap)
            throws InvalidProtocolBufferException;

    /**
     * Detect the face landmarks in the given face bound (single face).
     *
     * @param bitmap The given photo.
     * @param bound The boundary of the face.
     * @return A list of {@link DLibFace.Landmark}.
     *
     * @throws InvalidProtocolBufferException Fired if the message cannot be
     * recognized
     */
    List<DLibFace.Landmark> findLandmarksFromFace(Bitmap bitmap,
                                                  Rect bound)
            throws InvalidProtocolBufferException;

    /**
     * Detect the face landmarks in the given face bounds (multiple faces).
     *
     * @param bitmap The given photo.
     * @param faceBounds The list of face boundary.
     * @return A list of {@link DLibFace.Landmark}.
     *
     * @throws InvalidProtocolBufferException Fired if the message cannot be
     * recognized
     */
    List<DLibFace> findLandmarksFromFaces(Bitmap bitmap,
                                          List<Rect> faceBounds)
            throws InvalidProtocolBufferException;

    /**
     * Detect face bounds and then detect the face landmarks for every face.
     *
     * @param bitmap The given photo.
     * @return A list of {@link DLibFace.Landmark}.
     *
     * @throws InvalidProtocolBufferException Fired if the message cannot be
     * recognized
     */
    List<DLibFace> findFacesAndLandmarks(Bitmap bitmap)
            throws InvalidProtocolBufferException;
}

