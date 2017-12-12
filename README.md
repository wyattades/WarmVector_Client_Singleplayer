# WarmVector
Shooting bad guys, randomly generated levels, destructable terrain, what's not to love?  
WarmVector was created with my 2D Java game engine, which mainly served as a learning experience while I experimented with Java multi-threading and OpenGL. It turned out to be a pretty fun game (but is still poorly optimized because it doesn't utilize OpenGL quite correctly). Have fun!

![](https://i.imgur.com/nGcNT4U.gif)  

## Download and Run
- [Java Binary](https://drive.google.com/open?id=0BxT-nCrEKjxEWHV3bDJpQkh2b2s) - Unzip and double-click included .jar file (you will need Java 8 runtime)

## Known Bugs
- background music is never unloaded
- deltaTime should be used to ensure consistent game updates
- Introduction of OpenGL causes very noticeable screen-tearing
- SFX are played back with slight delay, and sometimes not at all

## Planned Features
- Create more weapon/bullet types, e.g. rockets, explosives, magic??
- Continue developing level difficulty progression
- Add random boxes to middle of rooms (with enough space to move around them!)
