package com.undp.fleettracker.network.models

import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 * @author: << sandip.mahajan >>
 */

class GenericResponse<T>(
    @field:NonNull @param:NonNull val status: ResponseStatus,
    @field:Nullable @param:Nullable val data: T,
    @field:Nullable @param:Nullable val message: String?
) {

    enum class ResponseStatus {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(@Nullable data: T): GenericResponse<T> {
            return GenericResponse(ResponseStatus.SUCCESS, data, null)
        }

        fun <T> error(@NonNull msg: String?, @Nullable data: T): GenericResponse<T> {
            return GenericResponse(ResponseStatus.ERROR, data, msg)
        }

        fun <T> loading(@Nullable data: T): GenericResponse<T> {
            return GenericResponse(ResponseStatus.LOADING, data, null)
        }
    }
}