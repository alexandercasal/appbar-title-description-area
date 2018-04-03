package com.alexandercasal.fadingedittextappbar

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

class TitleDetailsAppBarLayout : AppBarLayout, AppBarLayout.OnOffsetChangedListener {

    private val mTitleViewID: Int
    private var mTitleView: View? = null
    private var mTitleAnimationState = ANIM_STATE_NONE
    private var mTitleVisibility = View.INVISIBLE
    private val mDetailViewID: Int
    private var mDetailView: View? = null
    private var mDetailsAnimationState = ANIM_STATE_NONE
    private var mDetailsVisibility = View.VISIBLE

    constructor(context: Context): super(context) {
        mTitleViewID = NOT_SET
        mDetailViewID = NOT_SET
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.TitleDetailsAppBarLayout,
                0,
                0
        )

        try {
            mTitleViewID = a.getResourceId(R.styleable.TitleDetailsAppBarLayout_titleView, NOT_SET)
            mDetailViewID = a.getResourceId(R.styleable.TitleDetailsAppBarLayout_detailView, NOT_SET)
        } finally {
            a.recycle()
        }
    }

    /**
     * Should be called once while initializing the View. Ensure the Title and Detail
     * views have already been set, otherwise this will have no effect.
     */
    private fun setTitleAndDetailInitialVisibility() {
        mTitleView?.alpha = if (mTitleVisibility == View.VISIBLE) 1f else 0f
        mDetailView?.alpha = if (mDetailsVisibility == View.VISIBLE) 1f else 0f
    }

    override fun onSaveInstanceState(): Parcelable {
        val state = Bundle()

        state.putParcelable(SAVE_STATE_SUPER, super.onSaveInstanceState())
        state.putInt(SAVE_STATE_TITLE_VISIBILITY, mTitleVisibility)
        state.putInt(SAVE_STATE_DETAILS_VISIBILITY, mDetailsVisibility)

        return state
    }

    override fun onRestoreInstanceState(savedState: Parcelable?) {
        if (savedState is Bundle) {
            super.onRestoreInstanceState(savedState.getParcelable<Parcelable>(SAVE_STATE_SUPER))
            mTitleVisibility = savedState.getInt(SAVE_STATE_TITLE_VISIBILITY)
            mDetailsVisibility = savedState.getInt(SAVE_STATE_DETAILS_VISIBILITY)
            setTitleAndDetailInitialVisibility()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mTitleViewID == NOT_SET && mDetailViewID == NOT_SET) {
            return
        }

        mTitleView = findViewById(mTitleViewID)
        mDetailView = findViewById(mDetailViewID)
        setTitleAndDetailInitialVisibility()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (layoutParams !is CoordinatorLayout.LayoutParams || parent !is CoordinatorLayout) {
            throw IllegalStateException("TitleDetailsAppBarLayout must be a direct child of " +
                    "CoordinatorLayout")
        }

        addOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val maxScrollRange = appBarLayout.totalScrollRange
        val offsetPercentage = Math.abs(verticalOffset).toFloat() / maxScrollRange.toFloat()

        if (offsetPercentage > FADE_THRESHOLD) {
            setShouldShowTitle(true)
            setShouldShowDetails(false)
        } else {
            setShouldShowTitle(false)
            setShouldShowDetails(true)
        }
    }

    /**
     * @param showTitle true to fade-in the Title; false to fade-out the title
     */
    private fun setShouldShowTitle(showTitle: Boolean) {
        mTitleView?.let { title ->
            if (showTitle) {
                if (isOrWillBeShown(mTitleVisibility, mTitleAnimationState)) return
                title.animate().cancel()
                mTitleAnimationState = ANIM_STATE_SHOWING
                title.animate()
                        .alpha(1f)
                        .setDuration(FADE_SPEED)
                        .withEndAction {
                            mTitleAnimationState = ANIM_STATE_NONE
                            mTitleVisibility = View.VISIBLE
                        }
            } else {
                if (isOrWillBeHidden(mTitleVisibility, mTitleAnimationState)) return
                title.animate().cancel()
                mTitleAnimationState = ANIM_STATE_HIDING
                title.animate()
                        .alpha(0f)
                        .setDuration(FADE_SPEED)
                        .withEndAction {
                            mTitleAnimationState = ANIM_STATE_NONE
                            mTitleVisibility = View.INVISIBLE
                        }
            }
        }
    }

    /**
     * @param showDetails true to fade-in the Details; false to fade-out the details
     */
    private fun setShouldShowDetails(showDetails: Boolean) {
        mDetailView?.let { details ->
            if (showDetails) {
                if (isOrWillBeShown(mDetailsVisibility, mDetailsAnimationState)) return
                details.animate().cancel()
                mDetailsAnimationState = ANIM_STATE_SHOWING
                details.animate()
                        .alpha(1f)
                        .setDuration(FADE_SPEED)
                        .withEndAction {
                            mDetailsAnimationState = ANIM_STATE_NONE
                            mDetailsVisibility = View.VISIBLE
                        }
            } else {
                if (isOrWillBeHidden(mDetailsVisibility, mDetailsAnimationState)) return
                details.animate().cancel()
                mDetailsAnimationState = ANIM_STATE_HIDING
                details.animate()
                        .alpha(0f)
                        .setDuration(FADE_SPEED)
                        .withEndAction {
                            mDetailsAnimationState = ANIM_STATE_NONE
                            mDetailsVisibility = View.INVISIBLE
                        }
            }
        }
    }

    /**
     * @param currentVisibility [View.VISIBLE] or [View.INVISIBLE]
     * @param animationState [ANIM_STATE_NONE] or [ANIM_STATE_HIDING] or [ANIM_STATE_SHOWING]
     * @return true if in a shown state; false if in a hidden state
     */
    private fun isOrWillBeShown(currentVisibility: Int, animationState: Int): Boolean {
        return if (currentVisibility != View.VISIBLE) {
            animationState == ANIM_STATE_SHOWING
        } else {
            animationState != ANIM_STATE_HIDING
        }
    }

    /**
     * @param currentVisibility [View.VISIBLE] or [View.INVISIBLE]
     * @param animationState [ANIM_STATE_NONE] or [ANIM_STATE_HIDING] or [ANIM_STATE_SHOWING]
     * @return true if in a hidden state; false if in a shown state
     */
    private fun isOrWillBeHidden(currentVisibility: Int, animationState: Int): Boolean {
        return if (currentVisibility == View.VISIBLE) {
            animationState == ANIM_STATE_HIDING
        } else {
            animationState != ANIM_STATE_SHOWING
        }
    }

    companion object {
        private const val NOT_SET = -1
        private const val FADE_THRESHOLD = 0.2f
        private const val FADE_SPEED = 200L
        private const val ANIM_STATE_NONE = 0
        private const val ANIM_STATE_HIDING = 1
        private const val ANIM_STATE_SHOWING = 2

        private const val SAVE_STATE_SUPER = "com.alexandercasal.fadingedittextappbar.SUPER_STATE"
        private const val SAVE_STATE_TITLE_VISIBILITY = "com.alexandercasal.fadingedittextappbar.TITLE_VISIBILITY"
        private const val SAVE_STATE_DETAILS_VISIBILITY = "com.alexandercasal.fadingedittextappbar.DETAILS_VISIBILITY"
    }
}