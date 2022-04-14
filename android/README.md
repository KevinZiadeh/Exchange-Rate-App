# Android
> Android platform using Koltin

## Requirements
- Backend server set up and running. Please refer to corresponding [documentation](../backend/README.md)
- Backend connected to the database successfully
- Android Studio
- Android phone or Android emulator with API level 31 at least

## Run

```bash
git clone https://github.com/KevinZiadeh/Exchange-Rate-App.git
```
- Open the `Exchange-Rate-App/android` folder using Android Studio. 
- Wait for Grade to finish building. This may take a few minutes.
- Run `MainActivity`.
	- if you do not have a physical Android device, install the device emulator:
		1. `Tools` -> `SDK Manager` -> `SDK Tools` -> Select `Android Emulator
		2.  `Tools` -> `Device Manager` -> `Create device` -> Select whichever hardware setup you want -> `Next` -> Select the newest software which by the time of writng is `API 32` 

## Overview
### Structure
<p align="center">
  <img src="../res/android_structure.png?raw=true"/>
</p>

### API
The API package contains all the needed information and code to be able to communicate with the backend.
If you are not using an emulator or the backend is being run on another device, you need to edit the value of `API_URL` in `/api/retrofit.kt`  to match the URL and PORT of the backend server.
#### Model
The `model` package inside `api` package contains all the needed information about all the models that are used when interfacing with the backend.

### UI
The UI package contains all the information used to display our application and interface with it, be it logging in, conditional renderign depending on if user is logged in or not, fields and inputs used by the user, etc...

The UI is divided into a main activity `MainActivity`, and a number of fragments which can be accessed by using the side menu.
 
 
