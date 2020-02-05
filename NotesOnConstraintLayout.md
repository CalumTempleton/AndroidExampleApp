# Notes on Constraint Layouts

### Basics

Constraint layouts constrain layout items, also known as views. Without these constraints applied, at run time, the views will move position, likely to the top left corner at 0,0. A constraint is a relationship between two views that controls how the views will be positioned.

Note that in design view, views will not move position as they have temporary values applied. Views can be constrained by clicking on the view item handles and dragging them to the sides of the layout.

The design view and blueprint view sit side by side. The design view shows you what your view will look like on a device at runtime showing all the styling. The blueprint view shows the text-based view of your layout which can be easier to work with when the layout gets more complex. 

### Buttons within Design View

All of the follow features have buttons at the top of the design view window.

The Autoconnect feature can be toggled on or off using the magnet icon. This can be useful when dragging in a view where it will automatically add constraints. These may not be right but will provide a good starting point. It is good to use this tool both on and off.

Clear Constraints clears all constraints in the layout, not just the one selected. 

Infer Constraints tries to add constraints for the whole view, but like Autoconnect, it has some issues. Its best used on very simple views.

When a view is selected in the design view, you can use pack and expand options beside the guidelines button above the view. However, these are not that useful as do not add constraints and place views using absolute values.

### Features

A Center Constraint is something special where the top and bottom of a view are connected to the same handle on another view. An example of this would be a profile picture overlapping half on and off a background.

A Baseline Constraint is where the text in two views are aligned. An example of this would be where a button and a textview are side by side. This constraint can only be used on views that contain text. The XML for the example above from the textview would be - app:layout_constraintBaseline_toBaselineOf="@+id/button".

A Bais allows you to move a widget with a bais to one constraint. An example of this would be using constraints to center a TextView but have it 100dp to the right to allow space for an image. An XML example of this would be - app:layout_constrantHorizontal_bias="0.2". Note this is a percentage of the screen. Percentages should be used over exact values due to fragmentation on different screen sizes. Using the Attributes Window, under layout, there is a scroll bar allowing you to easily add bias. Note that to add bias, you need constraints on that axis.

A Chain is a group of views that are linked to each other with bi-directional position constraints. These can be horizontal or vertical or a combination of both to make a grid layout. There are four common chain modes which are:
- Spread where views are distributed evenly within the available space: spaceN - button - spaceN - button - spaceN - button - spaceN
- Packed where views are packed together: spaceN - button - button - button - spaceN
- Spead Inside where the left and right views are against the layout with the views inside are distributed evenly within the available space: button - spaceN - button - spaceN - button
- Weighted where it uses the logic of Spead/Spread Inside but adds weights to the views. For example: buttonX - buttonY - buttonX. 

To add a Chain, select multiple views such as three buttons (using shift), right-click and select "Chains". To cycle through the different chain modes, right-click on a view (after a chain has been created) and select "Cycle Chain Mode". Note that within the XML, the chain attribute is usually attached to the left or center view.

If you have a horizontal chain with say three buttons, you still need to apply vertical constraints. If you add a vertical constraint to one button, it will not do it to the whole chain. The best practice is to apply a vertical constraint to center or left view, then add a vertical constraint to the other views against that center or left view. This will mean all views are connected.

With Weighted Chains, they have an additional XML property called app:layout_constraintHorizontal_weight="2". Weighted is not its own mode but uses the logic of Spead/Spread Inside but adds weights to the views.

A Guideline is a visual guide that will not be seen at runtime and is only visible in desgin view. They are mainly used to align views, either horizontally or vertically, and can be used to simulate margins. Guidelines have there own set of attributes. To add a guideline, have a view with a vertical or horizontal constraint, then right-click, select "Helpers" and choose a guideline. You can drag the guideline across the layout, switching the side the guideline is set from by pressing the small circle at the start which cycles through the different types.

With Guidelines, you can either use DP or percentage of the layout size. With the percentages, you can design for multiple different screen sizes much more easily and its hard not to make use of them. This is done by constraining views to guidelines using a percentage and not the layouts themselves. Guideline values can also go into a "dimens" file so there is one easily accessible place to set values. The best practice with guidelines is to have a start and end and bottom and top pairs to keep consistent padding around the layout.

