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

package no.nordicsemi.android.common.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource

/**
 * `NordicTheme` is a custom theme for the Nordic Android applications.
 */
@Composable
fun NordicTheme(content: @Composable () -> Unit) {
    val darkColorPalette = darkColorScheme(
        primary = colorResource(id = R.color.md_theme_primary),
        onPrimary = colorResource(id = R.color.md_theme_onPrimary),
        primaryContainer = colorResource(id = R.color.md_theme_primaryContainer),
        onPrimaryContainer = colorResource(id = R.color.md_theme_onPrimaryContainer),
        inversePrimary = colorResource(id = R.color.md_theme_primaryInverse),
        secondary = colorResource(id = R.color.md_theme_secondary),
        onSecondary = colorResource(id = R.color.md_theme_onSecondary),
        secondaryContainer = colorResource(id = R.color.md_theme_secondaryContainer),
        onSecondaryContainer = colorResource(id = R.color.md_theme_onSecondaryContainer),
        tertiary = colorResource(id = R.color.md_theme_tertiary),
        onTertiary = colorResource(id = R.color.md_theme_onTertiary),
        tertiaryContainer = colorResource(id = R.color.md_theme_tertiaryContainer),
        onTertiaryContainer = colorResource(id = R.color.md_theme_onTertiaryContainer),
        background = colorResource(id = R.color.md_theme_background),
        onBackground = colorResource(id = R.color.md_theme_onBackground),
        surface = colorResource(id = R.color.md_theme_surface),
        onSurface = colorResource(id = R.color.md_theme_onSurface),
        surfaceContainer = colorResource(id = R.color.md_theme_surfaceContainer),
        surfaceContainerLow = colorResource(id = R.color.md_theme_surfaceContainerLow),
        surfaceContainerHigh = colorResource(id = R.color.md_theme_surfaceContainerHigh),
        surfaceContainerHighest = colorResource(id = R.color.md_theme_surfaceContainerHighest),
        surfaceVariant = colorResource(id = R.color.md_theme_surfaceVariant),
        onSurfaceVariant = colorResource(id = R.color.md_theme_onSurfaceVariant),
        inverseSurface = colorResource(id = R.color.md_theme_inverseSurface),
        inverseOnSurface = colorResource(id = R.color.md_theme_inverseOnSurface),
        error = colorResource(id = R.color.md_theme_error),
        onError = colorResource(id = R.color.md_theme_onError),
        errorContainer = colorResource(id = R.color.md_theme_errorContainer),
        onErrorContainer = colorResource(id = R.color.md_theme_onErrorContainer),
        outline = colorResource(id = R.color.md_theme_outline),
    )

    val lightColorPalette = lightColorScheme(
        primary = colorResource(id = R.color.md_theme_primary),
        onPrimary = colorResource(id = R.color.md_theme_onPrimary),
        primaryContainer = colorResource(id = R.color.md_theme_primaryContainer),
        onPrimaryContainer = colorResource(id = R.color.md_theme_onPrimaryContainer),
        inversePrimary = colorResource(id = R.color.md_theme_primaryInverse),
        secondary = colorResource(id = R.color.md_theme_secondary),
        onSecondary = colorResource(id = R.color.md_theme_onSecondary),
        secondaryContainer = colorResource(id = R.color.md_theme_secondaryContainer),
        onSecondaryContainer = colorResource(id = R.color.md_theme_onSecondaryContainer),
        tertiary = colorResource(id = R.color.md_theme_tertiary),
        onTertiary = colorResource(id = R.color.md_theme_onTertiary),
        tertiaryContainer = colorResource(id = R.color.md_theme_tertiaryContainer),
        onTertiaryContainer = colorResource(id = R.color.md_theme_onTertiaryContainer),
        background = colorResource(id = R.color.md_theme_background),
        onBackground = colorResource(id = R.color.md_theme_onBackground),
        surface = colorResource(id = R.color.md_theme_surface),
        surfaceContainer = colorResource(id = R.color.md_theme_surfaceContainer),
        surfaceContainerLow = colorResource(id = R.color.md_theme_surfaceContainerLow),
        surfaceContainerHigh = colorResource(id = R.color.md_theme_surfaceContainerHigh),
        surfaceContainerHighest = colorResource(id = R.color.md_theme_surfaceContainerHighest),
        onSurface = colorResource(id = R.color.md_theme_onSurface),
        surfaceVariant = colorResource(id = R.color.md_theme_surfaceVariant),
        onSurfaceVariant = colorResource(id = R.color.md_theme_onSurfaceVariant),
        inverseSurface = colorResource(id = R.color.md_theme_inverseSurface),
        inverseOnSurface = colorResource(id = R.color.md_theme_inverseOnSurface),
        error = colorResource(id = R.color.md_theme_error),
        onError = colorResource(id = R.color.md_theme_onError),
        errorContainer = colorResource(id = R.color.md_theme_errorContainer),
        onErrorContainer = colorResource(id = R.color.md_theme_onErrorContainer),
        outline = colorResource(id = R.color.md_theme_outline),
    )

    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = nordicTypography,
    ) {
        val background = colorScheme.background

        CompositionLocalProvider(
            LocalContentColor provides contentColorFor(background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = background),
            ) {
                content()
            }
        }
    }
}
