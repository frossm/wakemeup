<p align="center"> <img width="80%" src ="https://github.com/frossm/wakemeup/raw/master/graphics/ScreenShot.jpg"> </p> 

# INTRODUCTION

<img align="right" width="200" src="https://github.com/frossm/wakemeup/raw/master/graphics/PostIt512.jpg">WakeMeUp is a tiny utility to wake up a Wake-On-LAN device from it's standby mode.  It creates the "magic packet" and will broadcast it to the device.

This is done be creating a favorite and providing it the MAC Address and the Broadcast IP.  This, along with the name, is saved in the JAVA preferences system so you should only have to enter it once.

It does not have the ability to determine if the wakeup call was successful but I'm going to see if this is something I can add in the future.

Currently, it also doesn't have the ability to manage Wake-On-LAN devices that are setup with a password.

## Command Line Options
|Option|Description|
|--|--|
|-D|Debug mode.  Mostly used by the developer to show extra debugging information|
|-d NAME|Delete the favorite with the provided name|
|-c|Clear all saved favorites|
|-l|List all saved favorites stored in the system|
|-v|Show the version number as well as the latest version released on GitHub|
|-z|Remove colors from the output|
|-? or -h| Show Usage information.  Either `-h` or `-?` wil work|

## Favorites
WakeMeUp's arguments are favorite devices that you create.  Each of these will include the Name, the MAC Address, and the Broadcast IP.  When you execute WakeMeUp, you'll provide one or more favorite names.

The first time you run it with a name, it will check to see if that name entry exists.  If not, it will prompt you for the above mentioned details and then add it to the favorites list.  From then on, you won't be asked for the details and it will just execute.

To see a list of these favorites, use the `-l` switch, and you can `c` clear the entire list or just `d` a single entry.

## SNAP

[![wakemeup](https://snapcraft.io/wakemeup/badge.svg)](https://snapcraft.io/wakemeup)

I would encourage anyone with a supported Linux platform to use snap.  See [Snapcraft Homepage](https://snapcraft.io) for more information. You can download, install, and keep the application up to date automatically by installing the snap via:

`sudo snap install wakemeup`  (Assuming snap is installed)

This will install the application into a sandbox where it is separate from other applications.

[![Get it from the Snap Store](https://snapcraft.io/static/images/badges/en/snap-store-black.svg)](https://snapcraft.io/wakemeup)

## Feedback
If you have ideas or issues, please let me know.

wakemeup at fross dot org

## License
[The MIT License](https://opensource.org/licenses/MIT)

Copyright 2022 by Michael Fross

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
