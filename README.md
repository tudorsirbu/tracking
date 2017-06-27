# Tracking app

The app should run out of the box. It has been developed using the latest version of Android 3.0. 

At the following link a video demonstration of the realtime tracking while the app is in the foreground is available: 

 https://youtu.be/y0dmm6cN78g 

the next video shows the app while tracking the user when the app is not in the background, along with restoring the journey path when the app is brought back to the foreground:

https://youtu.be/ccd3HnIf_Gc

**Tasks marked in bold have been completed. A short description has also been provided for the tasks which could not be completed due to time restrictions.**

Primary Goals:

-	**Build an app that can display the user’s current location on a map**
-	**Update the map in real time as the user moves.**
-	**In response to a “tracking on/off” UI switch, record or do not record the user’s movements.**
-	**Display a path over the map as the user moves.**
-	**If a journey is defined as a set of recorded locations between a tracking on and a tracking off switch, retain the user’s journeys.**
-	**Allow the user to see all their journeys in a list.**
-	**Allow the user to see the start and end times of their journeys when they select them from the list.**
-	**The app should record the user’s location in the background if the user selects “tracking on” and backgrounds the app.**
-	**The app should not use the battery if the user selects “tracking off” and backgrounds the app.**
-	**If the app is resumed from the background during tracking, it should correctly display a path representing the journey that is currently being recorded.**

Bonus Goals:

-	**Allow the user to see each journey’s path plotted on a map, when selected from the list.**
-	Show any other interesting data you can think of relating to a journey, when selected from the list.
  - e.g. the speed returned by the location API or activity type (car, walking, bike) using the activity detection API
-	Secure the data that’s stored on the device.

Optional
-	**Retain the user’s data if the app is deleted and re-installed.**
-	Detect the user’s motion, and automatically determine when to turn tracking “on” or “off”, in a way that conserves battery power.
  ..- This can be achieved by acting based on detected user activity. If the user starts moving then the location tracking and be switched on and immediately off after they've stopped moving. 
  ..- Another way to trigger location tracking is when the user has connected their device to a charger or when they are connected to a bluetooth device that could be their car. (or a combination of both)
  ..- Geofences could also be setup to determine when the user has left a specific range. This could be useful if the user is moving within a larger building which might not have great GPS reception.

