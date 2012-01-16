#Satellite Menu

'Path' has a very attractive menu sitting on the left bottom corner of the screen. Satellite Menu is the open version of this menu. 

For the ones who didnt see path, the menu consists of a main button on the left bottom of the screen. When the user clicks on this button, a number of buttons are popped out of this button. I have resembled this structure to a planet and satellites and thats why I gave the name of this project. 

The menu uses built-in animations of Android platform. 

[![](http://farm8.staticflickr.com/7014/6706236829_ca1db99eec.jpg)]

[![](http://farm8.staticflickr.com/7145/6706236777_cc5309e57c.jpg)]


(http://farm8.staticflickr.com/7014/6706236829_ca1db99eec.jpg)


##Usage

Add the component definition to your view xml as in the following example:

  <?xml version="1.0" encoding="utf-8"?>
  <FrameLayout 
      xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:sat="http://schemas.android.com/apk/res/android.view.ext"
      android:layout_width="fill_parent"
      android:layout_height="fill_parent"
      android:orientation="vertical" >

      <android.view.ext.SatelliteMenu
          android:id="@+id/menu"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_gravity="bottom|left" 
          android:layout_margin="8dp"
          sat:satelliteDistance="170dp"
          sat:mainImage="@drawable/ic_launcher"
          sat:totalSpacingDegree="90"
          sat:closeOnClick="true"
          sat:expandDuration="500"/>
    
  </FrameLayout>






