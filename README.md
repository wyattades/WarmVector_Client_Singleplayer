# WarmVector_Client_Singleplayer_V1.03

Current Bugs:
- When a bullet goes through multiple entities, sometimes the particle effects will be the wrong color
- Strange camera jittering when the cursor is in certain positions
SOLVED - When opening/closing the pause menu, the default mouse cursor overrides the custom cursor until you click
- Weapon hit boxes are not big enough (square?)
SOLVED - Intro gif only fits with 1920x1080p display
- After pressing resume in pause menu, can't open pause menu again

To Do:
- Redo everything about the cursor (Reason: lots of bugs, inefficiently written)
- Add more levels
- Rewrite playState class (Reason: inefficiently written, OCD)
- Minimize the rendering of built-in Graphics2D objects e.g. drawString(), and maximize the use of bufferedImages