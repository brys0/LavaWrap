package brys.dev.lavawrap.Lib.Util

import java.lang.IllegalStateException
import java.lang.NumberFormatException
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Time module for Lavawrap
 * @author [Brys](http://brys.tk)
 */
object Time {
   private val TIMESTAMP_PATTERN = Pattern.compile("^(\\d?\\d)(?::([0-5]?\\d))?(?::([0-5]?\\d))?$")
    fun formatMili(ms: Long): String {
        val hours = ms / TimeUnit.HOURS.toMillis(1)
        val minutes = ms / TimeUnit.MINUTES.toMillis(1)
        val seconds = ms % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    fun seekTime(str: String): Long {
        val millis: Long
        var seconds: Long = 0
        var minutes: Long = 0
        var hours: Long = 0
        val m = TIMESTAMP_PATTERN.matcher(str)
        check(m.find()) { "Unable to match $str" }
        var capturedGroups = 0
        if (m.group(1) != null) capturedGroups++
        if (m.group(2) != null) capturedGroups++
        if (m.group(3) != null) capturedGroups++
        when (capturedGroups) {
            0 -> throw IllegalStateException("Unable to match $str")
            1 -> seconds = m.group(1).toInt().toLong()
            2 -> {
                minutes = m.group(1).toInt().toLong()
                seconds = m.group(2).toInt().toLong()
            }
            3 -> {
                hours = m.group(1).toInt().toLong()
                minutes = m.group(2).toInt().toLong()
                seconds = m.group(3).toInt().toLong()
            }
        }
        minutes += hours * 60
        seconds += minutes * 60
        millis = seconds * 1000
        return millis
    }

    fun convertToTitleCase(text: String?): String? {
        if (text == null || text.isEmpty()) {
            return text
        }
        val converted = StringBuilder()
        var convertNext = true
        for (ch: Char in text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true
            } else if (convertNext) {
               var char = Character.toTitleCase(ch)
                convertNext = false
            } else {
              var char = Character.toLowerCase(ch)
            }
            converted.append(ch)
        }
        return converted.toString()
    }

}