package com.freelances.earthmap3d.base

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object PreferencesCoinPoster {
    // MutableSharedFlow để emit Boolean; replay = 0 không giữ lại giá trị cũ
    private val _boolFlow = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1
    )
    // Expose SharedFlow chỉ đọc
    val coinFlow: SharedFlow<Boolean> = _boolFlow.asSharedFlow()

    /** 
     * Suspend function để post giá trị Boolean.
     * Nếu buffer đầy, emit sẽ suspend cho tới khi có collector tiêu thụ.
     */
    suspend fun post(value: Boolean) {
        _boolFlow.emit(value)
    }

    /**
     * Non-suspending emit; trả về false nếu buffer đầy và không emit được.
     */
    fun tryPost(value: Boolean): Boolean {
        return _boolFlow.tryEmit(value)
    }
}
