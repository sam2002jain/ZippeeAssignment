import React, { useState, useEffect } from 'react';
import {
  View,
  StyleSheet,
  Alert,
  PermissionsAndroid,
  Platform,
  Text,
  ActivityIndicator,
} from 'react-native';
import MapView, { Marker, PROVIDER_GOOGLE } from 'react-native-maps';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { NativeModules } from 'react-native';

const { NearbyPlacesModule } = NativeModules;

interface Place {
  name: string;
  latitude: number;
  longitude: number;
  distance: number;
}

interface Region {
  latitude: number;
  longitude: number;
  latitudeDelta: number;
  longitudeDelta: number;
}

function App() {
  const [places, setPlaces] = useState<Place[]>([]);
  const [loading, setLoading] = useState(false);
  const [region, setRegion] = useState<Region>({
    latitude: 28.6139,
    longitude: 77.2090,
    latitudeDelta: 0.01,
    longitudeDelta: 0.01,
  });
  const [selectedPlace, setSelectedPlace] = useState<Place | null>(null);

  useEffect(() => {
    requestLocationPermission();
  }, []);

  const requestLocationPermission = async () => {
    if (Platform.OS === 'android') {
      try {
        const granted = await PermissionsAndroid.requestMultiple([
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION,
        ]);

        if (
          granted[PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION] === 'granted' ||
          granted[PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION] === 'granted'
        ) {
          loadNearbyPlaces();
        } else {
          Alert.alert('Permission Denied', 'Location permission is required to show nearby places.');
        }
      } catch (err) {
        console.warn(err);
      }
    } else {
      loadNearbyPlaces();
    }
  };

  const loadNearbyPlaces = async () => {
    setLoading(true);
    try {
      const result = await NearbyPlacesModule.getNearbyPlaces();
      setPlaces(result.places);
      
      // Update region to center on first place if available
      if (result.places.length > 0) {
        const firstPlace = result.places[0];
        setRegion({
          latitude: firstPlace.latitude,
          longitude: firstPlace.longitude,
          latitudeDelta: 0.01,
          longitudeDelta: 0.01,
        });
      }
    } catch (error) {
      console.error('Error loading places:', error);
      Alert.alert('Error', 'Failed to load nearby places. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const onMarkerPress = (place: Place) => {
    setSelectedPlace(place);
    Alert.alert(
      place.name,
      `Distance: ${place.distance.toFixed(0)} meters`,
      [{ text: 'OK', onPress: () => setSelectedPlace(null) }]
    );
  };

  return (
    <SafeAreaProvider>
      <View style={styles.container}>
        <MapView
          provider={PROVIDER_GOOGLE}
          style={styles.map}
          region={region}
          onRegionChangeComplete={setRegion}
        >
          {places.map((place, index) => (
            <Marker
              key={index}
              coordinate={{
                latitude: place.latitude,
                longitude: place.longitude,
              }}
              title={place.name}
              description={`${place.distance.toFixed(0)}m away`}
              onPress={() => onMarkerPress(place)}
            />
          ))}
        </MapView>
        
        {loading && (
          <View style={styles.loadingOverlay}>
            <ActivityIndicator size="large" color="#0066cc" />
            <Text style={styles.loadingText}>Loading nearby places...</Text>
          </View>
        )}
        
        {selectedPlace && (
          <View style={styles.placeInfo}>
            <Text style={styles.placeName}>{selectedPlace.name}</Text>
            <Text style={styles.placeDistance}>
              Distance: {selectedPlace.distance.toFixed(0)} meters
            </Text>
          </View>
        )}
      </View>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    flex: 1,
  },
  loadingOverlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(255, 255, 255, 0.8)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    marginTop: 10,
    fontSize: 16,
    color: '#0066cc',
  },
  placeInfo: {
    position: 'absolute',
    bottom: 50,
    left: 20,
    right: 20,
    backgroundColor: 'white',
    padding: 15,
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  placeName: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
  },
  placeDistance: {
    fontSize: 14,
    color: '#666',
    marginTop: 5,
  },
});

export default App;
