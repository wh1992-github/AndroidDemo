package org.vudroid.core.events;
/**
 * 封装 Bring Up Zoom Controls Event 相关逻辑的类。
 */

public class BringUpZoomControlsEvent extends SafeEvent<BringUpZoomControlsListener> {
    @Override
    public void dispatchSafely(BringUpZoomControlsListener listener) {
        listener.toggleZoomControls();
    }
}
