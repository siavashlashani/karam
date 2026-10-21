package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.toPersianDigits
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name and verify persian digits`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("کارام", appName)
        assertEquals("۱۲۳", 123.toPersianDigits())
    }
}
