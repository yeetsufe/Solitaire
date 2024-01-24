Hints:

Functionalities Added:
- I have created a new Comparator interface for sorting leaderboard scores by descending value
- Instead of the player having to go to the options menu every time to change the difficulty level, I have created a submenu in the JMenu
that allows the player to directly choose the amount of cards they want.

Functionalities Missing:
- The original amount of cards have been changed as playing with 20 or 25 cards per suit requires an excessive amount of effort and not 
feasable for the player
- The timer is in a different position for a more efficient layout

Known Bugs/Errors:
- The last card may not update correctly once it has been placed in its respective pile before completing the game.
- Although this may not be a bug/error, I have realized at the last minute that the images for cards with a value above king do not show 
their value when stacked because the cards in front are covering the displayed value.
- The leaderboard text in the options menu will break if the names are excessively long

Other Info:
- The game will automatically terminate if the leaderboard file is not found
- The instructions and about menu are stored in the same JFrame
- Input from the leaderboard file has minimal error detection and will not detect invalid scores, although the file is not meant to be 
modified by the user.