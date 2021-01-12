# CS349 A1
Student: itvorogo
Marker: Constant


Total: 75 / 85 (88.24%)

Code:
(CO: won’t compile, CR: crashes, FR: UI freezes/unresponsive, NS: not submitted)


Notes:  12. Options are not displayed graphically 16. There was at least one situation when a control could not be used, but it was not disabled: Fill does not have line thickness


## FUNCTIONAL REQUIREMENTS (60)

1. [5/5] Saving data. File-Save can be used to save the model (the user should be promoted for a file-save location using a JDialog)

-5 Save feature not implemented
-2 The app doesn't prompt the user for a file name
-3 The file is not saved

2. [5/5] Loading data. File-Load can be used to load and restore the shapes that were saved using a JDialog. The view (e.g. window size, view-option) does not need to be restored, but all shapes should be saved and restored to the canvas, with the appropriate color, line thickness and so on.

-5 Load feature not implemented
-2 The app doesn't prompt the user for a file name
-3 The file is not loaded
-2 The file is loaded, but the drawing is not perfectly restored to the previous state

3. [2/2] File-New can be used to discard the current document and create a new blank document.

-2 New feature not implemented or doesn't work

4. [3/3] Selection tool. The user can select this tool in the toolbar then click on a shape to select it. There should be some visual indication which shape is selected. Pressing ESC on the keyboard will clear shape selection.

-3 Selection tool not implemented or doesn't work
-1 Selection is unreliable (sometimes clicking within a shape doesn't select it, or clicking outside of a shape selects something)
-1 There is no visual indication of selection
-1 Pressing ESC does not clear shape selection

5. [2/2] Erase tool. The user can select this tool in the toolbar then click on a shape to delete it.

-2 Erase tool not implemented or doesn't work
-1 Erase tool works only sometimes

6. [4/4] Line drawing tool. The user can select this tool in the toolbar and then draw lines in the canvas by holding and dragging the mouse, using the selected colour and line thickness.

-4 Line drawing tool not implemented or doesn't work
-2 Line drawing doesn't work as expected (the first point of the line should correspond to the position of mouse press, and the second point should correspond to the position of the mouse release)
-2 A preview of the line does not appear while the mouse button is being dragged

7. [4/4] Rectangle drawing tool. The user can select this tool in the toolbar and then draw rectangles in the canvas by holding and dragging the mouse, using the selected colour and line thickness.

-4 Rectangle drawing tool not implemented or doesn't work
-2 Rectangle drawing doesn't work as expected (the first corner of the rectangle should correspond to the position of mouse press, and the second corner should correspond to the position of the mouse release)
-2 A preview of the rectangle does not appear while the mouse button is being dragged

8. [4/4] Circle drawing tool. The user can select this tool in the toolbar and then draw circles or ellipses in the canvas by holding and dragging the mouse, using the selected colour and line thickness.

-4 Circle drawing tool not implemented or doesn't work
-2 Circle drawing doesn't work as expected (the first corner [or centre] of the circle/ellipse should correspond to the position of mouse press, and the second corner [or radius] should correspond to the position of the mouse release)
-2 A preview of the circle/ellipse does not appear while the mouse button is being dragged

9. [3/3] Fill tool: The user can select this tool in the toolbar and then click on a shape (rectangle or circle/ellipse) to fill it with the current colour.

-3 Fill tool not implemented or doesn't work
-2 Fill tool doesn't use the currently selected colour

10. [5/5] Colour palette. Displays at least 6 colors in a graphical view, which the user can select to set the property for drawing a new shape.

-5 Colour palette not implemented or doesn't work
-2 Colour pallete has less than 6 colours
-3 Colours are not displayed graphically
-2 There is no visual indicator of the selected colour
-3 New shapes are not drawn with the selected colour

11. [2/2] Colour picker. The user can click on the "Chooser" button to bring up a colour chooser dialog to select a colour.

-2 Colour picker not implemented or doesn't work
-1 There is no visual indication of the selected colour

12. [2/5] Line thickness palette. Displays at least 3 line widths, graphically, and allows the user to select line width for new shapes. [6 marks for supporting at least 3 lines; 4 marks for showing them graphically].

-5 Line thickness palette not implemented or doesn't work
-2 Line thickness palette has less than 3 thickness options
-3 Options are not displayed graphically
-2 There is no visual indicator of the selected thickness
-3 New shapes are not drawn with the selected thickness

13. [2/2] Selecting a shape will cause the color palette and line thickness palette to update their state to reflect the currently selected shape.

-1 The colour palette is not updated to relect the selected shape
-1 The line thickness palette is not updated to relect the selected shape

14. [10/10] Moving shapes. Users can move shapes around the screen by selecting, then dragging them. There should be an indication which shape is selected. [5 marks for moving, 5 marks for selection indicator]

-10 It's not possible to move shapes
-5 There is no indicator of which shape is selected and being moved
-5 Movement is not smooth (for example, the shape jumps around while being moved)
-5 There is no preview of the movement while the mouse is pressed and being dragged

15. [2/2] The interface should clearly indicate which tool, color and line thickness are selected at any time.

-2 The interface does not indicate which tool is selected at least in one situation

16. [0/2] The interface should enable/disable controls if appropriate.

-2 There was at least one situation when a control could not be used, but it was not disabled. E.g. Eraser tool does not have a colour, or line thcikness.


## LAYOUT (10)

1. [1/1] The starting appication window is no larger than 1600x1200.

-1 The application starts with a window that is larger than 1600x1200

2. [4/4] Swing widgets are used appropriately to implement the window layout.

-4 Innapropriate widgets were used to implement the window layout
-4 Implementation did not use only Swing widgets
-2 There was an issue with the layout (for example, some controls were difficult to use or incorrectly positioned)

3. [5/5] The layout includes menu bar, colour palette, line chooser and tool palette in the appropriate layout.

-2 toolbars not aligned properly

## TECHNICAL REQUIREMENTS AND MVC (5)

1. [5/5] Gradle.build exists, `gradle build` and `gradle run` works successfully.

-5 There is no build config file; or it didn't work
-4 There was a problem in the gradle.build, which the TA had to fix to run the program; details:
-3 The makefile works to compile the program, but not to run it

3. [0/0] If third-party graphics/images are used in the user interface, their origin and licence are included in the README file (this item can be negative).

-2 Third-party graphics/images were used, but there is no information about them in the README


## ADDITIONAL/BONUS FEATURES (10 + 10 optional bonus)

1. [5/10] Application incorporates one or more enhancements totalling 10%, as described in the requirements.

+5 Scale shapes: the ability to change the scale/size of any shape by selecting it, then grabbing a corner of the shape, and dragging to increase/decrease its size.
+5 Group shapes: select multiple shapes, then group them together so that they can be dragged/resized as a single entity.
+5 Customizable color palette: you can wholly or partially customize color buttons in the palette (e.g. right-click a button and choose a new color for that button from a color-chooser dialog).
+10 Dynamic use of widgets: if display is resized too small, replace a set of widgets with a space-optimized widget (e.g. replace stroke width with combo box, color palette with subset of buttons and so on). This should be a dynamic manipulation of at least 2 sets of widgets.
+10 System-level copy/paste: support pasting an image (any supported format) to an outside program (e.g. MS Paint, Photoshop, Pixelmator).
