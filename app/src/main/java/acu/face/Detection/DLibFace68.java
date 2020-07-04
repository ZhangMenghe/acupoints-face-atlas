package acu.face.Detection;

import android.graphics.RectF;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import acu.Messages;

public class DLibFace68 extends DLibFace {

    private static final int CHIN_START = 0;
    private static final int CHIN_END = 16;

    private static final int EYEBROW_L_START = 17;
    private static final int EYEBROW_L_END = 21;
    private static final int EYEBROW_R_START = 22;
    private static final int EYEBROW_R_END = 26;

    private static final int NOSE_START = 28;
    private static final int NOSE_END = 35;

    private static final int EYE_L_START = 36;
    private static final int EYE_L_END = 41;
    private static final int EYE_R_START = 42;
    private static final int EYE_R_END = 47;

    private static final int LIPS_OUTER_START = 48;
    private static final int LIPS_OUTER_END = 59;
    private static final int LIPS_INNER_START = 60;
    private static final int LIPS_INNER_END = 67;

    private final RectF mBound = new RectF();
    private final List<Landmark> mLandmarks = new CopyOnWriteArrayList<>();

    public DLibFace68(Messages.Face rawFace) {
        // Bound.
        mBound.set(rawFace.getBound().getLeft(),
                rawFace.getBound().getTop(),
                rawFace.getBound().getRight(),
                rawFace.getBound().getBottom());

        // Landmarks.
        for (int i = 0; i < rawFace.getLandmarksCount(); ++i) {
            Messages.Landmark rawLandmark = rawFace.getLandmarks(i);

            mLandmarks.add(new Landmark(rawLandmark));
        }
    }

    public DLibFace68(RectF bound) {
        mBound.set(bound);
    }

    public DLibFace68(DLibFace other) {
        this(other, 1f, 1f);
    }

    public DLibFace68(DLibFace other, float scaleX, float scaleY) {
        // Bound.
        mBound.set(other.getBound().left * scaleX,
                other.getBound().top * scaleY,
                other.getBound().right * scaleX,
                other.getBound().bottom * scaleY);

        // Landmarks.
        for (int i = 0; i < other.getAllLandmarks().size(); ++i) {
            final DLibFace68.Landmark landmark = other.getAllLandmarks().get(i);
            mLandmarks.add(new DLibFace68.Landmark(
                    landmark.x * scaleX,
                    landmark.y * scaleY));
        }
    }

    public DLibFace68(List<Landmark> landmarks) {
        // Landmarks.
        mLandmarks.clear();
        mLandmarks.addAll(landmarks);

        // Calculate bound by the given landmarks.
        float left = Float.MAX_VALUE;
        float top = Float.MAX_VALUE;
        float right = Float.MIN_VALUE;
        float bottom = Float.MIN_VALUE;
        for (int i = 0; i < landmarks.size(); ++i) {
            final DLibFace68.Landmark landmark = landmarks.get(i);

            left = Math.min(left, landmark.x);
            top = Math.min(top, landmark.y);
            right = Math.max(right, landmark.x);
            bottom = Math.max(bottom, landmark.y);
        }
        mBound.set(left, top, right, bottom);
    }

    public RectF getBound() {
        return mBound;
    }

    public void setAllLandmarks(List<Landmark> landmarks) {
        mLandmarks.clear();
        mLandmarks.addAll(landmarks);
    }

    @Override
    public List<Landmark> getAllLandmarks() {
        return mLandmarks;
    }

    @Override
    public List<Landmark> getLeftEyebrowLandmarks() {
        return mLandmarks.subList(EYEBROW_L_START, EYEBROW_L_END + 1);
    }

    @Override
    public List<Landmark> getRightEyebrowLandmarks() {
        return mLandmarks.subList(EYEBROW_R_START, EYEBROW_R_END + 1);
    }

    @Override
    public List<Landmark> getLeftEyeLandmarks() {
        return mLandmarks.subList(EYE_L_START, EYE_L_END + 1);
    }

    @Override
    public List<Landmark> getRightEyeLandmarks() {
        return mLandmarks.subList(EYE_R_START, EYE_R_END + 1);
    }

    @Override
    public List<Landmark> getNoseLandmarks() {
        return mLandmarks.subList(NOSE_START, NOSE_END + 1);
    }

    @Override
    public List<Landmark> getInnerLipsLandmarks() {
        return mLandmarks.subList(LIPS_INNER_START, LIPS_INNER_END + 1);
    }

    @Override
    public List<Landmark> getOuterLipsLandmarks() {
        return mLandmarks.subList(LIPS_OUTER_START, LIPS_OUTER_END + 1);
    }

    @Override
    public List<Landmark> getChinLandmarks() {
        return mLandmarks.subList(CHIN_START, CHIN_END + 1);
    }

    @Override
    public String toString() {
        return "DLibFace{" +
                "mBound=" + mBound +
                ", mLandmarks=" + mLandmarks +
                '}';
    }
}
