# Android File Manager

Android File Manager (Kotlin) is an Android app that serves as a powerful and user-friendly file manager. It allows users to manage files and folders on their device efficiently. The app should support basic file operations such as browsing, copying, moving, deleting, and renaming files and folders. Additionally, the app should include storage analysis features to help users manage their device's storage effectively.

# Functional features
1. Browse Files and Folders (grid/list views + hierarchical order) (Need 2 different layouts for this case)
2. File Operations (Copy, Move, Delete, Rename) + Destination Picker
3. Search for files or folders (based on keywords), dynamic update to search results
4. File Details (File size, location and last modified date)

# Non-functional features
1. Intuitive and easy for the user to navigate.
2. Follow Material Design guidelines for consistency
3. App should be responsive and can handle large file operations efficiently
4. Storage analysis should be performed quickly (even for devices with substantial storage size) (Enhancement)
5. Ensure that the app requests for the necessary permissions following best practices (Android 6.0) to access the user's files, but protect sensitive files from unauthorised access. (AndroidManifest File)
6. App supports Android 6.0 (Marshmallow) and above (On project creation)

# Enhancements
1. File Sharing (Share button?)
2. Storage Analysis (Bring up a pie chart and brief breakdown of file types and space used)
3. Cloud Storage Integration (External package)
4. File Compression and Extraction
5. Dark Mode (styles.xml)

## Proposed tentative plan:
1. SideNav on the left for toggling between Internal, External and Cloud storage, using multiple tabs.
2. Header containing 3 buttons; SideNav on the left, Search/Folder Options on the right
4. Views for Internal/External/Cloud includes 1 TableLayout (For grid) and 1 LinearLayout (For list).
5. Each row or grid should have an options button or hold-to-trigger to bring up options for file operations.

### Search Button
Clicking search should bring up a text window at the bottom of the page, resting directly on the keyboard.
