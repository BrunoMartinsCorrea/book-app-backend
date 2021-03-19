package com.github.brunomartinscorrea.config.container.modules

import com.github.brunomartinscorrea.adapter.service.UserService
import com.github.brunomartinscorrea.user.UserServiceImpl
import org.koin.dsl.module

val serviceModules = module {
    single<UserService> { UserServiceImpl(get()) }
}
