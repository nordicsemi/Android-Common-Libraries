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

package no.nordicsemi.android.common.test.simple

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.scanner.data.CustomFilter
import no.nordicsemi.android.common.scanner.data.OnlyNearby
import no.nordicsemi.android.common.scanner.data.OnlyWithNames
import no.nordicsemi.android.common.scanner.rememberFilterState
import no.nordicsemi.android.common.scanner.view.FilterDialog
import no.nordicsemi.android.common.scanner.view.ScannerView
import no.nordicsemi.android.common.test.LocalFilterState
import no.nordicsemi.android.common.test.LocalScanningState
import no.nordicsemi.android.common.test.R
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import no.nordicsemi.kotlin.ble.core.util.fromShortUuid
import kotlin.uuid.Uuid

val ScannerDestinationId = createDestination<Unit, ScanResult>("ble-scanner")

val ScannerDestination = defineDestination(ScannerDestinationId) {
    val navigationVM = hiltViewModel<SimpleNavigationViewModel>()

    // Use ScannerScreen if you need an App Bar included.
//    ScannerScreen(
//        cancellable = true,
//        state = rememberFilterState(
//            dynamicFilters = listOf(
//                OnlyNearby(),
//                OnlyWithNames(),
//                WithServiceUuid(
//                    title = R.string.filter_hrm,
//                    icon = Icons.Default.MonitorHeart,
//                    uuid = Uuid.fromShortUuid(0x180D), // Heart Rate Service UUID,
//                    isInitiallySelected = true
//                )
//            )
//        ),
//        onResultSelected = {
//            when (it) {
//                is DeviceSelected -> {
//                    navigationVM.navigateUpWithResult(ScannerDestinationId, it.scanResult)
//                }
//
//                ScanningCancelled -> {
//                    navigationVM.navigateUp()
//                }
//            }
//        }
//    )

    // Or Scanner View and Filter Dialog if you control it from own App Bar.
    var isScanning by LocalScanningState.current
    var isFilterOpen by LocalFilterState.current
    val state = rememberFilterState(
        // Here's an option to set global filtering based on the advertising data.
//        filter = {
//            ServiceUuid(Uuid.fromShortUuid(0x180D))
//        },
        // Show all devices, not only connectable.
        scanResultFilter = { true },
        // Specify some dynamic filters.
        dynamicFilters = listOf(
            OnlyNearby(),
            OnlyWithNames(),
            CustomFilter(
                title = R.string.filter_show_all,
                predicate = { selected, result, _ ->
                    if (selected) {
                        !result.advertisingData.serviceUuids.contains(Uuid.fromShortUuid(0x180D))
                    } else {
                        true
                    }
                }
            )
        )
    )

    val insets = WindowInsets.displayCutout
        .only(WindowInsetsSides.Horizontal)
    ScannerView(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets)
            .consumeWindowInsets(insets)
            .padding(horizontal = 16.dp),
        onScanResultSelected = {
            navigationVM.navigateUpWithResult(ScannerDestinationId, it)
        },
        state = state,
        onScanningStateChanged = {
            isScanning = it
        },
    )

    if (isFilterOpen) {
        FilterDialog(
            state = state,
            onDismissRequest = { isFilterOpen = false },
        )
    }
}
