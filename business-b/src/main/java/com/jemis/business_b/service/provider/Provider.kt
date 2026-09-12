package com.jemis.business_b.service.provider

import com.jemis.base.interfaces.IUserService
import com.therouter.inject.ServiceProvider

@ServiceProvider
fun getUserInfo(): IUserService {
    return object : IUserService {
        override fun getUserInfo(): String {
            return "This is a user from business b"
        }
    }
}