package com.technologies.fabernovel.bottombanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.BaseTransientBottomBar


typealias AnimationContentViewCallback = com.google.android.material.snackbar.ContentViewCallback

class BottomBanner private constructor(
    parent: ViewGroup,
    content: View,
    callback: AnimationContentViewCallback
) : BaseTransientBottomBar<BottomBanner>(parent, content, callback) {
    private val bannerText: TextView by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById<TextView>(R.id.banner_text)
    }
    private val bannerIcon: ImageView by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById<ImageView>(R.id.banner_icon)
    }
    private val bannerAction: Button by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById<Button>(R.id.banner_action)
    }

    fun setText(text: CharSequence): BottomBanner {
        bannerText.text = text
        return this
    }

    fun setIcon(@DrawableRes drawableRes: Int): BottomBanner {
        bannerIcon.setImageResource(drawableRes)
        return this
    }

    fun setAction(text: CharSequence, callback: (View) -> Unit = {}): BottomBanner {
        bannerAction.text = text
        bannerAction.visibility = View.VISIBLE
        bannerAction.setOnClickListener { view ->
            callback(view)
            dismiss()
        }
        return this
    }

    fun setAction(text: CharSequence, listener: View.OnClickListener): BottomBanner {
        bannerAction.text = text
        bannerAction.visibility = View.VISIBLE
        bannerAction.setOnClickListener { view ->
            listener.onClick(view)
            dismiss()
        }
        return this
    }

    companion object {

        const val LENGTH_SHORT = BaseTransientBottomBar.LENGTH_SHORT
        const val LENGTH_LONG = BaseTransientBottomBar.LENGTH_LONG
        const val LENGTH_INDEFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(LENGTH_SHORT, LENGTH_LONG, LENGTH_INDEFINITE)
        annotation class BannerDuration

        private val animationCallback =
            object : AnimationContentViewCallback {
                override fun animateContentIn(delay: Int, duration: Int) {
                    /* no animations */
                }

                override fun animateContentOut(delay: Int, duration: Int) {
                    /* no animations */
                }
            }

        @JvmStatic
        fun make(view: View, text: CharSequence, @BannerDuration duration: Int): BottomBanner {
            val parent = findSuitableParent(view)
            if (parent == null) {
                throw IllegalArgumentException(
                    "No suitable parent found from the given view. Please provide a valid view."
                )
            }
            val inflater = LayoutInflater.from(view.context)
            val content = inflater.inflate(R.layout.view_bottom_banner, parent, false)
            val viewCallback = animationCallback
            val bottomBanner = BottomBanner(parent, content, viewCallback)
            bottomBanner.setText(text)
            bottomBanner.getView().setPadding(0, 0, 0, 0)
            bottomBanner.duration = duration
            return bottomBanner
        }

        private fun findSuitableParent(view: View): ViewGroup? {
            var currentView: View? = view
            var fallback: ViewGroup? = null
            do {
                when (currentView) {
                    is CoordinatorLayout -> return currentView
                    is FrameLayout -> if (currentView.id == android.R.id.content) {
                        return currentView
                    } else {
                        fallback = currentView
                    }
                }
                if (currentView != null) {
                    val parent = currentView.parent
                    currentView = if (parent is View) parent else null
                }
            } while (currentView != null)
            return fallback
        }
    }
}
