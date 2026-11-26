package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ChangePasswordResult
import com.example.data.LockRepository
import com.example.domain.CopyFormat
import com.example.domain.PredictionEngine
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ANGKA BANDAR", appName)
    }

    @Test
    fun `digital root matches observed 5-digit pattern samples`() {
        // Sample blocks from dataset: 61123, 76753, 00922, 84000, 05281
        assertEquals(3, PredictionEngine.digitalRoot(1 + 2))
        assertEquals(3, PredictionEngine.digitalRoot(7 + 5))
        assertEquals(2, PredictionEngine.digitalRoot(9 + 2))
        assertEquals(0, PredictionEngine.digitalRoot(0 + 0))
        assertEquals(1, PredictionEngine.digitalRoot(2 + 8))
    }

    @Test
    fun `prediction engine always produces 100 unique 2D lines across 4 sheets`() {
        val samples = listOf("4829", "0000", "9999", "1234", "0707", "6112")
        for (sample in samples) {
            val result = PredictionEngine.generate2D(sample)
            assertEquals(sample, result.input4D)
            assertEquals(100, result.lines2D.size)
            assertEquals(100, result.lines2D.distinct().size)
            assertEquals(5, result.signature5D.length)
            assertTrue(result.lines2D.all { it.length == 2 && it.all(Char::isDigit) })
            assertEquals(result.lines2D.joinToString("*"), result.formatForClipboard(CopyFormat.STAR_SEPARATED))
        }
    }

    @Test
    fun `lock repository verifies default password DJOKER4444 and rotates password`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = LockRepository(context)

        assertTrue(repo.verifyPassword("DJOKER4444"))
        assertFalse(repo.verifyPassword("WRONG1234"))

        val wrongChange = repo.changePassword("WRONG", "NEWPASS99", "NEWPASS99")
        assertEquals(ChangePasswordResult.WrongCurrentPassword, wrongChange)

        val validChange = repo.changePassword("DJOKER4444", "VIPBANDAR88", "VIPBANDAR88")
        assertEquals(ChangePasswordResult.Success, validChange)

        assertFalse(repo.verifyPassword("DJOKER4444"))
        assertTrue(repo.verifyPassword("VIPBANDAR88"))
    }
}
