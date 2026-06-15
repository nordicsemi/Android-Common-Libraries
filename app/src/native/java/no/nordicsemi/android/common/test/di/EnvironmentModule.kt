@file:Suppress("unused")

package no.nordicsemi.android.common.test.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.ActivityRetainedLifecycle
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import no.nordicsemi.kotlin.ble.core.android.AndroidEnvironment
import no.nordicsemi.kotlin.ble.environment.android.NativeAndroidEnvironment

@Module
@InstallIn(ActivityRetainedComponent::class)
object EnvironmentModule {

    @ActivityRetainedScoped
    @Provides
    fun provideEnvironment(
        @ApplicationContext context: Context,
        lifecycle: ActivityRetainedLifecycle,
    ): NativeAndroidEnvironment {
        // Make sure the environment is closed when the lifecycle is cleared.
        // This will unregister the broadcast receiver.
        return NativeAndroidEnvironment.getInstance(context, isNeverForLocationFlagSet = true)
            .also { 
                lifecycle.addOnClearedListener { it.close() }
            }
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AndroidEnvironmentModule {

    @Binds
    abstract fun bindEnvironment(environment: NativeAndroidEnvironment): AndroidEnvironment
}