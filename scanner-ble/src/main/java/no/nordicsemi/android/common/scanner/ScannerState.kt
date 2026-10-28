/*
 * Copyright (c) 2025, Nordic Semiconductor
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

package no.nordicsemi.android.common.scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * The state of the scanner, which allows to control it from outside of the scanner view.
 *
 * Create it using [rememberScannerState] and pass it to the scanner screen or view.
 */
@Stable
class ScannerState internal constructor() {
    private val refreshChannel = Channel<Unit>(Channel.CONFLATED)

    /**
     * Emits a value each time [refresh] was called.
     */
    internal val refreshRequests: Flow<Unit> = refreshChannel.receiveAsFlow()

    /**
     * Whether the scanner is currently scanning.
     *
     * It is `false` when the scanner is not shown, Bluetooth is disabled, or the scan has finished.
     */
    val isScanning: StateFlow<Boolean>
        field = MutableStateFlow(false)

    internal fun setScanning(scanning: Boolean) {
        isScanning.value = scanning
    }

    /**
     * Clears the list of devices and restarts scanning.
     *
     * This does the same as the pull-to-refresh gesture. If the scanner is not shown yet, for
     * example because Bluetooth is disabled or permissions are missing, the request is performed
     * when it appears. Multiple requests made in the meantime are merged into one.
     */
    fun refresh() {
        refreshChannel.trySend(Unit)
    }
}

/**
 * Creates a [ScannerState] that can be used to control the scanner.
 */
@Composable
fun rememberScannerState(): ScannerState = remember { ScannerState() }
