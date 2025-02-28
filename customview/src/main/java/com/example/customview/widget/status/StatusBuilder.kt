package com.example.customview.widget.status

import android.view.View

class StatusBuilder {

    class Builder {

        var contentView: View? = null
        var errorView: View? = null
        var emptyView: View? = null
        var loadingView: View? = null

        fun setContentView(contentView: View): Builder {
            this.contentView = contentView
            return this
        }

        fun setContentView(layoutResId: Int): Builder {
            this.contentView = contentView
            return this
        }

        fun setErrorView(errorView: View): Builder {
            this.errorView = errorView
            return this
        }

        fun setEmptyView(emptyView: View): Builder {
            this.emptyView = emptyView
            return this
        }

        fun setLoadingView(loadingView: View): Builder {
            this.loadingView = loadingView
            return this
        }

        fun build() {

        }
    }
}