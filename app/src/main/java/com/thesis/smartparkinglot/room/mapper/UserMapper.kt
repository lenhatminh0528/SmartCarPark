package com.thesis.smartparkinglot.room.mapper

import com.thesis.smartparkinglot.retrofit.response.UserResponse
import com.thesis.smartparkinglot.room.entities.UserEntity

object UserMapper {
    fun responseToEntity(userResponse: UserResponse) : UserEntity {
        return UserEntity(
            id = userResponse.id,
            cardId = userResponse.cardId,
            carNumber = userResponse.carNumber,
            address = userResponse.address,
            userName = userResponse.username,
            linkQr = userResponse.linkQr
        )
    }
}