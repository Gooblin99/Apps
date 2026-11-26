package com.example.domain

enum class CopyFormat(val label: String) {
    STAR_SEPARATED("Format: 12*34"),
    NEWLINE("Format: Baris"),
}

enum class PredictionSheet(
    val title: String,
    val shortName: String,
    val rangeLabel: String,
    val startIndex: Int,
) {
    RED("LEMBAR MERAH", "Merah", "L001–L025", 0),
    GREEN("LEMBAR HIJAU", "Hijau", "L026–L050", 25),
    YELLOW("LEMBAR KUNING", "Kuning", "L051–L075", 50),
    BLUE("LEMBAR BIRU", "Biru", "L076–L100", 75),
}

data class PredictionResult(
    val input4D: String,
    val asDigit: Int,
    val kopDigit: Int,
    val kepalaDigit: Int,
    val ekorDigit: Int,
    val biji2D: Int,
    val signature5D: String,
    val lines2D: List<String>,
    val angkaMain: List<Int>,
) {
    fun linesForSheet(sheet: PredictionSheet): List<String> =
        lines2D.drop(sheet.startIndex).take(25)

    fun formatSheetStarSeparated(sheet: PredictionSheet): String =
        linesForSheet(sheet).joinToString("*")

    fun formatForClipboard(format: CopyFormat): String {
        return when (format) {
            CopyFormat.STAR_SEPARATED -> lines2D.joinToString("*")
            CopyFormat.NEWLINE -> buildString {
                append("ANGKA BANDAR • 4D: ")
                append(input4D)
                append(" (100 Line • Pola ")
                append(signature5D)
                append(")\n")
                PredictionSheet.entries.forEach { sheet ->
                    append(sheet.title)
                    append(" (")
                    append(sheet.rangeLabel)
                    append("): ")
                    append(formatSheetStarSeparated(sheet))
                    if (sheet != PredictionSheet.BLUE) append('\n')
                }
            }
        }
    }
}

object PredictionEngine {

    const val TARGET_LINE_COUNT = 100

    private val mistikLamaMap = intArrayOf(1, 0, 5, 8, 7, 2, 9, 4, 3, 6)
    private val mistikBaruMap = intArrayOf(8, 7, 6, 9, 5, 4, 2, 1, 0, 3)

    /**
     * Pola digit ke-5 pada tabel acakan 5-digit [A B C D E]:
     * E = Digital Root dari (C + D), yaitu 0 jika C+D == 0, selainnya 1..9.
     */
    fun digitalRoot(sum: Int): Int {
        if (sum <= 0) return 0
        return 1 + (sum - 1) % 9
    }

    private fun indexDigit(d: Int): Int = (d + 5) % 10

    private fun mistikLama(d: Int): Int = mistikLamaMap[d]

    private fun mistikBaru(d: Int): Int = mistikBaruMap[d]

    private fun pairStr(tens: Int, ones: Int): String = "${tens % 10}${ones % 10}"

    fun generate2D(rawInput: String): PredictionResult {
        val clean = rawInput.filter { it.isDigit() }
        require(clean.length == 4) { "Result 4D harus terdiri dari tepat 4 digit angka." }

        val asD = clean[0].digitToInt()
        val kopD = clean[1].digitToInt()
        val kepD = clean[2].digitToInt()
        val ekorD = clean[3].digitToInt()

        val biji2D = digitalRoot(kepD + ekorD)
        val bijiAsKop = digitalRoot(asD + kopD)
        val biji4D = digitalRoot(asD + kopD + kepD + ekorD)
        val signature5D = "$clean$biji2D"

        val uniqueLines = LinkedHashSet<String>(TARGET_LINE_COUNT)

        // 1. Inti kombinasi langsung & transformasi 4D + Biji
        val coreCandidates = listOf(
            pairStr(kepD, ekorD),
            pairStr(ekorD, kepD),
            pairStr(asD, ekorD),
            pairStr(kopD, kepD),
            pairStr(biji2D, ekorD),
            pairStr(kepD, biji2D),
            pairStr(mistikBaru(kepD), ekorD),
            pairStr(kepD, mistikLama(ekorD)),
            pairStr(indexDigit(kepD), ekorD),
            pairStr(kepD, indexDigit(ekorD)),
            pairStr(bijiAsKop, biji2D),
            pairStr(biji4D, biji2D),
        )
        for (candidate in coreCandidates) {
            if (uniqueLines.size >= TARGET_LINE_COUNT) break
            uniqueLines.add(candidate)
        }

        // 2. Acakan pseudo-acak terstruktur mengikuti pola distribusi Biji (Digital Root) dari tabel 5-digit
        val targetBijiSet = setOf(
            biji2D,
            biji4D,
            digitalRoot(asD + ekorD),
            digitalRoot(kopD + kepD),
            digitalRoot(biji2D + 4),
        )

        var prngState = (
            asD * 10000L +
                kopD * 1000L +
                kepD * 100L +
                ekorD * 10L +
                biji2D
            ) xor 0x5DEECE66DL

        fun nextRandomByte(): Int {
            prngState = prngState * 6364136223846793005L + 1442695040888963407L
            return ((prngState ushr 33) and 0x7FFFFFFFL).toInt()
        }

        // Bangun permutasi acak deterministik dari 00..99
        val shuffledPool = (0..99).toMutableList()
        for (i in shuffledPool.lastIndex downTo 1) {
            val j = nextRandomByte() % (i + 1)
            val tmp = shuffledPool[i]
            shuffledPool[i] = shuffledPool[j]
            shuffledPool[j] = tmp
        }

        // Prioritaskan angka 2D dari pool acak yang harmonis dengan Biji aktif (maks 50 slot pertama)
        for (num in shuffledPool) {
            if (uniqueLines.size >= 50) break
            val t = num / 10
            val u = num % 10
            if (digitalRoot(t + u) in targetBijiSet) {
                uniqueLines.add(String.format("%02d", num))
            }
        }

        // Lengkapi hingga tepat 100 line unik dari pool acak yang tersisa
        for (num in shuffledPool) {
            if (uniqueLines.size >= TARGET_LINE_COUNT) break
            uniqueLines.add(String.format("%02d", num))
        }

        val mainDigits = listOf(
            kepD,
            ekorD,
            biji2D,
            indexDigit(kepD),
            mistikBaru(ekorD),
            biji4D,
        ).distinct().take(4)

        return PredictionResult(
            input4D = clean,
            asDigit = asD,
            kopDigit = kopD,
            kepalaDigit = kepD,
            ekorDigit = ekorD,
            biji2D = biji2D,
            signature5D = signature5D,
            lines2D = uniqueLines.toList(),
            angkaMain = mainDigits,
        )
    }
}
