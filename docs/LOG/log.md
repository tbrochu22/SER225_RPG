Sprint Log 

Sprint 1 
    Boss NPC + Cutaway Screen

    New files

    src/NPCs/Boss1.java 
        Loads Boss1.png as a sprite

    src/Scripts/TestMap/Boss1Script.java 
        Locks the player, faces the boss toward them, plays 3 lines of dialogue through a textbox, unlocks the player, then tells all GameListeners a fight just started.

    src/Screens/CutawayScreen.java 
        Just a black screen that says "Cutaway not created" / "Press Escape to return to the game" for now. Escape sends you back to PlayLevelScreen.

    Resources/Boss1.png 
        New sprite

    src/Level/GameListener.java 
        Added onFight() next to onWin().

    src/Maps/TestMap.java 
        Added the Boss1 NPC to loadNPCs(), placed at tile (18, 6), set Boss1Script as its interact script.

    src/Screens/PlayLevelScreen.java
        Added a cutawayScreen field, created in initialize()
        Added a CUTAWAY state to PlayLevelScreenState
        Put CUTAWAY into update() and draw()
        Added onFight() to switch into CUTAWAY state
        Added endCutaway() to switch back to RUNNING

Sprint 2