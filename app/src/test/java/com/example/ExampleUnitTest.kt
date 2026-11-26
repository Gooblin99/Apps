package com.example

import com.example.domain.CopyFormat
import com.example.domain.PredictionEngine
import com.example.domain.PredictionSheet
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun digitalRoot_matches_observed_5digit_pattern_samples() {
        assertEquals(3, PredictionEngine.digitalRoot(1 + 2))
        assertEquals(3, PredictionEngine.digitalRoot(7 + 5))
        assertEquals(2, PredictionEngine.digitalRoot(9 + 2))
        assertEquals(0, PredictionEngine.digitalRoot(0 + 0))
        assertEquals(1, PredictionEngine.digitalRoot(2 + 8))
    }

    @Test
    fun predictionEngine_generates_100_lines_split_into_4_sheets_of_25_with_star_separator() {
        val samples = listOf("4829", "0000", "9999", "1234", "0707", "6112")
        for (sample in samples) {
            val result = PredictionEngine.generate2D(sample)
            assertEquals(sample, result.input4D)
            assertEquals(100, result.lines2D.size)
            assertEquals(100, result.lines2D.distinct().size)
            assertTrue(result.lines2D.all { it.length == 2 && it.all(Char::isDigit) })

            val allSheetLines = mutableListOf<String>()
            for (sheet in PredictionSheet.entries) {
                val sheetLines = result.linesForSheet(sheet)
                assertEquals(25, sheetLines.size)
                allSheetLines.addAll(sheetLines)
                val starFormatted = result.formatSheetStarSeparated(sheet)
                assertFalse(starFormatted.contains(' '))
                assertEquals(24, starFormatted.count { it == '*' })
            }
            assertEquals(100, allSheetLines.distinct().size)
            assertEquals(result.lines2D.joinToString("*"), result.formatForClipboard(CopyFormat.STAR_SEPARATED))
        }
    }
}
