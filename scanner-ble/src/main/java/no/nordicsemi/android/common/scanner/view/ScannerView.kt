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

package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.permissions.ble.RequireBluetooth
import no.nordicsemi.android.common.permissions.ble.RequireLocation
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.android.common.scanner.ScanFilterState
import no.nordicsemi.android.common.scanner.data.ScannedPeripheral
import no.nordicsemi.android.common.scanner.rememberFilterState
import no.nordicsemi.android.common.scanner.spec.ServiceUuids
import no.nordicsemi.android.common.scanner.viewmodel.ScannerViewModel
import no.nordicsemi.android.common.scanner.viewmodel.ScanningState
import no.nordicsemi.android.common.scanner.viewmodel.UiState
import no.nordicsemi.android.common.ui.view.CircularIcon
import no.nordicsemi.android.common.ui.view.RssiIcon
import no.nordicsemi.kotlin.ble.client.android.AdvertisingData
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import no.nordicsemi.kotlin.ble.client.android.preview.PreviewPeripheral
import no.nordicsemi.kotlin.ble.core.Phy
import no.nordicsemi.kotlin.ble.core.PrimaryPhy
import kotlin.time.Duration
import kotlin.uuid.Uuid

@Composable
fun ScannerView(
    onScanResultSelected: (ScanResult) -> Unit,
    modifier: Modifier = Modifier,
    state: ScanFilterState = rememberFilterState(),
    timeout: Duration = Duration.INFINITE,
    onScanningStateChanged: (Boolean) -> Unit = {},
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    deviceItem: @Composable (ScanResult) -> Unit = { scanResult ->
        DeviceListItem(scanResult)
    },
) {
    val viewModel = hiltViewModel<ScannerViewModel>()
    viewModel.setFilterState(state)

    val isScanningChanged by rememberUpdatedState(onScanningStateChanged)

    RequireBluetooth(
        onChanged = { isEnabled ->
            if (!isEnabled) {
                isScanningChanged(false)
            }
        }
    ) {
        RequireLocation { isLocationRequiredAndDisabled ->
            LaunchedEffect(isLocationRequiredAndDisabled) {
                // This would start scanning on each orientation change,
                // but there is a flag set in the ViewModel to prevent that.
                // User needs to pull to refresh to start scanning again.
                viewModel.initiateScanning(timeout = timeout)
            }

            val pullToRefreshState = rememberPullToRefreshState()
            val scope = rememberCoroutineScope()

            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = {
                    viewModel.reload()
                    scope.launch {
                        pullToRefreshState.animateToHidden()
                    }
                },
                state = pullToRefreshState,
                modifier = modifier,
            ) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                DisposableEffect(uiState.isScanning) {
                    isScanningChanged(uiState.isScanning)
                    onDispose {
                        isScanningChanged(false)
                    }
                }
                ScannerContent(
                    isLocationRequiredAndDisabled = isLocationRequiredAndDisabled,
                    uiState = uiState,
                    onClick = onScanResultSelected,
                    verticalArrangement = verticalArrangement,
                    contentPadding = contentPadding,
                    deviceItem = deviceItem,
                )
            }
        }
    }
}

@Composable
internal fun ScannerContent(
    isLocationRequiredAndDisabled: Boolean,
    uiState: UiState,
    onClick: (ScanResult) -> Unit,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    deviceItem: @Composable (ScanResult) -> Unit,
) {
    when (uiState.scanningState) {
        is ScanningState.Loading ->
            ScanEmptyView(locationRequiredAndDisabled = isLocationRequiredAndDisabled)

        is ScanningState.Error -> ScanErrorView(error = uiState.scanningState.error)

        is ScanningState.DevicesDiscovered -> {
            if (uiState.scanningState.result.isEmpty()) {
                ScanEmptyView(locationRequiredAndDisabled = isLocationRequiredAndDisabled)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = contentPadding,
                    verticalArrangement = verticalArrangement,
                ) {
                    DeviceListItems(
                        devices = uiState.scanningState.result,
                        onScanResultSelected = onClick,
                        deviceItem = deviceItem
                    )
                }
            }
        }
    }
}

@Suppress("FunctionName")
internal fun LazyListScope.DeviceListItems(
    devices: List<ScannedPeripheral>,
    onScanResultSelected: (ScanResult) -> Unit,
    deviceItem: @Composable (ScanResult) -> Unit = { scanResult ->
        DeviceListItem(scanResult)
    },
) {
    items(devices) { device ->
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onScanResultSelected(device.latestScanResult) }
        ) {
            deviceItem(device.latestScanResult)
        }
    }
}