A Group is a helper element that is used to group views. This is used to set visibility of all elements. This is not to be confused with an Android ViewGroup. A group reference views by id. All attributes set on the Group will apply to all the children. Groups are mainly used for toggling between content such as pass/fail messages or grouping content together. 

Barriers are invisible lines that are used by views to be constrained against. Barriers can, however, move based on views changing size and can be either horizontal or vertical. A basic example would be if you have a barrier on the right side of a button (not constrained to) and you have a textview above (these views are then both on the left of the barrier). Then, you have another view constrained to that barrier but on the right side. If you start to enter text into the textview and it fills up, it will push the barrier out and anything constrained to it. They are defined in XML as app:barrierDirection="right" and app:constaint_referenced_ids="button, edittextbox"...

To add a Barrier, right-click Helpers then select Barriers. Note that they will not be visible like guidelines within the design view but will be listed under the component tree. The reason barriers are not visible is that they need to be assigned views. To do this, simply select views in the component tree and drag them into the barrier. Make sure the direction is configured correctly. Barriers are useful for when using views that may have variable sizes such as textviews, images, and buttons. They can be good for handling translation or error codes.

Ratios show the relative size of two or more values. They can be used on many different kinds of views and have very little limits. However, they are mainly used on image views as images have their own ratios. They are defined in XML as app:layout_constraintDimensionRatio="16.9". To use the attributes tab to set a ratio, either the layout_width or layout_height must be set to match_constraint. Then, in the layout section, within the diagram, a small triangle will appear in the top left corner. Click on this to reveal the ratio box. The other layout value (which is not set to match_constraint), set this to a value of DP such as 200 to see the content on screen.

Looking at the XML snippet in the paragraph above, when implemented, sometimes there will be a "w" or "h" within the value such as "w,16:9". This means the width is calculated based on the DP height value. You need to watch when rotating with this view as this can have issues.

Circular positioning is an advance feature where views are placed based on a radius and an angle. An example of this may be a compass with North, East.. marking on it. One of the most powerful things you can do with this is to do an expandable button such as a share button. When this is pressed, three additional buttons to different social media outlets come out at different angles such as 0, 45 and 90. Currently, this is only available by the text editor and not in the design the view.

Circular positioning is defined in XML as app:layout_constraintCircle="@+id/centre_view", app:layout_constraintCircleAngle="30dp", app:layout_constraintCircleRadius="90dp". Note that as this is not greatly supported in the design view, warnings may appearing relating to the view not being constrained properly. This is likely fine but just make sure it runs on a few devices first.

### Tips

With textviews, setting the layout width to match constraints with borders is a good idea. This will help handle long text entries.

When placing views within the layout, make full use of the attributes window at the right side of the design view window. Make sure that good use of margins is used. Additionally, for textviews, make use of unique attributes such as Text Appearance.

With Constraint Layouts and Android in general, its good to have a 16dp margin all around on mobiles. This will likely be higher such as 64dp on a tablet.

When designing complex views is to change the version of Android you want to visualise your view on. There can be differences between versions.

A small tip for needing to wrap text boxes when wanting to use wrap_content instead of match_constraint, if you want to use wrap_content but it cuts off the screen, add the XML property app:layout_constraintWidth="true". Adding in a horizontal bias to the left will also be useful for both short and long text.

Constraint views have an optimizer that runs in the background automatically to try and optimize the view. The more views, the more complex things get. Constraint's view is based on the Cassowary algorithm which calculates where views are in relation to each other. Wrap_content and fixed dimensions are much cheaper than match_constraint/0dp, though this is rarely not always the case. This is because no measuring is needed.

There are different levels of optimizations which can be specified through the XML as app:layout_optimizationLevel="standard|direct|barrier|chains|dimensions". This goes at the top level of the constraint layout. You should not need to do this however but good to know if experiencing performance issues. Note that optimization are in a list, so you can add and remove types as required. You can also set the value to "none".