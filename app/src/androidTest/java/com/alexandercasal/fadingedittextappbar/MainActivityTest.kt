package com.alexandercasal.fadingedittextappbar

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withAlpha
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.alexandercasal.fadingedittextappbar.testutil.AppBarLayoutViewActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Rule @JvmField
    val mMainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun appBarTitleSharedBetweenToolbarAndDetailViewWhenScrolling() {
        // Initial State
        onView(withId(R.id.appbar_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar_title)).check(matches(withAlpha(0f)))
        onView(withId(R.id.details_linear_layout)).check(matches(withAlpha(1f)))

        // Set Content
        onView(withId(R.id.et_title)).perform(typeText("The Title"))
        onView(withId(R.id.et_description)).perform(typeText("The Description"))

        // Collapse Action Bar
        onView(withId(R.id.appbar_layout)).perform(AppBarLayoutViewActions.collapseAppBarLayout())

        // Collapsed State
        onView(withId(R.id.toolbar_title)).check(matches(withAlpha(1f)))
        onView(withId(R.id.toolbar_title)).check(matches(withText("The Title")))
        onView(withId(R.id.details_linear_layout)).check(matches(withAlpha(0f)))

        // Expand Action Bar
        onView(withId(R.id.appbar_layout)).perform(AppBarLayoutViewActions.expandAppBarLayout())

        // Expanded Sate
        onView(withId(R.id.toolbar_title)).check(matches(withAlpha(0f)))
        onView(withId(R.id.details_linear_layout)).check(matches(withAlpha(1f)))
        onView(withId(R.id.et_title)).check(matches(withText("The Title")))
        onView(withId(R.id.et_description)).check(matches(withText("The Description")))
    }
}