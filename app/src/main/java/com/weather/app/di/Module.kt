package com.weather.app.di

import com.weather.app.data.datasource.remote.SearchNetworkClient
import com.weather.app.data.datasource.remote.repository.SearchRepositoryImpl
import com.weather.app.domain.repository.SearchRepository
import com.weather.app.domain.usecase.SearchUseCase
import com.weather.app.framework.network.NetworkHandler
import com.weather.app.framework.network.ktorHttpClient
import com.weather.app.presentation.search.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun injectFeatures() = weatherAppFeatures

private val weatherAppFeatures by lazy {
    loadKoinModules(
        arrayListOf(
            useCaseModule,
            repositoryModule,
            dataSourceModule,
            viewModelModule,
            utilsModule,
        )
    )
}

val utilsModule: Module = module {
    single { Dispatchers.IO }
    single { ktorHttpClient() }
    single { NetworkHandler(androidContext()) }
}

val useCaseModule: Module = module {
    factoryOf(::SearchUseCase)
}

val repositoryModule: Module = module {
    singleOf(::SearchRepositoryImpl) {
        bind<SearchRepository>()
    }
}

val dataSourceModule: Module = module {
    singleOf(::SearchNetworkClient)
}

val viewModelModule: Module = module {
    viewModelOf(::SearchViewModel)
}
