# MaterialProgressBar
a material style ProgressBar,extends View.

#ART
![demo](https://github.com/Rowandjj/MaterialProgressBar/blob/master/art/material_progressbar_gif.gif)

#FEATURES
1. inderteminate and determinate mode supported.
2. many attributes supported.

#USAGE

just like ProgreeBar in android SDK

```
    xmlns:app="http://schemas.android.com/apk/res-auto"
    
<com.taobao.library.MaterialProgressBar
            app:bar_color="#ff0000"
            app:rim_color="#33ff0000"
            app:bar_rimshown="true"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
```
tips:

1. you can use layout_width/layout_height to control progressbar's width.
2. only in determinate mode that you can call setProgress().change mode use setMode() function.

#ATTRIBUTES

```
 <declare-styleable name="MaterialProgressBar">
        <attr name="bar_color" format="color"/>
        <attr name="bar_mode" format="enum">
            <enum name="INDETERMINATE" value="0"/>
            <enum name="DETERMINATE" value="1"/>
        </attr>
        <attr name="bar_width" format="dimension"/>
        <attr name="bar_progress" format="float"/>
        <attr name="rim_width" format="dimension"/>
        <attr name="rim_color" format="color"/>
        <attr name="bar_rimshown" format="boolean"/>
 </declare-styleable>
```

#more samples
```
 <com.taobao.library.MaterialProgressBar
    android:layout_marginLeft="10dp"
    android:layout_width="60dp"
    android:layout_height="70dp" />

<com.taobao.library.MaterialProgressBar
    android:layout_width="70dp"
    android:layout_height="70dp"
    app:bar_rimshown="true"
    app:bar_width="5dp"
    app:rim_width="6dp"/>
    
<com.taobao.library.MaterialProgressBar
   android:id="@+id/pb"
   app:bar_mode="DETERMINATE"
   app:bar_progress="0.6"
   android:layout_centerHorizontal="true"
   android:layout_below="@id/container"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content" />
```
#ISSURES
if you found issue,just pull request to me!




