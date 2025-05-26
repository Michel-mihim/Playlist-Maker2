package com.example.playlist_maker2.utils.converters

fun phraseDurationGenerator(msDuration: Int?): String {
    var word = ""
    var preLastChar: Char? = null
    var lastChar: Char? = null

    val secDuration = msDuration?.div(1000)

    if (secDuration!! < 60)
    {//менее минуты=================================================================================
        lastChar = secDuration.toString().last()
        if (secDuration.toString().length >= 2) {
            preLastChar = secDuration.toString()[secDuration.toString().length - 2]
        }

        when (preLastChar) {
            '1' -> {
                word = secDuration.toString() + " секунд"
            }

            else -> {
                when (lastChar) {
                    '1' -> word = secDuration.toString() + " секунда"
                    '2' -> word = secDuration.toString() + " секунды"
                    '3' -> word = secDuration.toString() + " секунды"
                    '4' -> word = secDuration.toString() + " секунды"
                    else -> word = secDuration.toString() + " секунд"
                }
            }
        }
    } else {//более минуты==========================================================================
        val minMuration = secDuration.div(60)

        lastChar = minMuration.toString().last()
        if (minMuration.toString().length >= 2) {
            preLastChar = minMuration.toString()[minMuration.toString().length - 2]
        }

        when (preLastChar) {
            '1' -> {
                word = minMuration.toString() + " минут"
            }

            else -> {
                when (lastChar) {
                    '1' -> word = minMuration.toString() + " минута"
                    '2' -> word = minMuration.toString() + " минуты"
                    '3' -> word = minMuration.toString() + " минуты"
                    '4' -> word = minMuration.toString() + " минуты"
                    else -> word = minMuration.toString() + " минут"
                }
            }
        }
    }


    return word
}

fun phraseTrackGenerator(tracksCount: Int?): String {
    var word = ""
    var preLastChar: Char? = null
    var lastChar: Char? = null

    lastChar = tracksCount.toString().last()
    if (tracksCount.toString().length >= 2) {
        preLastChar = tracksCount.toString()[tracksCount.toString().length - 2]
    }

    when (preLastChar) {
        '1' -> {
            word = tracksCount.toString() + " треков"
        }

        else -> {
            when (lastChar) {
                '1' -> word = tracksCount.toString() + " трек"
                '2' -> word = tracksCount.toString() + " трека"
                '3' -> word = tracksCount.toString() + " трека"
                '4' -> word = tracksCount.toString() + " трека"
                else -> word = tracksCount.toString() + " треков"
            }
        }
    }
    return word
}

fun wordModifier(tracksCount: Int?): String {
    var word = ""
    var preLastChar: Char? = null
    var lastChar: Char? = null

    lastChar = tracksCount.toString().last()
    if (tracksCount.toString().length >= 2) {
        preLastChar = tracksCount.toString()[tracksCount.toString().length - 2]
    }

    when (preLastChar) {
        '1' -> {
            word = " треков"
        }

        else -> {
            when (lastChar) {
                '1' -> word = " трек"
                '2' -> word = " трека"
                '3' -> word = " трека"
                '4' -> word = " трека"
                else -> word = " треков"
            }
        }
    }
    return word
}