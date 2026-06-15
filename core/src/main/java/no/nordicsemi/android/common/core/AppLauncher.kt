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

package no.nordicsemi.android.common.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.net.toUri

/**
 * Base link to the Google Play Store.
 */
const val GOOGLE_PLAY_LINK = "https://play.google.com/store/apps/details?id="

/**
 * AppLauncher is a utility class that can be used to launch an app with a given package name.
 *
 * If the activity is not installed, the Google Play Store will be opened.
 */
@Suppress("unused")
object AppLauncher {
    /**
     * Opens the app with supplied package name, or opens Google Play if the app is not installed.
     */
    fun lunch(
        packageManager: PackageManager,
        packageName: String,
        context: Context
    ) {
        when (isPackageInstalled(packageName, packageManager)) {
            true -> openApp(packageManager, packageName, context)
            false -> try {
                openGooglePlay(packageName, context)
            } catch (e: ActivityNotFoundException) {
                // An Exception will be thrown if the Play Store is not installed on the target device.
                e.printStackTrace()
            }
        }
    }

    private fun openGooglePlay(packageName: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, (GOOGLE_PLAY_LINK + packageName).toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun openApp(
        packageManager: PackageManager,
        packageName: String,
        context: Context
    ) {
        val launchIntent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        launchIntent?.let { context.startActivity(it) }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            val _ = packageManager.getPackageInfoCompat(packageName)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    private fun PackageManager.getPackageInfoCompat(
        packageName: String,
        flags: Int = 0
    ): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            getPackageInfo(packageName, flags)
        }
    }
}