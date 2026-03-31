package org.vudroid.core.events;
/**
 * 用于约束 Current Page 回调能力的接口。
 */

public interface CurrentPageListener {
    void currentPageChanged(int pageIndex);

    class CurrentPageChangedEvent extends SafeEvent<CurrentPageListener> {
        private final int pageIndex;

        public CurrentPageChangedEvent(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        @Override
        public void dispatchSafely(CurrentPageListener listener) {
            listener.currentPageChanged(pageIndex);
        }
    }
}
