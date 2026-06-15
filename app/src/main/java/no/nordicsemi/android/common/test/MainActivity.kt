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

package no.nordicsemi.android.common.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.logger.view.LoggerAppBarIcon
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.NavigationView
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.popUpToStartDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.navigation.with
import no.nordicsemi.android.common.test.main.MainDestinations
import no.nordicsemi.android.common.test.simple.Advanced
import no.nordicsemi.android.common.test.simple.AdvancedDestination
import no.nordicsemi.android.common.test.simple.Hello
import no.nordicsemi.android.common.test.simple.HelloDialog
import no.nordicsemi.android.common.test.simple.ScannerDestinationId
import no.nordicsemi.android.common.test.simple.Settings
import no.nordicsemi.android.common.test.simple.SettingsDestination
import no.nordicsemi.android.common.test.tab.SecondDestinations
import no.nordicsemi.android.common.test.tab.ThirdDestination
import no.nordicsemi.android.common.test.tab.ThirdTab
import no.nordicsemi.android.common.theme.NordicActivity
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.ui.view.AppBarIcon
import no.nordicsemi.android.common.ui.view.NavigationDrawerDividerDefaults
import no.nordicsemi.android.common.ui.view.NavigationDrawerTitle
import no.nordicsemi.android.common.ui.view.NavigationDrawerTitleDefaults
import no.nordicsemi.android.common.ui.view.NordicLargeAppBar
import no.nordicsemi.android.common.ui.view.NordicLogo
import no.nordicsemi.kotlin.ble.core.android.AndroidEnvironment
import no.nordicsemi.kotlin.ble.environment.android.compose.LocalEnvironmentOwner
import javax.inject.Inject

data class Item(val title: String, val destinationId: DestinationId<Unit, *>, val icon: ImageVector)

val LocalScanningState = compositionLocalOf { mutableStateOf(false) }
val LocalFilterState = compositionLocalOf { mutableStateOf(false) }

val Tabs = createSimpleDestination("tabs")
val FirstTab = createSimpleDestination("first_tab")
val SecondTab = createSimpleDestination("second_tab")

private val menuItems = listOf(
    Item("Main", Tabs, Icons.Filled.Verified),
    Item("Settings", Settings, Icons.Filled.Settings),
)

private val advancedMenuItems = listOf(
    Item("Advanced Settings", Advanced, Icons.Filled.ImageSearch),
)

private val tabs = listOf(
    Item("Main", FirstTab, Icons.Filled.Verified),
    Item("Inner Navigation", SecondTab, Icons.Filled.Navigation),
    Item("Simple", ThirdTab, Icons.Filled.Album),
)

@AndroidEntryPoint
class MainActivity : NordicActivity() {

