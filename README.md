# Photo-Editor-App

## Setup
### SDKs
Check that the following SDKs are installed and up-to-date.


Tools->SDK Manager:
![SDK list](https://i.imgur.com/alErK8q.png)


### OpenCv Module
OpenCV is a required module dependency which you must install yourself. 
Download the latest android library version from the OpenCV website and extract the zip.
In Android Studio, go to File->New->Import Module and select the
extracted directory/src (which contains the `build.gradle` file). Then rename the module to 
`opencv`. Android Studio should build the project successfully.
