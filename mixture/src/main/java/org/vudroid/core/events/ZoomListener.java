package org.vudroid.core.events;
/**
 * 用于约束 Zoom 回调能力的接口。
 */

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
