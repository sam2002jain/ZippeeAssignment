# Nearby Places Map App

A React Native app with Android Native MVVM architecture for displaying nearby places on a map.

## Architecture

### Android Native (MVVM)
- **Model**: `Place.kt` - Data model for places
- **View**: React Native UI components
- **ViewModel**: `NearbyPlacesViewModel.kt` - Business logic and state management
- **Repository**: `PlacesRepository.kt` - Location and places data handling

### React Native
- Map display using `react-native-maps`
- Native module integration for location data
- Permission handling for location access

## Features

- ✅ Current location detection
- ✅ Mock nearby places data
- ✅ Distance calculation
- ✅ Map with markers
- ✅ Marker tap to show place info
- ✅ Runtime permission handling
- ✅ Error state management
- ✅ Async operations with proper loading states

## Setup & Run

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Android setup**:
   ```bash
   cd android
   ./gradlew clean
   cd ..
   ```

3. **Run the app**:
   ```bash
   npx react-native run-android
   ```

## Key Implementation Details

### MVVM Separation
- **No business logic in React Native** - All location and data logic is in Android native code
- **ViewModel manages state** - Uses LiveData for reactive updates
- **Repository pattern** - Handles location services and data fetching
- **Native Module bridge** - Exposes Android functionality to React Native

### Async Operations
- All operations are non-blocking using Kotlin coroutines
- Proper error handling for location permissions and services
- Loading states managed in ViewModel

### Error Handling
- Permission denied scenarios
- Location service unavailable
- Network/data fetching errors

## Google Maps API Key

The app includes a placeholder API key in `AndroidManifest.xml`. For production use, replace with your own Google Maps API key.

## Mock Data

The app uses mock nearby places data as acceptable for interview purposes. In production, this would integrate with a real places API like Google Places API.

---

## Original React Native Setup Instructions

This is a new [**React Native**](https://reactnative.dev) project, bootstrapped using [`@react-native-community/cli`](https://github.com/react-native-community/cli).

> **Note**: Make sure you have completed the [Set Up Your Environment](https://reactnative.dev/docs/set-up-your-environment) guide before proceeding.

### Troubleshooting

If you're having issues getting the above steps to work, see the [Troubleshooting](https://reactnative.dev/docs/troubleshooting) page.