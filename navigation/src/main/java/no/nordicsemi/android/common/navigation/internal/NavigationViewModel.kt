/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@file:Suppress("unused")

package no.nordicsemi.android.common.navigation.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
): ViewModel() {
    /** A flow of navigation events. */
    val events = navigationManager.events

    /**
     * The given [SavedStateHandle] will be used to store the navigation result.
     *
     * This has to be the instance given in navigation composable, as each ViewModel may received
     * a different instance of [SavedStateHandle] using Hilt injections.
     */
    fun use(savedStateHandle: SavedStateHandle) {
        navigationManager.savedStateHandle = savedStateHandle
    }

    /**
     * The given [Flow] will be used to get the destination hierarchy.
     */
    fun use(currentBackStackEntryFlow: Flow<NavBackStackEntry>) {
        navigationManager.currentBackStackEntryFlow = currentBackStackEntryFlow
    }

    /**
     * Navigates back to the previous destination sending cancellation result.
     */
    fun navigateUp() {
        navigationManager.navigateUp()
    }

    /**
     * After the navigation is completed, this method should be called to consume the event.
     * Otherwise, it will be emitted again. This covers a case, when the event was received, but
     * the consumer was destroyed before it could handle it.
     */
    fun consumeEvent(event: NavigationManager.Event) {
        navigationManager.consumeEvent(event)
    }

    override fun onCleared() {
        super.onCleared()
        navigationManager.savedStateHandle = null
        navigationManager.currentBackStackEntryFlow = null
    }
}