package com.dilip.country_code_picker

class Utils {

    companion object {
        /**
         * Get emoji flag from country iso code using unicode
         * @param isoString country iso code
         * @return emoji flag using unicode
         */
        fun getEmojiFlag(isoString: String): String {
            return isoString.uppercase().map { char -> Character.codePointAt("$char", 0) + 0x1F1A5 }
                .joinToString("") {
                    String(Character.toChars(it))
                }
        }

    }
}