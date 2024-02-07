# Mobile Computing
Niklas Raesalmi

## Homework 1

Following the Jetpack Compose Tutorial went relatively straightforwardly and without any bigger hiccups. I only encountered problems with the dark mode not working at the start. I first followed the tutorial line-by-line to learn the basics and after that I added my own twist to it by tweaking the values and adding other modifiers to the author, body and the structure of them. I then added multiple different possible choices for the profile photo, title and text and used randomization to pick them for each entry.

I added images by drag-and-dropping them into the Resource Manager's Drawable section and edited their size and shape using the foundation.layout, foundation.border and ui.draw.clip imports. I modified the size and spacing of the text, made the titles bolded and changed the title's colors. By using foundation.lazy.LazyColumn, I added multiple text entries and made the app scrollable. By using imports from runtime and foundation.clickable and by using a boolean variable and if statements to check its state, I made the entries interactable.

![Homework 1 Demo](homework-1/Screen_recording_20240122_020459.webm)
## Homework 2

The most important part for implementing navigation is setting up a NavHost that keeps track of the page that you're currently on and where each button leads to.

I got the navigation working by first setting both pages into the NavHost and then for the buttons I used navController.navigate() and put the page that the button should lead to into the parenthesis. To prevent circular navigation, instead of the back button on the message page leading back to the main screen I instead used navController.popBackStack() to go back one page.

![Homework 2 Demo](homework-2/Screen_recording_20240128_195709.webm)
