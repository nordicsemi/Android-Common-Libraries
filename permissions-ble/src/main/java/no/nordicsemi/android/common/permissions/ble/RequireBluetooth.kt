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

package no.nordicsemi.android.common.permissions.ble

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.permissions.ble.view.BluetoothDisabledView
import no.nordicsemi.android.common.permissions.ble.view.BluetoothNotAvailableView
import no.nordicsemi.android.common.permissions.ble.view.BluetoothPermissionRequiredView
import no.nordicsemi.android.common.permissions.ble.viewmodel.PermissionViewModel
import no.nordicsemi.kotlin.ble.core.Manager

/**
 * The reason why the BLE permission is not available.
 */
enum class BlePermissionNotAvailableReason {
    /** Bluetooth Scan permission is required. */
    PERMISSION_REQUIRED,
    /** Bluetooth is not available on this device. */
    NOT_AVAILABLE,
    /** Bluetooth is disabled. */
    DISABLED,
}

/**
 * A wrapper for composables that require Bluetooth.
 *
 * This composable will display a view based on the Bluetooth state and permissions.
 *
 * On Android 12+ it will require `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT` and `BLUETOOTH_ADVERTISE`
 * permission to be granted and will show a view allowing requesting them.
 *
 * ### Example:
 * ```kotlin
 * RequireBluetooth(
 *     onChanged = { onScanningStateChanged(it) }
 * ) {
 *     RequireLocation(
 *         onChanged = { onScanningStateChanged(it) }
 *     ) {
 *         // Bluetooth scanner views
 *     }
 * }
 * ```
 *
 * @param onChanged A callback that will be called when the state of the Bluetooth changes.
 * @param contentWithoutBluetooth A composable that will be displayed when Bluetooth is not available.
 * @param content A composable that will be displayed when Bluetooth is available.
 * @see BlePermissionNotAvailableReason
 */
@Composable
fun RequireBluetooth(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutBluetooth: @Composable (BlePermissionNotAvailableReason) -> Unit = {
        NoBluetoothView(reason = it)
    },
    content: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val environment = viewModel.environment
    val state by environment.bluetoothState.collectAsStateWithLifecycle()
    val permissionGranted by viewModel.bluetoothPermissionFlow.collectAsStateWithLifecycle()

    LaunchedEffect(state, permissionGranted) {
        val bluetoothEnabled = state == Manager.State.POWERED_ON
        onChanged(bluetoothEnabled && permissionGranted)
    }

    when  {
        state == Manager.State.UNSUPPORTED -> contentWithoutBluetooth(BlePermissionNotAvailableReason.NOT_AVAILABLE)
        // First check the BLUETOOTH_CONNECT permission.
        // It's required to enable Bluetooth.
        !permissionGranted -> contentWithoutBluetooth(BlePermissionNotAvailableReason.PERMISSION_REQUIRED)
        state == Manager.State.POWERED_ON -> content()
        else -> contentWithoutBluetooth(BlePermissionNotAvailableReason.DISABLED)
    }
}

@Composable
private fun NoBluetoothView(
    reason: BlePermissionNotAvailableReason,
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val environment = viewModel.environment

    when (reason) {
        BlePermissionNotAvailableReason.NOT_AVAILABLE ->
            BluetoothNotAvailableView()

        BlePermissionNotAvailableReason.PERMISSION_REQUIRED ->
            BluetoothPermissionRequiredView()

        BlePermissionNotAvailableReason.DISABLED ->
            BluetoothDisabledView(
                onEnable = { environment.enableBluetooth() }
            )
    }
}
