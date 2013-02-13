#Satellite Menu

'Path' has a very attractive menu sitting on the left bottom corner of the screen. Satellite Menu is the open version of this menu. 

For the ones who didnt see path, the menu consists of a main button on the left bottom of the screen. When the user clicks on this button, a number of buttons are popped out of this button. I have resembled this structure to a planet and satellites and thats why I gave the name of this project. 

The menu uses built-in animations of Android platform. 

Works in API Level 7 (2.1) and above.

Licenced with LGPL. 

<img src="http://i.imgur.com/0Igkktd.png" height="250px" title="Android Satellite Menu Expanded"/>

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

The attributes are:

* `satelliteDistance` The distance of items from the center button
* `totalSpacingDegree` The degree between the first and the last item.
* `closeOnClick` Defines if the menu shall collapse after clicking on a menu item.
* `expandDuration` The duration of expand and collapse operations in milliseconds.

After defining the view in XML, some menu items can be added from code:


    SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
    List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
    items.add(new SatelliteMenuItem(4, R.drawable.ic_1));
    items.add(new SatelliteMenuItem(4, R.drawable.ic_3));
    items.add(new SatelliteMenuItem(4, R.drawable.ic_4));
    items.add(new SatelliteMenuItem(3, R.drawable.ic_5));
    items.add(new SatelliteMenuItem(2, R.drawable.ic_6));
    items.add(new SatelliteMenuItem(1, R.drawable.ic_2));

Each menu item takes the ID of the item and the drawable resource for the item. 

In order to listen clicks on items:

    menu.setOnItemClickedListener(new SateliteClickedListener() {
      public void eventOccured(int id) {
        Log.i("sat", "Clicked on " + id);
      }
    });

The click event gives the ID of the item which was defined when adding it. 
