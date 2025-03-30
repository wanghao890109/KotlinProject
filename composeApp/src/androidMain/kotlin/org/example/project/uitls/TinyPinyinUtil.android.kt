package org.example.project.uitls

import com.github.promeg.pinyinhelper.Pinyin

actual object TinyPinyinUtil {
    actual fun getPinyinFirstLetter(ch: Char): Char {
        return Pinyin.toPinyin(ch).first().uppercaseChar() // 获取拼音首字母
    }
}