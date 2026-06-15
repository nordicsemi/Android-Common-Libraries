/*
 * Copyright (c) 2022, Nordic Semiconductor
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

plugins {
    alias(libs.plugins.nordic.android.application)
    alias(libs.plugins.nordic.feature.hilt.compose)
}

group = "no.nordicsemi.android.common"

android {
    namespace = "no.nordicsemi.android.common.test"

    flavorDimensions += listOf("mode")
    productFlavors {
        create("native") {
            isDefault = true
            dimension = "mode"
        }
        create("mock") {
            dimension = "mode"
        }
    }
}

dependencies {
    implementation(project(":theme"))
    implementation(project(":ui"))
    implementation(project(":logger"))
    implementation(project(":navigation"))
    implementation(project(":permissions-ble"))
    implementation(project(":permissions-nfc"))
    implementation(project(":permissions-wifi"))
    implementation(project(":permissions-internet"))
    implementation(project(":permissions-notification"))
    implementation(project(":scanner-ble"))

    // The sample app defines 2 flavors (see above): native and mock.
    // Each of them is using a different environment implementation.
    "nativeImplementation"(nordic.blek.client.android)
    "mockImplementation"(nordic.blek.client.android.mock)
    // And both use the Compose extension. This extension has its own
    // optional dependencies to both of them (compileOnly), so once of
    // the above has to be included.
    implementation(nordic.blek.environment.android.compose)

    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.activity.compose)

    // LeakCanary should only run in debug builds.
    debugImplementation(libs.leakcanary)

    // Temporary fix:
    // After updating Kotlin to 2.4.0 there's no Hilt (Dagger) version yet updated.
    // Build fails with error:
    // [Hilt] Provided Metadata instance has version 2.4.0, while maximum supported version is 2.3.0.
    //        To support newer versions, update the kotlin-metadata-jvm library.
    ksp(libs.kotlin.metadata.jvm)
}
