package acu.face.Detection;

import android.graphics.RectF;

import java.util.List;

import acu.Messages;

public abstract class DLibFace {

    public abstract RectF getBound();

    public abstract List<Landmark> getAllLandmarks();

    public abstract List<Landmark> getLeftEyebrowLandmarks();

    public abstract List<Landmark> getRightEyebrowLandmarks();

    public abstract List<Landmark> getLeftEyeLandmarks();

    public abstract List<Landmark> getRightEyeLandmarks();

    public abstract List<Landmark> getNoseLandmarks();

    public abstract List<Landmark> getInnerLipsLandmarks();

    public abstract List<Landmark> getOuterLipsLandmarks();

    public abstract List<Landmark> getChinLandmarks();

    ///////////////////////////////////////////////////////////////////////////
    // Clazz //////////////////////////////////////////////////////////////////

    public static class Landmark {

        public final float x;
        public final float y;

        public Landmark(float x,
                        float y) {
            this.x = x;
            this.y = y;
        }

        public Landmark(Messages.Landmark landmark) {
            this.x = landmark.getX();
            this.y = landmark.getY();
        }

        public Landmark(Landmark other) {
            this.x = other.x;
            this.y = other.y;
        }

        @Override
        public String toString() {
            return "Landmark{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