@Composable
fun DeviceListItem(
    result: ScanResult,
    customIconBuilder: (Uuid) -> Int? = { null },
) {
    val icon: Int = result.advertisingData.serviceUuids
        .firstNotNullOfOrNull {
            customIconBuilder(it) ?: ServiceUuids.getServiceInfo(it)?.icon
        }
        ?: result.advertisingData.meshBeacon?.let { R.drawable.ic_mesh }
        ?: result.advertisingData.meshMessage?.let { R.drawable.ic_mesh }
        ?: result.advertisingData.meshPbAdv?.let { R.drawable.ic_mesh }
        ?: R.drawable.outline_bluetooth_24

    DeviceListItem(
        iconPainter = painterResource(icon),
        title = result.advertisingData.name ?: result.peripheral.name
        ?: stringResource(R.string.no_name),
        subtitle = result.peripheral.address,
        trailingContent = {
            // Show RSSI if available
            RssiIcon(result.rssi)
        }
    )
}

@Composable
fun DeviceListItem(
    iconPainter: Painter?,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit = { },
) {
    DeviceListItem(
        modifier = modifier,
        iconPainter = iconPainter,
        headlineContent = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingContent = trailingContent
    )
}

@Composable
fun DeviceListItem(
    iconPainter: Painter?,
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlineContent: @Composable () -> Unit = {},
    supportingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) {
    OutlinedCard(modifier) {
        ListItem(
            headlineContent = headlineContent,
            overlineContent = overlineContent,
            supportingContent = supportingContent,
            leadingContent = {
                iconPainter?.let {
                    CircularIcon(painter = it)
                }
            },
            trailingContent = trailingContent,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScannerContentPreview_empty() {
    ScannerContent(
        isLocationRequiredAndDisabled = true,
        uiState = UiState(
            isScanning = true,
            scanningState = ScanningState.Loading,
        ),
        onClick = {},
        deviceItem = { DeviceListItem(it) }
    )
}

@Preview(showBackground = true)
@Composable
private fun ScannerContentPreview() {
    val scope = rememberCoroutineScope()

    ScannerContent(
        isLocationRequiredAndDisabled = false,
        uiState = UiState(
            isScanning = true,
            scanningState = ScanningState.DevicesDiscovered(
                result = listOf(
                    ScannedPeripheral(
                        scanResult = ScanResult(
                            peripheral = PreviewPeripheral(
                                scope = scope,
                                name = "Nordic HRM",
                                address = "12:34:56:78:9A:BC",
                            ),
                            isConnectable = true,
                            advertisingData = AdvertisingData(raw = byteArrayOf(0x02, 0x01, 0x06)),
                            rssi = -60,
                            txPowerLevel = null,
                            primaryPhy = PrimaryPhy.PHY_LE_1M,
                            secondaryPhy = Phy.PHY_LE_1M,
                            timestamp = System.currentTimeMillis(),
                        ),
                    ),
                    ScannedPeripheral(
                        scanResult = ScanResult(
                            peripheral = PreviewPeripheral(
                                scope = scope,
                                name = "A device with a very long name",
                                address = "00000000-1234-1234-5678-1234567890AB",
                            ),
                            isConnectable = true,
                            advertisingData = AdvertisingData(raw = byteArrayOf(0x02, 0x01, 0x06)),
                            rssi = -60,
                            txPowerLevel = null,
                            primaryPhy = PrimaryPhy.PHY_LE_1M,
                            secondaryPhy = Phy.PHY_LE_1M,
                            timestamp = System.currentTimeMillis(),
                        ),
                    )
                )
            ),
        ),
        onClick = {},
        deviceItem = { DeviceListItem(it) }
    )
}

@Preview(showBackground = true)
@Composable
private fun ScannerContentPreview_error() {
    ScannerContent(
        isLocationRequiredAndDisabled = false,
        uiState = UiState(
            isScanning = true,
            scanningState = ScanningState.Error("Internal error"),
        ),
        onClick = {},
        deviceItem = { DeviceListItem(it) }
    )
}

@Preview
@Composable
private fun DeviceListItemPreview() {
    DeviceListItem(
        iconPainter = painterResource(R.drawable.outline_bluetooth_24),
        title = "Nordic HRM",
        subtitle = "12:34:56:78:9A:BC",
        trailingContent = {
            RssiIcon(rssi = -60)
        }
    )
}
