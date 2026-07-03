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

@file:Suppress("unused")

package no.nordicsemi.android.common.scanner.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import kotlin.uuid.Uuid

sealed class Filter(
    @field:StringRes val title: Int,
    @field:DrawableRes val icon: Int,
    val isInitiallySelected: Boolean,
    val predicate: (selected: Boolean, result: ScanResult, highestRssi: Int) -> Boolean,
)

/**
 * Filter that allows scan results with no empty names.
 *
 * This filter will only allow devices that have a non-empty name in their advertising data.
 * If the device does not advertise a name, it will be excluded from the results.
 *
 * @param title The title of the filter..
 * @param isInitiallySelected Whether the filter is initially selected, defaults to `false`.
 */
class OnlyWithNames(
    @StringRes title: Int = R.string.filter_only_with_names,
    isInitiallySelected: Boolean = false,
) : Filter(
    title = title,
    icon = R.drawable.baseline_label_24,
    isInitiallySelected = isInitiallySelected,
    predicate = { selected, result, _ ->
        !selected || result.advertisingData.name?.isNotEmpty() == true
    },
)

/**
 * Filter nearby devices based on RSSI value.
 *
 * It will allow devices with RSSI value greater or equal to the given RSSI value.
 *
 * @param rssiThreshold The RSSI threshold to filter nearby devices, defaults to `-50 dBm`.
 * @param title The title of the filter.
 * @param isInitiallySelected Whether the filter is initially selected, defaults to `false`.
 */
class OnlyNearby(
    rssiThreshold: Int = -50, // Default RSSI value to filter nearby devices
    @StringRes title: Int = R.string.filter_only_nearby,
    isInitiallySelected: Boolean = false,
) : Filter(
    title = title,
    icon = R.drawable.baseline_my_location_24,
    isInitiallySelected = isInitiallySelected,
    predicate = { selected, _, highestRssi ->
        !selected || highestRssi >= rssiThreshold
    }
)

/**
 * Filter devices based on a Service UUID.
 *
 * This filter will match devices that advertise the given service UUID in their advertising data,
 * service data, or service solicitation UUIDs.
 *
 * @param uuid The UUID to filter.
 * @param icon The icon to display for the filter.
 * @param title The title of the filter.
 * @param isInitiallySelected Whether the filter is initially selected, defaults to `false`.
 */
class WithServiceUuid(
    uuid: Uuid,
    @DrawableRes icon: Int = R.drawable.baseline_check_24,
    @StringRes title: Int = R.string.filter_with_service_uuid,
    isInitiallySelected: Boolean = false,
) : Filter(
    title = title,
    icon = icon,
    isInitiallySelected = isInitiallySelected,
    predicate = { selected, result, _ ->
        !selected ||
        result.advertisingData.serviceUuids.contains(uuid) ||
        result.advertisingData.serviceData.keys.any { it == uuid } ||
        result.advertisingData.serviceSolicitationUuids.contains(uuid)
    }
)

/**
 * Custom filter.
 *
 * The filter shows only devices that match the given predicate.
 *
 * @param title The title of the filter.
 * @param icon The icon to display for the filter.
 * @param isInitiallySelected Whether the filter is initially selected, defaults to `false`.
 * @param predicate The predicate function that takes the selection state, scan result, and highest RSSI,
 *                  and returns `true` if the result should be included in the filtered list.
 */
class CustomFilter(
    @StringRes title: Int,
    @DrawableRes icon: Int = R.drawable.baseline_check_24,
    isInitiallySelected: Boolean = false,
    predicate:  (selected: Boolean, result: ScanResult, highestRssi: Int) -> Boolean,
) : Filter(
    title = title,
    icon = icon,
    isInitiallySelected = isInitiallySelected,
    predicate = predicate
)

