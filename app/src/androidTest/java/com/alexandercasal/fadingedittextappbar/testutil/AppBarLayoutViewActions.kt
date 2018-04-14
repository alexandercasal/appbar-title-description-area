package com.alexandercasal.fadingedittextappbar.testutil

import android.support.design.widget.AppBarLayout
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.View

class AppBarLayoutViewActions {
    companion object {
        fun collapseAppBarLayout() = object : ViewAction {
            override fun getConstraints() = isAssignableFrom(AppBarLayout::class.java)

            override fun getDescription() = "Collapses an AppBarLayout"

            override fun perform(uiController: UiController, view: View) {
                (view as AppBarLayout).setExpanded(false)
                uiController.loopMainThreadForAtLeast(1000)
                uiController.loopMainThreadUntilIdle()
            }
        }

        fun expandAppBarLayout() = object : ViewAction {
            override fun getConstraints() = isAssignableFrom(AppBarLayout::class.java)

            override fun getDescription() = "Expands an AppBarLayout"

            override fun perform(uiController: UiController, view: View) {
                (view as AppBarLayout).setExpanded(true)
                uiController.loopMainThreadForAtLeast(1000)
                uiController.loopMainThreadUntilIdle()
            }
        }
    }
}