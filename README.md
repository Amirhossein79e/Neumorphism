[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![](https://jitpack.io/v/Amirhossein79e/Neumorphism.svg)](https://jitpack.io/#Amirhossein79e/Neumorphism)
# Neumorphism #
### Implementation of Neumorphism design on Android view components. ###
####  This library let you use views with neumorphism design so simple like below <br/> and supports Button,TextView,CardView. ####

<br/>

<p align="center">
  <img align="left" width="484" height="272" src="https://github.com/Amirhossein79e/Neumorphism/blob/4102ea9e55b5dcc1498576c45ee0ec425c00428a/Screenshot%202022-02-26%20054319%20(1).png" />
  <img align="right" width="484" height="272" src="https://github.com/Amirhossein79e/Neumorphism/blob/4102ea9e55b5dcc1498576c45ee0ec425c00428a/Screenshot%202022-02-26%20055008.png" />
</p>

_

### How to add to project: <br/> ###

__Step 1.__  Add it in your root build.gradle(project level) at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
__Step 2.__ Add the dependency in build.gradle(module level)

	dependencies {
	        implementation 'com.github.Amirhossein79e:Neumorphism:1.4'
	}

After you added library to your project follow below steps

### How to add to project: <br/> ###

Usage of the views is so simple because there are extended from AndroidX AppCompatViews and just 5 attributes you need to specify for using the views.

This library have 3 views currently:

1.`NeuCardView` same as CardView<br/>
2.`NeuTextView` same as AppCompatTextView<br/>
3.`NeuButton` same as AppCompatButton<br/>

The attributes you should specify them, if you don't use them then the view use default value defined in library :

1. `app:neu_background_color="solid color"` : <br/>
This attr set the solid background color for the view. currently not support gradient.

2. `app:neu_light_shadow_color="solid color"` :<br/>
This attr set the color for the light shadow color in the direction of the light source. in Neumorphism design you need dark shadow and light shadow.

3. `app:neu_dark_shadow_color="solid color"` :<br/>
This attr set a color for the dark shadow color of view.

4. `app:neu_elevation="dimension"` :<br/>
This attr set elevation for the view and acts like the CardView `card_elevation` attr. whatever it be larger shadows around the view will be larger.

5. `app:neu_radius="dimension"` :<br/>
This attr set corner radius for the view and acts like the CardView `card_corner_radius` attr. whatever it be larger bounds of the view will be more rounded.

in below we have a example usage of the `NeuCardView` in ConstraintLayout .

	<androidx.constraintlayout.widget.ConstraintLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context=".MainActivity"
	    android:background="@color/md_blue_grey_800">


	  <com.amirhosseinemadi.neumorphism.widget.NeuCardView
	      android:layout_width="164dp"
	      android:layout_height="164dp"
	      app:layout_constraintTop_toTopOf="parent"
	      app:layout_constraintStart_toStartOf="parent"
	      app:layout_constraintEnd_toEndOf="parent"
	      app:layout_constraintBottom_toBottomOf="parent"
	      app:neu_background_color="@color/md_blue_grey_800"
	      app:neu_light_shadow_color="@color/md_blue_grey_700"
	      app:neu_dark_shadow_color="@color/md_blue_grey_900"
	      app:neu_elevation="6dp"
	      app:neu_radius="12dp">
	  </com.amirhosseinemadi.neumorphism.widget.NeuCardView>


	</androidx.constraintlayout.widget.ConstraintLayout>
	
__I Hope this library help you__
