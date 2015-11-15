# Podcatcher - BBC Software Engineering & Internet Applications MSc - Mobile Applications Android
As part of the course, a deliverable is a final project that covers
certain topics learned over the period of the three day course. The final project is in the form
of a fully functioning Android application. This document proposes the details of the project,
how it aims to cover the requirements of the final project and justifications.
## Background
Podcasts have become an increasingly popular way to distribute digital audio over the
Internet. The term 'podcast' was coined as a cross between iPod and broadcast. Whether
this is radio shows, documentaries, interviews and lectures to name a few. These normally
use RSS (Really Simple Syndication) feeds as a structured standard to expose podcasts to
be consumed by software on computers, mp3 players, mobile phones and tablets. This
means that the creators of the content can have subscribers to the audio content they are
producing and push new items to user who subscribe to that feed. Because of the number of
ways to consume podcasts they are ever increasing in popularity including on Android
devices.

## Podcatcher Application
The aim of this project is to produce an android application that subscribes to Podcast RSS
feeds or what is sometimes know as a ‘Podcatcher’. Users of the application will be able to:

• Add Podcasts

• Subscribe permanently to Podcasts

• Check them for updates

• Play selected podcasts

Creating an application that does this covers the majority of requirements for the project.

## Requirements
The application should cover:

* UI design (Activity, Fragment, View components)

* Network Connections

* XML (or JSON) data parsing

* Database implementation

* Location Detection

* Gestures (including multi-touches)

* Animation and Sensors;

The target device:

* Google Nexus 7 tablet (2013, the second generation):

* Screen resolution: 1200x1920 (including system bars);

* Screen density: 323ppi (127ppcm);

* SDK version: 19 (Android 4.4.4 KITKAT);

* Camera: a 1.2-MP 720p front-facing and a 5.0-MP 1080p rear-facing camera;

### UI Design
This application will use the Android Holo theme to give the application and familiar feel to
users of the Android operating system. It will use Activities, List Views, Notifications and
Audio Player to listen to selected podcasts

### Database implementation
The details of the Podcast (metadata and RSS feed URL will be stored locally in an SQLite
database so that users can remember and delete their chosen podcasts

### Network Connections
As podcast information is delivered via RSS, these will have to be polled over the internet to
get the latest updates. The audio files, typically MP3 will be streamed to the audio player
over the internet.

### XML Parsing
RSS is an xml based format will be parsed and relevant information extracted and stored by
the application

### Data Parsing
An RSS formatted xml feed will be parsed and podcast items selectable and mp3 played

### Location Detection
Unfortunately there is not an obvious way to incorporate location detection in this application
as the nature of podcasts are that they are global and available to everyone with a means of
subscribing to them

### Gestures
Podcasts are selectable by pressing on them and long press to delete, individual podcast
episodes are selectable which will then load them into the Audio Player

### Animations and Sensors
If a user shakes their Android device in the initial.main view, it will launch the add podcast
screen for adding new podcasts
