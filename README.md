# VideoJournal

## Overview

Video Journal is an app for anyone wanting to measure their progress over time in their field of interest. The app provides a simple interface to record videos and save them with a custom title and description.

## Activities

### - Recording List Activity (Main Activity)
This is the first activity that the user is presented with when they open the app. After installation, they will be prompted to provide permissions to read/write to storage and to record video.

Clicking the floating action button will start the Recording Editor Activity and prompt the user to record a video.

### - Recording Editor Activity (Activity 2):
Before the second activity’s layout is drawn, the user is directed to record a video using their phone’s default camera app. After returning from this activity, one of two things will happen. If the user does not record a video, they will be returned to the recording list activity. Otherwise, they will be prompted to enter the title and a description for the new recording. 

In the editor, a bitmap is created from the first frame of the video to be used as a thumbnail. The image file path is saved in the SQLite table row associated with this recording, along with the video path, title, date, description and unique identifier.

### - Recording Viewer Activity (Activity 3):
By clicking one of the list items in the main activity, the Recording Viewer Activity is started, which shows a video View, title, and date. To create a smooth transition between activities, shared elements were implemented to animate the thumbnail, title, and date. When the video container is clicked, the thumbnail placeholder is set to invisible before playing the video. From this screen, there is also the option to delete the recording, by clicking the menu item in the top right-hand corner of the screen.

## Components and Libraries

- RecyclerView with a Grid Layout Manager for the List Activity
- SQLite Database was used to store recording entry information and paths to files
- FileProvider was used to store Videos and Images in a private local directory 
- Used ObjectAnimators and vector drawables to create play and pause visual indicators
