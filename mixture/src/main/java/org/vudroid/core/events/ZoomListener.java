package org.vudroid.core.events;

public interface ZoomListener {
    void zoomChanged(float newZoom, float oldZoom);

    void commitZoom();

    class CommitZoomEvent extends SafeEvent<ZoomListener> {
        @Override
        public void dispatchSafely(ZoomListener listener) {
            listener.commitZoom();
        }
    }
}
