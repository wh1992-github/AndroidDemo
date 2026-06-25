package com.example.customview.anim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * 封装 Base Item Animation 相关逻辑的类。
 */
abstract class BaseItemAnimation : SimpleItemAnimator() {

    private val mPendingRemovals = ArrayList<ViewHolder>()
    private val mPendingAdditions = ArrayList<ViewHolder>()
    private val mPendingMoves = ArrayList<MoveInfo>()
    private val mPendingChanges = ArrayList<ChangeInfo>()

    private val mAdditionsList = ArrayList<ArrayList<ViewHolder>>()
    private val mMovesList = ArrayList<ArrayList<MoveInfo>>()
    private val mChangesList = ArrayList<ArrayList<ChangeInfo>>()

    private val mAddAnimations = ArrayList<ViewHolder>()
    private val mMoveAnimations = ArrayList<ViewHolder>()
    private val mRemoveAnimations = ArrayList<ViewHolder>()
    private val mChangeAnimations = ArrayList<ViewHolder>()

    private class MoveInfo(
        val holder: ViewHolder,
        val fromX: Int,
        val fromY: Int,
        val toX: Int,
        val toY: Int
    )

    private class ChangeInfo(
        var oldHolder: ViewHolder?,
        var newHolder: ViewHolder?,
        val fromX: Int = 0,
        val fromY: Int = 0,
        val toX: Int = 0,
        val toY: Int = 0
    ) {
        override fun toString(): String =
            "ChangeInfo{oldHolder=$oldHolder, newHolder=$newHolder, fromX=$fromX, fromY=$fromY, toX=$toX, toY=$toY}"
    }

    override fun runPendingAnimations() {
        val removalsPending = mPendingRemovals.isNotEmpty()
        val movesPending = mPendingMoves.isNotEmpty()
        val changesPending = mPendingChanges.isNotEmpty()
        val additionsPending = mPendingAdditions.isNotEmpty()
        if (!removalsPending && !movesPending && !additionsPending && !changesPending) {
            return
        }

        for (holder in mPendingRemovals) {
            animateRemoveImpl(holder)
        }
        mPendingRemovals.clear()

        if (movesPending) {
            val moves = ArrayList<MoveInfo>()
            moves.addAll(mPendingMoves)
            mMovesList.add(moves)
            mPendingMoves.clear()
            val mover = Runnable {
                for (moveInfo in moves) {
                    animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY)
                }
                moves.clear()
                mMovesList.remove(moves)
            }
            if (removalsPending) {
                if (moves.isNotEmpty()) {
                    val view = moves[0].holder.itemView
                    ViewCompat.postOnAnimationDelayed(view, mover, removeDuration)
                }
            } else {
                mover.run()
            }
        }

        if (changesPending) {
            val changes = ArrayList<ChangeInfo>()
            changes.addAll(mPendingChanges)
            mChangesList.add(changes)
            mPendingChanges.clear()
            val changer = Runnable {
                for (change in changes) {
                    animateChangeImpl(change)
                }
                changes.clear()
                mChangesList.remove(changes)
            }
            if (removalsPending) {
                if (changes.isNotEmpty()) {
                    val holder = changes[0].oldHolder
                    if (holder != null) {
                        ViewCompat.postOnAnimationDelayed(holder.itemView, changer, removeDuration)
                    }
                }
            } else {
                changer.run()
            }
        }

