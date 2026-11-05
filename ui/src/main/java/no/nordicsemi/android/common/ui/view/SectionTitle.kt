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

@file:Suppress("unused")

package no.nordicsemi.android.common.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.ui.R

/**
 * A section title is a component that displays a title with an icon
 * and optionally a menu.
 *
 * The menu does not make the title taller: it takes the height of the icon. A button in the
 * menu (40-48 dp) is centered on it and may overlap the padding around the title, so the title
 * has the same space around it with and without the menu.
 *
 * @param painter The painter to be displayed.
 * @param title The title of the section.
 * @param modifier The modifier to be applied to the layout.
 * @param menu The menu to be displayed on the right side of the title.
 */
@Composable
fun SectionTitle(
    painter: Painter,
    title: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    menu: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = tint,
        )
        Text(
            text = title,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        menu?.let {
            Row(
                modifier = Modifier
                    .height(24.dp) // The height of the icon.
                    .wrapContentHeight(align = Alignment.CenterVertically, unbounded = true),
                verticalAlignment = Alignment.CenterVertically,
                content = it,
            )
        }
    }
}

/**
 * A section title is a component that displays a title with an icon
 * and optionally a menu.
 *
 * @param icon The icon to be displayed.
 * @param title The title of the section.
 * @param modifier The modifier to be applied to the layout.
 * @param menu The menu to be displayed on the right side of the title.
 */
@Composable
fun SectionTitle(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    menu: @Composable (RowScope.() -> Unit)? = null,
) {
    SectionTitle(
        painter = rememberVectorPainter(image = icon),
        title = title,
        modifier = modifier,
        tint = tint,
        menu = menu,
    )
}

@Preview(showBackground = true)
@Composable
private fun SectionTitlePreview() {
    SectionTitle(
        painter = painterResource(R.drawable.baseline_add_24),
        title = "Title",
        menu = {
            Switch(
                checked = true,
                onCheckedChange = {},
            )
            OutlinedButton(
                onClick = {},
            ) {
                Text("Button")
            }
        }
    )
}