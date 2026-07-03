# Module scanner-ble

Implementation of Nordic's common Bluetooth LE scanner screen and UI components.

# Package no.nordicsemi.android.common.scanner

The entry point for common BLE scanner.

This package contains the main entry point for the BLE scanner, including the [ScannerScreen] 
composable and the [ScanFilterState] for managing its state. It also defines the result types 
returned when a device is selected or the process is canceled.

# Package no.nordicsemi.android.common.scanner.data

Data models for customizing the scanner.

This package provides the data models used by the scanner, along with a set of predefined [Filter]s 
and [SortingOption]s. These allow for custom filtering logic based on device names, proximity (RSSI), 
or service UUIDs, and sorting based on name or signal strength.

# Package no.nordicsemi.android.common.scanner.view

UI components, views, etc.

This package contains the UI components that make up the scanner screen. It includes the [ScannerView] 
for displaying the device list, a specialized [ScannerAppBar], and various supporting views for 
handling empty states, errors, and filter selection.
