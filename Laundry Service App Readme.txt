Laundry Service App
Overview
The Laundry Service App is an Android application designed to streamline the management of laundry services. It allows users to sign up, log in, track drivers in real-time, and manage their laundry orders. The app includes a real-time driver tracking feature, multi-language support, and Firebase integration for managing user data and laundry orders.

Features
User Registration & Login: Sign-up and login pages, with support for Single Sign-On (SSO) and biometric authentication.
Order Summary: Users can view a summary of their laundry orders.
Track Driver: Real-time driver tracking using a custom API, with projected arrival times, and options to call or message the driver.
Multi-language Support: Supports multiple South African languages such as English, Afrikaans, isiZulu, isiXhosa, Sesotho, and Setswana.
Image Attachment: Add and display images of laundry slips/items.
Firebase Integration: Stores user data and laundry slips using Firebase Realtime Database and Firebase Storage for images.
Settings Page: Allows users to update language preferences, profile, and more.
Technologies Used
Kotlin: Primary programming language used for development.
Android SDK: Used for Android app components.
Firebase: Integrated for user authentication, real-time database, and image storage.
Google Maps SDK: Implemented for real-time driver location tracking.
Multi-language Support: Using Android’s strings.xml to handle translations.
Installation
Prerequisites
Android Studio
Firebase account
Google Maps API Key
Android SDK version 21 or higher
Steps
Clone the repository:

bash
Copy code
git clone https://github.com/WavhudiTshibubudze/Coders.git
Open the project in Android Studio.

Configure Firebase:

Go to Firebase Console and create a new project.
Add your Android app to the Firebase project.
Download the google-services.json file and place it in the app/ directory.
Enable Firebase Authentication and Firebase Realtime Database.
Configure Google Maps:

Obtain a Google Maps API Key from Google Cloud Console.
Add the API key in the AndroidManifest.xml file under:
xml
Copy code
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />
Build and run the project in Android Studio.

Usage
Sign Up & Login: After launching the app, users can create an account or log in using their credentials.

Track Driver: Navigate to the "Track Driver" page, where users can view the driver's current location on a map and see the estimated time of arrival. They can also call or message the driver from this page.

Add Laundry Slip: Go to the "Add Item" page, enter the slip number, clothing type, color, material, and description, and attach an image of the laundry slip.

Multi-language Support: The app supports English, Afrikaans, isiZulu, isiXhosa, Sesotho, and Setswana. Users can change the language from the Settings page.

Code Overview
Main Components
MainActivity.kt: The main entry point of the application.
TrackDriverActivity.kt: Handles the real-time tracking of the driver's location.
AddSlipActivity.kt: Allows users to add a new laundry slip.
SettingsActivity.kt: Provides options to change the app's language and update user settings.
FirebaseHelper.kt: Contains Firebase database and storage handling functions.
GoogleMapsHelper.kt: Manages Google Maps interactions and driver location tracking.
Multi-language Support
To add or modify translations:

Go to res/values/strings.xml for the default language (English).
Additional translations are stored in res/values-af/, res/values-zu/, res/values-xh/, res/values-st/, and res/values-ts/ for Afrikaans, isiZulu, isiXhosa, Sesotho, and Setswana, respectively.
Firebase Integration
Authentication: Users authenticate using Firebase Authentication.
Database: All order details and user profiles are stored in Firebase Realtime Database.
Storage: Images of laundry items are stored in Firebase Storage.
Contribution
To contribute:

Fork the repository.
Create a new branch (git checkout -b feature-branch).
Make your changes and commit them (git commit -m "Added new feature").
Push to the branch (git push origin feature-branch).
Create a Pull Request.
License
This project is licensed under the MIT License. See the LICENSE file for details.

Credits
Developers: Your Name, Collaborator's Name
Design References: Android Material Design, Google Maps Platform
Special Thanks: Firebase team, Kotlin community, and Stack Overflow contributors.