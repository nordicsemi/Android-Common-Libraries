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

import androidx.annotation.StringRes
import no.nordicsemi.android.common.scanner.R

sealed class SortingOption(
    @field:StringRes val title: Int,
    val comparator: Comparator<ScannedPeripheral>
)

/**
 * Sort results alphabetically by device name.
 * 
 * Devices are ordered based on their names in ascending order.
 *
 * Devices without a name will be sorted after those with names.
 */
class SortByName(
    @StringRes title: Int = R.string.sort_by_alphabetical,
) : SortingOption(
    title = title,
    comparator = Comparator { left, right ->
        val leftName = left.latestScanResult.advertisingData.name
        val rightName = right.latestScanResult.advertisingData.name

        when {
            leftName == null && rightName == null -> 0 // Both names are null
            leftName == null -> 1 // Only right has a name
            rightName == null -> -1 // Only left has a name
            else -> leftName.compareTo(rightName, ignoreCase = true)
        }
    }
)

/**
 * Sort results by highest RSSI value.
 */
class SortByRssi(
    @StringRes title: Int = R.string.sort_by_rssi,
) : SortingOption(
    title = title,
    comparator = Comparator { left, right ->
        // Sort by RSSI in descending order
        right.highestRssi.compareTo(left.highestRssi)
    }
)

/**
 * Sort results using a custom comparator.
 */
class CustomSorting(
    @StringRes title: Int,
    comparator: Comparator<ScannedPeripheral>
) : SortingOption(
    title = title,
    comparator = comparator
)

/**
 * Sort results by first found order.
 *
 * The comparator returns equals for any two scanned peripherals.
 */
data object SortingDisabled : SortingOption(
    title = R.string.sort_by_none,
    comparator = Comparator { _, _ -> 0 } // No sorting applied
)