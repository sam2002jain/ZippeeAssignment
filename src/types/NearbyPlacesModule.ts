export interface Place {
  name: string;
  latitude: number;
  longitude: number;
  distance: number;
}

export interface NearbyPlacesResult {
  places: Place[];
}

export interface NearbyPlacesModule {
  getNearbyPlaces(): Promise<NearbyPlacesResult>;
  requestLocationPermission(): Promise<boolean>;
}

declare module 'react-native' {
  interface NativeModulesStatic {
    NearbyPlacesModule: NearbyPlacesModule;
  }
}