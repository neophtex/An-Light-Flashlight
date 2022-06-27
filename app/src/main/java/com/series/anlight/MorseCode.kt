package com.series.anlight


class MorseCode {

    private val alpha = arrayOf(
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
        "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8",
        "9", "0", "!", ",", "?", ".", "'"
    )

    private val morse = arrayOf(
        ".-",
        "-...",
        "-.-.",
        "-..",
        ".",
        "..-.",
        "--.",
        "....",
        "..",
        ".---",
        "-.-",
        ".-..",
        "--",
        "-.",
        "---",
        ".--.",
        "--.-",
        ".-.",
        "...",
        "-",
        "..-",
        "...-",
        ".--",
        "-..-",
        "-.--",
        "--..",
        ".----",
        "..---",
        "...--",
        "....-",
        ".....",
        "-....",
        "--...",
        "---..",
        "----.",
        "-----",
        "-.-.--",
        "--..--",
        "..--..",
        ".-.-.-",
        ".----."
    )

    fun decode(morseCode: String): String {
        var build = ""
        val change = morseCode.trim { it <= ' ' }
        val words = change.split("   ").toTypedArray()
        for (word in words) {
            for (letter in word.split(" ").toTypedArray()) {
                for (x in morse.indices) {
                    if (letter == morse[x]) build += alpha[x]
                }
            }
            build += " "
        }
        return build.uppercase()
    }

    fun decodeEnglish(englishCode: String): String {
        var build = ""
        val change = englishCode.trim { it <= ' ' }
        val words = change.split(" ").toTypedArray()
        for (word in words) {
            for (i in word.indices) {
                for (x in morse.indices) {
                    if (word.substring(i, i + 1).equals(alpha[x], ignoreCase = true)) {
                        build = build + morse[x] + " "
                    }
                }
            }
            build += "  "
        }
        return build
    }
}