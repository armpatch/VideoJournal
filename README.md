# VideoJournal

An Android app for creating a journal of videos.

## Overview

Video Journal is an app for anyone wanting to measure their progress over time in their field of interest. The app provides a minimalist interface to record and playback videos, and gives the option to provide a title and some notes for the recording.

## Activities

### - RecordingListActivity (Main Activity)
This is the first activity that the user is presented with when they open the app. The first time the app is started, they will be prompted to provide permissions to read/write to storage and to record video.

To create a their first entry, the user can click the floating action button, which will start the second activity: RecordingEditorActivity.

### - Recording Editor Activity (Activity 2):
Immediately after this activity is started, it starts an external activity using the phone's default recording device. If the user does not successfully record a video, they will be returned to the recording list activity without submitting a new entry. Otherwise, they will be prompted to enter the title and description for the recording. 

Before returning to the Recording Editor, a thumbnail is created and the path is saved to the recording database entry, in addition to the mp4 video filepath. The bitmap is applied to an ImageView as a still preview to the recording.

### - Recording Viewer Activity (Activity 3):
This activity is opened when a list item is clicked from the main activity. This screen shows a large videoView with the recording, as well as title and date textViews. In order to create a more seamless transition between the activities, shared elements were added between this and the RecordingListActivity. Originally, the three shared elements were the title, date, and videoView. However, videoViews proved problematic for transitions because they appear black while loading their video, and immediately before being started. This resulted in a disjointed transition between the two activitie. 

In order for the animation to be coherent, a placeholder thumbnail was placed on top of the video view and acted as the shared element in its place. An onClickListener was attached to the container holding the videoview and placeholder, rather than directly to the videoView. Clicking the container removed the placeholder and reveals the videoView below, which starts to play. Ideally, this transition would not be noticeable, however, the videoView still flashes black for an instant before starting.

## Components and Libraries

- RecyclerView with a GridLayoutManager for the List Activity
- SQLite Database was used to store recording objects, containing a date, title, description, and a path to the video and thumbnail files
- Used ObjectAnimators to create the play and pause indicators for the videoviews

## Authors

**Aaron Patch** - (https://github.com/armpatch)

## Acknowledgments
