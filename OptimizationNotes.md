# Optimization Notes

### Basics

With all software, we need to be concious of our use of memory/RAM. Loading mass content into memory or not handing content properly will cause performance issues. The Android operating system has particular low memory due to being a hand held device and the OS can shut down apps automatically if not handled properly.

Garbage Collection (GC) is a form of automatic memory management. The GC attempts to reclaim garbage/memory occupied by objects that are no longer in use by our application. The Android GC does most of this work for us.

For the GC to be able to do work effectively, it cannot be strong referrenced in code. If the GC fails to reclaim objects memory, a memory leak will occur. This would be caused by the GC being unable to release the referenced object. 

How the GC works is that in our application, as we assign more objects memory, more GC events occur. During a GC event, all threads are suspended. This can cause problems if GC events are occuring rapidly as, for example, the UI thread is suspended, the UI will be unresponsive.

From a development point of view, we want to make use of as little objects as possible, therefore causing less GC events which inturn will provide a better user performance.

### 

Memory Footprint is