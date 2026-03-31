package org.vudroid.core.multitouch;

import android.view.MotionEvent;
/**
 * 用于约束 Multi Touch Zoom 相关能力的接口。
 */

public interface MultiTouchZoom {
    boolean onTouchEvent(MotionEvent ev);

    boolean isResetLastPointAfterZoom();

    void setResetLastPointAfterZoom(boolean resetLastPointAfterZoom);
}
