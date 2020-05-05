Drone Flight Simulator in Google Maps:
==========================================================

This application simulates a drone journey from a starting point to a destination. The application shows how a drone might fly to a destination while avoiding no-fly zones. The simulation is made using Maps Android API.

Prerequisites
--------------

- Android SDK v24
- Latest Android Build Tools
- Android Support Repository

Getting started
---------------

This project uses the Gradle build system.

1. Start by cloning this repository.
2. In Android Studio, create a new project and choose the "Import Project" option.
3. Select the directory that you downloaded with this repository.
4. If prompted for a gradle configuration, accept the default settings.
  Alternatively use the "gradlew build" command to build the project directly.
5. Add your API key to your app's `gradle.properties` file.
  (For information on getting an API key, see the
  [documentation](https://developers.google.com/maps/documentation/android-api/signup).)

Resources
--------------
[Polylines and Polygons to Represent Routes and Areas](https://developers.google.com/maps/documentation/android-api/polygon-tutorial).
[Camera and View to Control the Animation](https://developers.google.com/maps/documentation/android-sdk/views).
