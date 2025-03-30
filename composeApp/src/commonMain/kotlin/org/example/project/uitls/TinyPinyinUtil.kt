package org.example.project.uitls

import org.example.project.uitls.TinyPinyinUtil.getPinyinFirstLetter

expect object TinyPinyinUtil {
    fun getPinyinFirstLetter(ch: Char): Char
}

fun getFirstLetter(name: String): Char {
    if (name.isEmpty()) return '#'

    val firstChar = name.first()

    return when {
        firstChar.isLetter() -> firstChar.uppercaseChar() // A-Z
        isChinese(firstChar) -> getPinyinFirstLetter(firstChar) // 转拼音
        else -> '#' // 其他归类到 #
    }
}

fun isChinese(char: Char): Boolean {
    return char in '\u4e00'..'\u9fff'
}