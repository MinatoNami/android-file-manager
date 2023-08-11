# Android File Manager

Android File Manager (Kotlin) is an Android app that serves as a powerful and user-friendly file manager. It allows users to manage files and folders on their device efficiently. The app should support basic file operations such as browsing, copying, moving, deleting, and renaming files and folders. Additionally, the app should include storage analysis features to help users manage their device's storage effectively.

# Functional features
1. Browse Files and Folders (grid/list views + hierarchical order) (Provided)
2. File Operations (Copy, Move, Delete, Rename) + Destination Picker (Provided)
3. Search for files or folders (based on keywords), dynamic update to search results (Provided)
4. File Details (File size, location and last modified date) (Provided)

# Non-functional features
1. Intuitive and easy for the user to navigate.
2. Follow Material Design guidelines for consistency
3. App should be responsive and can handle large file operations efficiently
4. Storage analysis should be performed quickly (even for devices with substantial storage size) (Enhancement)
5. Ensure that the app requests for the necessary permissions following best practices (Android 6.0) to access the user's files, but protect sensitive files from unauthorised access. (AndroidManifest File)
6. App supports Android 6.0 (Marshmallow) and above (On project creation)

# Enhancements
1. File Sharing (Provided)
2. Storage Analysis (Bring up a pie chart and brief breakdown of file types and space used) (Provided)
3. Cloud Storage Integration (External package)
4. File Compression and Extraction (Provided)
5. Dark Mode (styles.xml) (Provided)

## Proposed future improvement plan:
1. Edit package to add in a side navigational bar
2. Look at cloud storage integration

# How will this project work well with eye-tracking
## Features
1. With eye tracking, users can browse through files just by simply looking at them. The large icons and labels reduces distractions for the user, as details are only available when the user decides to look at the "i" button for more information or perform other eye gestures (like blink twice to show more info).
2. List/Grid views also allows the user to choose between 2 different layouts, according to their own preferences. The spacing between icons allows the user to easily navigate and scroll between pages using their eye movements. Possible implementation of the scrolling function, can be done b trackign the eye gestures at boundaries (Top or Bottom of the page).
3. By staring at a folder, options for file operations can be made available, which allows the user to copy/delete/share files/folders.
4. Storage Analysis allows the user to see a brief breakdown of their storage device. Eye tracking motion can be used to display a more detailed breakdown of the particular file type (like when the user stares at the category for a period of time)
5. If it's possible to implement a Text-To-Speech feature, we can use this to allow the user to search for files easily. This can be triggered when the user performs a certain eye gesture (like blinking three times) and the search bar opens up. Saying anything besides certain singular keywords like "Enter" or "Cancel", will allow the user to search for the file by name or filetype.

## Cost wise
1. Being compatible with Android 6 and above, allows the use of cheaper hardware, which is a factor for mass manufacturing.
2. It also means that there is better stablity and lesser hardware-related issues during development.

# Personal thoughts on project
It has been an intense and exciting 3 days working on this project, as I had to learn Android developement from scratch. Although my background in Java has helped with the transition to Kotlin, knowing the language is one thing and building an app using it is another. Given more time, I would like to add in more features that can improve integration with eye-tracking, as eye-tracking allow for more optoions in designing the application.
