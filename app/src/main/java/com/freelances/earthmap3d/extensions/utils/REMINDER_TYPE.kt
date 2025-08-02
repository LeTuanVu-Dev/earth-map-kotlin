package com.earthmap.map.ltv.tracker.com.freelances.earthmap3d.extensions.utils

enum class REMINDER_TYPE(val value: Int) {
    LOCK_FIRST_TYPE(0),
    LOCK_SECOND_TYPE(1),
    LOCK_THIRD_TYPE(2),
    LOCK_FOURTH_TYPE(3),
    LOCK_FIFTH_TYPE(4);

    companion object {
        fun valueOfName(value: Int): REMINDER_TYPE {
            return entries.find { it.value == value } ?: LOCK_FIRST_TYPE
        }
    }
}