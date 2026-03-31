package org.vudroid.core.models;

import org.vudroid.core.events.CurrentPageListener;
import org.vudroid.core.events.EventDispatcher;
/**
 * 用于描述 Current Page 数据的实体类。
 */

public class CurrentPageModel extends EventDispatcher {
    private int currentPageIndex;

    public void setCurrentPageIndex(int currentPageIndex) {
        if (this.currentPageIndex != currentPageIndex) {
            this.currentPageIndex = currentPageIndex;
            dispatch(new CurrentPageListener.CurrentPageChangedEvent(currentPageIndex));
        }
    }
}
