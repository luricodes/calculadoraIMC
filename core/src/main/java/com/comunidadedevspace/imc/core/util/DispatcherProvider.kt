package com.comunidadedevspace.imc.core.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher

    class Default
        @Inject
        constructor(
            private val ioDispatcher: CoroutineDispatcher,
        ) : DispatcherProvider {
            override val io: CoroutineDispatcher = ioDispatcher
            override val default: CoroutineDispatcher = Dispatchers.Default
            override val main: CoroutineDispatcher = Dispatchers.Main
        }
}
