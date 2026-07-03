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

package no.nordicsemi.android.common.scanner.spec

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import no.nordicsemi.android.common.scanner.R
import kotlin.uuid.Uuid

internal val HTS_SERVICE_UUID= Uuid.parse("00001809-0000-1000-8000-00805f9b34fb")
internal val BPS_SERVICE_UUID= Uuid.parse("00001810-0000-1000-8000-00805f9b34fb")
internal val CSC_SERVICE_UUID= Uuid.parse("00001816-0000-1000-8000-00805f9b34fb")
internal val CGMS_SERVICE_UUID= Uuid.parse("0000181F-0000-1000-8000-00805f9b34fb")
internal val DF_SERVICE_UUID= Uuid.parse("21490000-494a-4573-98af-f126af76f490")
internal val GLS_SERVICE_UUID= Uuid.parse("00001808-0000-1000-8000-00805f9b34fb")
internal val HRS_SERVICE_UUID= Uuid.parse("0000180D-0000-1000-8000-00805f9b34fb")
internal val PRX_SERVICE_UUID= Uuid.parse("00001802-0000-1000-8000-00805f9b34fb")
internal val RSCS_SERVICE_UUID= Uuid.parse("00001814-0000-1000-8000-00805F9B34FB")
internal val UART_SERVICE_UUID= Uuid.parse("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
internal val BATTERY_SERVICE_UUID= Uuid.parse("0000180F-0000-1000-8000-00805f9b34fb")
internal val THROUGHPUT_SERVICE_UUID = Uuid.parse("0483DADD-6C9D-6CA9-5D41-03AD4FFF4ABB")
internal val CHANNEL_SOUND_SERVICE_UUID = Uuid.parse("0000185B-0000-1000-8000-00805F9B34FB")
// Nordic Service UUIDs
internal val MDS_SERVICE_UUID = Uuid.parse("54220000-f6a5-4007-a371-722f4ebd8436")
internal val LBS_SERVICE_UUID = Uuid.parse("00001523-1212-EFDE-1523-785FEABCD123")
internal val QUICK_START_54L_SERVICE_UUID = Uuid.parse("b2007aaa-c203-43a5-8b6f-a7f3d001a1e0")
internal val MESH_PROVISIONING_SERVICE_UUID = Uuid.parse("00001827-0000-1000-8000-00805F9B34FB")
internal val MESH_PROXY_SERVICE_UUID = Uuid.parse("00001828-0000-1000-8000-00805F9B34FB")
internal val MESH_PROXY_SOLICITATION_SERVICE_UUID = Uuid.parse("00001829-0000-1000-8000-00805F9B34FB")

// TODO: make a callback which checks the uuid and returns the name and icon for the service.
object ServiceUuids {

    @Composable
    fun getServiceInfo(uuid: Uuid): ServiceNameWithIcon? = when (uuid) {
        HTS_SERVICE_UUID -> ServiceNameWithIcon("Health Thermometer", R.drawable.baseline_thermostat_24)
        BPS_SERVICE_UUID -> ServiceNameWithIcon("Blood Pressure", R.drawable.baseline_bloodtype_24)
        CSC_SERVICE_UUID -> ServiceNameWithIcon(
            "Cycling Speed and Cadence",
            R.drawable.baseline_directions_bike_24,
        )
        CGMS_SERVICE_UUID -> ServiceNameWithIcon(
            "Continuous Glucose Monitoring",
            R.drawable.baseline_water_drop_24
        )
        DF_SERVICE_UUID -> ServiceNameWithIcon("Direction Finding", R.drawable.baseline_my_location_24)
        GLS_SERVICE_UUID -> ServiceNameWithIcon("Glucose", R.drawable.baseline_water_drop_24)
        HRS_SERVICE_UUID -> ServiceNameWithIcon("Heart Rate", R.drawable.baseline_monitor_heart_24)
        PRX_SERVICE_UUID -> ServiceNameWithIcon("Proximity", R.drawable.baseline_thermostat_24)
        RSCS_SERVICE_UUID -> ServiceNameWithIcon(
            "Running Speed and Cadence",
            R.drawable.baseline_directions_run_24
        )

        UART_SERVICE_UUID -> ServiceNameWithIcon("UART", R.drawable.baseline_sync_alt_24)
        BATTERY_SERVICE_UUID -> ServiceNameWithIcon("Battery", R.drawable.outline_battery_full_24)
        THROUGHPUT_SERVICE_UUID -> ServiceNameWithIcon("Throughput", R.drawable.baseline_sync_alt_24)
        CHANNEL_SOUND_SERVICE_UUID -> ServiceNameWithIcon(
            "Channel Sounding",
            R.drawable.baseline_my_location_24
        )
        MDS_SERVICE_UUID -> ServiceNameWithIcon(
            "Monitoring & Diagnostics",
            R.drawable.ic_memfault_app_logo
        )
        LBS_SERVICE_UUID -> ServiceNameWithIcon("LED Button", R.drawable.outline_lightbulb_2_24)
        QUICK_START_54L_SERVICE_UUID -> ServiceNameWithIcon("Quick Start", R.drawable.ic_quick_start)
        MESH_PROXY_SERVICE_UUID,
        MESH_PROXY_SOLICITATION_SERVICE_UUID,
        MESH_PROVISIONING_SERVICE_UUID -> ServiceNameWithIcon("Mesh", R.drawable.ic_mesh)

        else -> null
    }
}

data class ServiceNameWithIcon(
    val service: String,
    @field:DrawableRes val icon: Int,
)