        if (additionsPending) {
            val additions = ArrayList<ViewHolder>()
            additions.addAll(mPendingAdditions)
            mAdditionsList.add(additions)
            mPendingAdditions.clear()
            val adder = Runnable {
                for (holder in additions) {
                    animateAddImpl(holder)
                }
                additions.clear()
                mAdditionsList.remove(additions)
            }
            if (removalsPending || movesPending || changesPending) {
                val removeDuration = if (removalsPending) removeDuration else 0
                val moveDuration = if (movesPending) moveDuration else 0
                val changeDuration = if (changesPending) changeDuration else 0
                val totalDelay = removeDuration + Math.max(moveDuration, changeDuration)
                if (additions.isNotEmpty()) {
                    val view = additions[0].itemView
                    ViewCompat.postOnAnimationDelayed(view, adder, totalDelay)
                }
            } else {
                adder.run()
            }
        }
    }

    override fun animateRemove(holder: ViewHolder): Boolean {
        resetAnimation(holder)
        mPendingRemovals.add(holder)
        return true
    }

    private fun animateRemoveImpl(holder: ViewHolder) {
        val view = holder.itemView
        val animation = view.animate()
        mRemoveAnimations.add(holder)
        setRemoveAnimation(holder, animation)
        animation.setDuration(removeDuration).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
                dispatchRemoveStarting(holder)
            }

            override fun onAnimationEnd(animator: Animator) {
                animation.setListener(null)
                setRemoveAnimationEnd(holder)
                dispatchRemoveFinished(holder)
                mRemoveAnimations.remove(holder)
                dispatchFinishedWhenDone()
            }
        }).start()
    }

    override fun animateAdd(holder: ViewHolder): Boolean {
        resetAnimation(holder)
        setAddItemAnimationInit(holder)
        mPendingAdditions.add(holder)
        return true
    }

    private fun animateAddImpl(holder: ViewHolder) {
        val view = holder.itemView
        val animation = view.animate()
        mAddAnimations.add(holder)
        setAddItemAnimation(holder, animation)
        animation.setDuration(addDuration).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
                dispatchAddStarting(holder)
            }

            override fun onAnimationEnd(animator: Animator) {
                animation.setListener(null)
                dispatchAddFinished(holder)
                mAddAnimations.remove(holder)
                dispatchFinishedWhenDone()
            }

            override fun onAnimationCancel(animation: Animator) {
                setAddItemAnimationCancel(holder)
            }
        }).start()
    }

    override fun animateMove(holder: ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        val view = holder.itemView
        val adjustedFromX = fromX + holder.itemView.translationX.toInt()
        val adjustedFromY = fromY + holder.itemView.translationY.toInt()
        resetAnimation(holder)
        val deltaX = toX - adjustedFromX
        val deltaY = toY - adjustedFromY
        if (deltaX == 0 && deltaY == 0) {
            dispatchMoveFinished(holder)
            return false
        }
        if (deltaX != 0) {
            view.translationX = (-deltaX).toFloat()
        }
        if (deltaY != 0) {
            view.translationY = (-deltaY).toFloat()
        }
        mPendingMoves.add(MoveInfo(holder, adjustedFromX, adjustedFromY, toX, toY))
        return true
    }

    private fun animateMoveImpl(holder: ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        val view = holder.itemView
        val deltaX = toX - fromX
        val deltaY = toY - fromY
        if (deltaX != 0) {
            view.animate().translationX(0f)
        }
        if (deltaY != 0) {
            view.animate().translationY(0f)
        }
        val animation = view.animate()
        mMoveAnimations.add(holder)
        animation.setDuration(moveDuration).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
                dispatchMoveStarting(holder)
            }

            override fun onAnimationCancel(animator: Animator) {
                if (deltaX != 0) view.translationX = 0f
                if (deltaY != 0) view.translationY = 0f
            }

            override fun onAnimationEnd(animator: Animator) {
                animation.setListener(null)
                dispatchMoveFinished(holder)
                mMoveAnimations.remove(holder)
                dispatchFinishedWhenDone()
            }
        }).start()
    }

    override fun animateChange(
        oldHolder: ViewHolder,
        newHolder: ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        if (oldHolder === newHolder) {
            return animateMove(oldHolder, fromX, fromY, toX, toY)
        }
        val prevTranslationX = oldHolder.itemView.translationX
        val prevTranslationY = oldHolder.itemView.translationY
        val prevAlpha = oldHolder.itemView.alpha
        resetAnimation(oldHolder)
        val deltaX = (toX - fromX - prevTranslationX).toInt()
        val deltaY = (toY - fromY - prevTranslationY).toInt()
        oldHolder.itemView.translationX = prevTranslationX
        oldHolder.itemView.translationY = prevTranslationY
        oldHolder.itemView.alpha = prevAlpha
        if (newHolder != null) {
            resetAnimation(newHolder)
            newHolder.itemView.translationX = (-deltaX).toFloat()
            newHolder.itemView.translationY = (-deltaY).toFloat()
            setNewChangeAnimationInit(newHolder)
        }
        mPendingChanges.add(ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY))
        return true
    }

    private fun animateChangeImpl(changeInfo: ChangeInfo) {
        val holder = changeInfo.oldHolder
        val view = holder?.itemView
        val newHolder = changeInfo.newHolder
        val newView = newHolder?.itemView
        if (view != null && holder != null) {
            val oldViewAnim = view.animate().setDuration(changeDuration)
            mChangeAnimations.add(holder)
            oldViewAnim.translationX((changeInfo.toX - changeInfo.fromX).toFloat())
            oldViewAnim.translationY((changeInfo.toY - changeInfo.fromY).toFloat())
            setOldChangeAnimation(holder, oldViewAnim)
            oldViewAnim.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator) {
                    dispatchChangeStarting(holder, true)
                }

                override fun onAnimationEnd(animator: Animator) {
                    oldViewAnim.setListener(null)
                    setOldChangeAnimationEnd(holder)
                    view.translationX = 0f
                    view.translationY = 0f
                    dispatchChangeFinished(holder, true)
                    mChangeAnimations.remove(holder)
                    dispatchFinishedWhenDone()
                }
            }).start()
        }
        if (newView != null && newHolder != null) {
            val newViewAnimation = newView.animate()
            mChangeAnimations.add(newHolder)
            setNewChangeAnimation(newHolder, newViewAnimation)
            newViewAnimation.translationX(0f).translationY(0f).setDuration(changeDuration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animator: Animator) {
                        dispatchChangeStarting(newHolder, false)
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        newViewAnimation.setListener(null)
                        setNewChangeAnimationEnd(newHolder)
                        newView.translationX = 0f
                        newView.translationY = 0f
                        dispatchChangeFinished(newHolder, false)
                        mChangeAnimations.remove(newHolder)
                        dispatchFinishedWhenDone()
                    }
                }).start()
        }
    }

    private fun endChangeAnimation(infoList: MutableList<ChangeInfo>, item: ViewHolder) {
        for (i in infoList.size - 1 downTo 0) {
            val changeInfo = infoList[i]
            if (endChangeAnimationIfNecessary(changeInfo, item)) {
                if (changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                    infoList.removeAt(i)
                }
            }
        }
    }

    private fun endChangeAnimationIfNecessary(changeInfo: ChangeInfo) {
        changeInfo.oldHolder?.let { endChangeAnimationIfNecessary(changeInfo, it) }
        changeInfo.newHolder?.let { endChangeAnimationIfNecessary(changeInfo, it) }
    }

    private fun endChangeAnimationIfNecessary(changeInfo: ChangeInfo, item: ViewHolder): Boolean {
        val oldItem: Boolean
        if (changeInfo.newHolder === item) {
            changeInfo.newHolder = null
            oldItem = false
        } else if (changeInfo.oldHolder === item) {
            changeInfo.oldHolder = null
            oldItem = true
        } else {
            return false
        }
        setNewChangeAnimationEnd(item)
        item.itemView.translationX = 0f
        item.itemView.translationY = 0f
        dispatchChangeFinished(item, oldItem)
        return true
    }

    override fun endAnimation(item: ViewHolder) {
        val view = item.itemView
        view.animate().cancel()
        for (i in mPendingMoves.size - 1 downTo 0) {
            val moveInfo = mPendingMoves[i]
            if (moveInfo.holder === item) {
                view.translationY = 0f
                view.translationX = 0f
                dispatchMoveFinished(item)
                mPendingMoves.removeAt(i)
            }
        }
        endChangeAnimation(mPendingChanges, item)
        if (mPendingRemovals.remove(item)) {
            setRemoveAnimationEnd(item)
            dispatchRemoveFinished(item)
        }
        if (mPendingAdditions.remove(item)) {
            setAddItemAnimationCancel(item)
            dispatchAddFinished(item)
        }

        for (i in mChangesList.size - 1 downTo 0) {
            val changes = mChangesList[i]
            endChangeAnimation(changes, item)
            if (changes.isEmpty()) {
                mChangesList.removeAt(i)
            }
        }
        for (i in mMovesList.size - 1 downTo 0) {
            val moves = mMovesList[i]
            for (j in moves.size - 1 downTo 0) {
                val moveInfo = moves[j]
                if (moveInfo.holder === item) {
                    view.translationY = 0f
                    view.translationX = 0f
                    dispatchMoveFinished(item)
                    moves.removeAt(j)
                    if (moves.isEmpty()) {
                        mMovesList.removeAt(i)
                    }
                    break
                }
            }
        }
        for (i in mAdditionsList.size - 1 downTo 0) {
            val additions = mAdditionsList[i]
            if (additions.remove(item)) {
                setAddItemAnimationCancel(item)
                dispatchAddFinished(item)
                if (additions.isEmpty()) {
                    mAdditionsList.removeAt(i)
                }
            }
        }

        if (mRemoveAnimations.remove(item) && DEBUG) {
            throw IllegalStateException("after animation is cancelled, item should not be in mRemoveAnimations list")
        }
        if (mAddAnimations.remove(item) && DEBUG) {
            throw IllegalStateException("after animation is cancelled, item should not be in mAddAnimations list")
        }
        if (mChangeAnimations.remove(item) && DEBUG) {
            throw IllegalStateException("after animation is cancelled, item should not be in mChangeAnimations list")
        }
        if (mMoveAnimations.remove(item) && DEBUG) {
            throw IllegalStateException("after animation is cancelled, item should not be in mMoveAnimations list")
        }
        dispatchFinishedWhenDone()
    }

    private fun resetAnimation(holder: ViewHolder) {
        if (sDefaultInterpolator == null) {
            sDefaultInterpolator = ValueAnimator().interpolator
        }
        holder.itemView.animate().interpolator = sDefaultInterpolator
        endAnimation(holder)
    }

    override fun isRunning(): Boolean =
        mPendingAdditions.isNotEmpty() ||
            mPendingChanges.isNotEmpty() ||
            mPendingMoves.isNotEmpty() ||
            mPendingRemovals.isNotEmpty() ||
            mMoveAnimations.isNotEmpty() ||
            mRemoveAnimations.isNotEmpty() ||
            mAddAnimations.isNotEmpty() ||
            mChangeAnimations.isNotEmpty() ||
            mMovesList.isNotEmpty() ||
            mAdditionsList.isNotEmpty() ||
            mChangesList.isNotEmpty()

    internal fun dispatchFinishedWhenDone() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    override fun endAnimations() {
        var count = mPendingMoves.size
        for (i in count - 1 downTo 0) {
            val item = mPendingMoves[i]
            val view = item.holder.itemView
            view.translationY = 0f
            view.translationX = 0f
            dispatchMoveFinished(item.holder)
            mPendingMoves.removeAt(i)
        }
        count = mPendingRemovals.size
        for (i in count - 1 downTo 0) {
            val item = mPendingRemovals[i]
            dispatchRemoveFinished(item)
            mPendingRemovals.removeAt(i)
        }
        count = mPendingAdditions.size
        for (i in count - 1 downTo 0) {
            val item = mPendingAdditions[i]
            setAddItemAnimationCancel(item)
            dispatchAddFinished(item)
            mPendingAdditions.removeAt(i)
        }
        count = mPendingChanges.size
        for (i in count - 1 downTo 0) {
            endChangeAnimationIfNecessary(mPendingChanges[i])
        }
        mPendingChanges.clear()
        if (!isRunning) {
            return
        }

        for (i in mMovesList.size - 1 downTo 0) {
            val moves = mMovesList[i]
            count = moves.size
            for (j in count - 1 downTo 0) {
                val moveInfo = moves[j]
                val item = moveInfo.holder
                val view = item.itemView
                view.translationY = 0f
                view.translationX = 0f
                dispatchMoveFinished(moveInfo.holder)
                moves.removeAt(j)
                if (moves.isEmpty()) {
                    mMovesList.remove(moves)
                }
            }
        }
        for (i in mAdditionsList.size - 1 downTo 0) {
            val additions = mAdditionsList[i]
            count = additions.size
            for (j in count - 1 downTo 0) {
                val item = additions[j]
                setAddItemAnimationCancel(item)
                dispatchAddFinished(item)
                additions.removeAt(j)
                if (additions.isEmpty()) {
                    mAdditionsList.remove(additions)
                }
            }
        }
        for (i in mChangesList.size - 1 downTo 0) {
            val changes = mChangesList[i]
            count = changes.size
            for (j in count - 1 downTo 0) {
                endChangeAnimationIfNecessary(changes[j])
                if (changes.isEmpty()) {
                    mChangesList.remove(changes)
                }
            }
        }

        cancelAll(mRemoveAnimations)
        cancelAll(mMoveAnimations)
        cancelAll(mAddAnimations)
        cancelAll(mChangeAnimations)

        dispatchAnimationsFinished()
    }

    internal fun cancelAll(viewHolders: List<ViewHolder>) {
        for (i in viewHolders.size - 1 downTo 0) {
            viewHolders[i].itemView.animate().cancel()
        }
    }

    override fun canReuseUpdatedViewHolder(viewHolder: ViewHolder, payloads: MutableList<Any>): Boolean =
        payloads.isNotEmpty() || super.canReuseUpdatedViewHolder(viewHolder, payloads)

    abstract fun setAddItemAnimationInit(holder: ViewHolder?)

    abstract fun setAddItemAnimation(holder: ViewHolder?, animator: ViewPropertyAnimator?)

    abstract fun setAddItemAnimationCancel(holder: ViewHolder?)

    abstract fun setRemoveAnimation(holder: ViewHolder?, animator: ViewPropertyAnimator?)

    abstract fun setRemoveAnimationEnd(holder: ViewHolder?)

    abstract fun setOldChangeAnimation(holder: ViewHolder?, animator: ViewPropertyAnimator?)

    abstract fun setOldChangeAnimationEnd(holder: ViewHolder?)

    abstract fun setNewChangeAnimationInit(holder: ViewHolder?)

    abstract fun setNewChangeAnimation(holder: ViewHolder?, animator: ViewPropertyAnimator?)

    abstract fun setNewChangeAnimationEnd(holder: ViewHolder?)

    companion object {
        private const val DEBUG = false
        private var sDefaultInterpolator: TimeInterpolator? = null
    }
}