    @Inject
    lateinit var environment: AndroidEnvironment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NordicTheme {
                // The test app can run both as native or mock (there are 2 flavors).
                // For each flavor a different `environment` is created (using Hilt, see .di module).
                // Adding the environment to local composition allows to access it from any composable.
                // For mock environment the `provides` method returns 2 values:
                // - LocalEnvironment with the environment,
                // - LocalActivityResultRegistryOwner, that can intercept Bluetooth permission requests.
                CompositionLocalProvider(values = LocalEnvironmentOwner provides environment) {
                    Content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content() {
    val navigator: SimpleNavigationViewModel = hiltViewModel()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Note: This state is read in ScannerDestination.kt.
    @Suppress("VariableNeverRead")
    var isFilterOpen by LocalFilterState.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerState = drawerState,
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
            ) {
                val insets = WindowInsets.displayCutout
                    .only(WindowInsetsSides.Start)
                Column(
                    modifier = Modifier.padding(insets.asPaddingValues()),
                ) {
                    NordicLogo(
                        modifier = Modifier
                            .padding(NavigationDrawerTitleDefaults.ItemPadding)
                            .padding(vertical = 16.dp)
                    )

                    NavigationDrawerTitle(
                        title = "Menu",
                        modifier = Modifier.padding(NavigationDrawerTitleDefaults.ItemPadding)
                    )

                    NavigationDrawerItems(
                        items = menuItems,
                        navigator = navigator,
                        onDismiss = { scope.launch { drawerState.close() } },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(NavigationDrawerDividerDefaults.ItemPadding)
                    )

                    NavigationDrawerTitle(
                        title = "Advanced",
                        modifier = Modifier.padding(NavigationDrawerTitleDefaults.ItemPadding)
                    )

                    NavigationDrawerItems(
                        items = advancedMenuItems,
                        navigator = navigator,
                        onDismiss = { scope.launch { drawerState.close() } },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.BrokenImage,
                                contentDescription = null
                            )
                        },
                        label = { Text("This does nothing") },
                        selected = false,
                        onClick = { },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        val currentDestination by navigator.currentDestination()
            .collectAsStateWithLifecycle()
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                NordicLargeAppBar(
                    title = { Text(text = stringResource(id = R.string.title_main)) },
                    showBackButton = currentDestination in listOf(
                        Hello,
                        HelloDialog,
                        ScannerDestinationId
                    ),
                    onNavigationButtonClick = { navigator.navigateUp() },
                    onHamburgerButtonClick = {
                        scope.launch { drawerState.open() }
                    },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        if (navigator.isInHierarchy(ScannerDestinationId)
                                .collectAsStateWithLifecycle().value) {
                            if (LocalScanningState.current.value) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(4.dp).size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp,
                                )
                            }
                            AppBarIcon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                onClick = {
                                    isFilterOpen = true
                                }
                            )
                        }
                        val context = LocalContext.current
                        LoggerAppBarIcon(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Logger clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                )
            },
            bottomBar = {
                // Show the Navigation Bar only in the Tabs destination.
                if (navigator.isInHierarchy(Tabs).collectAsStateWithLifecycle().value) {
                    NavigationBar {
                        tabs.forEach { tab ->
                            val selected by navigator.isInHierarchy(tab.destinationId)
                                .collectAsStateWithLifecycle()
                            NavigationBarItem(
                                icon = { Icon(tab.icon, contentDescription = null) },
                                label = { Text(tab.title) },
                                selected = selected,
                                onClick = {
                                    // Checking if the tab is not selected here
                                    // is a workaround for an issue with how the navigation
                                    // restores the previous stack when back button was used.
                                    // See: https://issuetracker.google.com/issues/258237571
                                    if (!selected) {
                                        navigator.navigateTo(tab.destinationId) {
                                            popUpToStartDestination {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            },
            contentWindowInsets = WindowInsets.statusBars
        ) { padding ->
            // This is a workaround for the issue with the back button going back
            // to the previous tab instead of closing the drawer.
//                        BackHandler(
//                            enabled = drawerState.isOpen,
//                            onBack = { scope.launch { drawerState.close() } }
//                        )
            NavigationView(
                destinations = listOf(
                    Tabs with ((FirstTab with MainDestinations) + (SecondTab with SecondDestinations) + ThirdDestination),
                    SettingsDestination,
                    AdvancedDestination,
                ),
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun NavigationDrawerItems(
    items: List<Item>,
    navigator: Navigator,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    items.forEach { item ->
        val selected by navigator.isInHierarchy(item.destinationId).collectAsStateWithLifecycle()
        NavigationDrawerItem(
            icon = { Icon(item.icon, contentDescription = null) },
            label = { Text(item.title) },
            selected = selected,
            onClick = {
                onDismiss()
                // Checking if the tab is not selected here
                // is a workaround for an issue with how the navigation
                // restores the previous stack when back button was used.
                // See: https://issuetracker.google.com/issues/258237571
                if (!selected) {
                    navigator.navigateTo(item.destinationId) {
                        popUpToStartDestination {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = modifier,
        )
    }
}