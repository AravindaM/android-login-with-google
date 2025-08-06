# Google Sign-In Android App (No Firebase Required!)

This is a bare minimum Android app that implements Google Sign-In functionality without Firebase - just using Google's OAuth 2.0 directly.

## Features

- Single Google Sign-In button
- Comprehensive logging of all authentication steps
- Displays authorization token in logs
- Shows user information after successful login
- **No Firebase dependency required!**

## Setup Instructions

### 1. Create Google Cloud Console Project

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the "Google Sign-In API" (sometimes called "Google+ API" or "People API")

### 2. Create OAuth 2.0 Credentials

1. Go to "Credentials" in the left sidebar
2. Click "Create Credentials" > "OAuth 2.0 Client IDs"
3. Select "Web application" as application type
4. Note down the **Client ID** - this is your web client ID
5. Optionally, create an "Android" application type as well for additional security

### 3. Update Client ID in Your App

1. Open `app/src/main/res/values/strings.xml`
2. Replace `YOUR_WEB_CLIENT_ID_HERE` with your actual web client ID from step 2

### 4. Add SHA-1 Fingerprint (Recommended)

For production apps, add your app's SHA-1 fingerprint to the OAuth client:

```bash
# Get debug keystore SHA-1
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

Add this SHA-1 fingerprint to your OAuth 2.0 client in Google Cloud Console.

### 5. Build and Run

1. Sync the project in Android Studio
2. Build and run the app
3. Click the "Sign in with Google" button
4. Check the logcat for detailed authentication logs

## Why No Firebase?

Firebase is Google's mobile platform that includes authentication, but it's not required for Google Sign-In:

- **Firebase approach**: Uses Firebase Auth SDK + Google Sign-In
- **Direct approach** (this app): Uses Google Sign-In SDK directly with OAuth 2.0

Both approaches give you the same authorization token that you can use with any backend!

## Logging

The app logs all authentication steps with the tag `GoogleSignIn`. Look for:

- Configuration setup
- Button clicks  
- Sign-in process initiation
- Authentication results
- User account information
- **Authorization token (ID Token)**

## Code Structure

- `FirstFragment.java` - Contains the Google Sign-In implementation
- `fragment_first.xml` - Layout with the Google Sign-In button
- `strings.xml` - Contains the web client ID (needs to be updated)

## What Gets Logged

After successful sign-in, you'll see in logcat:
- Account ID
- User's email address
- User's display name
- User's given name and family name
- Profile photo URL
- **ID Token (authorization token)** ⭐

The ID Token is a JWT that contains user information and can be sent to any backend server for verification.
