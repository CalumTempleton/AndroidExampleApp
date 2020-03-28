# Optimization Notes

## Basics

With all software, we need to be concious of our use of memory/RAM. Loading mass content into memory or not handing content properly will cause performance issues. The Android operating system has particular low memory due to being a hand held device and the OS can shut down apps automatically if not handled properly.

Garbage Collection (GC) is a form of automatic memory management. The GC attempts to reclaim garbage/memory occupied by objects that are no longer in use by our application. The Android GC does most of this work for us.

For the GC to be able to do work effectively, it cannot be strong referrenced in code. If the GC fails to reclaim objects memory, a memory leak will occur. This would be caused by the GC being unable to release the referenced object. 

How the GC works is that in our application, as we assign more objects memory, more GC events occur. During a GC event, all threads are suspended. This can cause problems if GC events are occuring rapidly as, for example, the UI thread is suspended, the UI will be unresponsive.

From a development point of view, we want to make use of as little objects as possible, therefore causing less GC events which inturn will provide a better user performance.

## Profiling

Memory Footprint refers to the amount of main memory that a program uses/references while running. 

To make use of the Profiler in Android Studio, select the Profiler tab at the bottom of the screen. This will likely be beside build. This shows four metrics, CPU, Memory, Network and Enegry. Each of these can be clicked on to provide a more indepth overview. To make use of the Profiler, you may need to select an application which can be done by selecting the + icon, then selecting the package name. 

### Memory

When click on the memory, you get a better overview of what is going on. At the top, the package name will be listed along with a timeline which shows events such as changing activity, pressing buttons... With the graph itself, you can also see actions such as Garbage Collection.

Below, the graph it self will show you a break down of how much memory is being used. To see what is using the memory, you can select a portion of the graph by highlighting. You can then sort by package, then see what classes are using what. You can also anlyse the heap. These can be further clicked on to often identify what exact variables are using what.

One of the most useful tools about the memory profiler is that you can force garbage collection. This can be done by pressing on the garbage can above the package name. When pressing this and seeing memory dropping drastically, you know you have memory problems which need to be addressed.

Furthermore, if you see the memory graph having lots of spikes, this means there is frequent garbage collection events occuring. This means that memory is being assigned in a short period of time. This is called a Memory Churn. In Logcat, this can be identified by messages including the text "GC freed".

## Data Types

OMO is a common term used when dealing with Android Memory Management and stands for Object Memory Overhead.

In Java (and possibly kotlin), when creating a mass number of objects, such as a thousand numbers, it is very important to consider the different data types as each will take up a different number of bytes. Primitive types should be looked at. Strings are immutable in Java meaning once they are created they are closed for change. This means that the wrong usage of strings would lead to a lot of GC events.

The StringBuilder class is good to use when dealing with strings as it allows for faster string modifications and is more memory efficient. Mkaing use of abstracted constants is also more memory efficient. In Java, making values static and final and in Kotlin, making them const val companion objects. This will help memory footprint.

Loading images can be expensive and requires a lot of memory. The amount will depend on the size of image and the format of the pixels (an example would be ARGB8888 meaning each pixel takes 4 bytes). An image 1280*850 with this pixel format would take up 4,352,000 bytes or 4.352 MB. If images or image data is not handled correctly, it can crash your app by throwing an OutOfMemoryError. There is a limit of how much memory is allowed per process which can be found using the ActivityManager.getMemoryClass().