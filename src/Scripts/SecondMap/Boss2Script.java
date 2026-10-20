package Scripts.SecondMap;

import java.util.ArrayList;

import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.NPCFacePlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

// script for talking to the second boss (the red octopus at the end of the desert path)
// ends by signaling to the play level screen that it's time to cut away to the boss fight
public class Boss2Script extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("Another one crawls into my desert...");
            addText("The slime was weak. I am not.");
            addText("I will wrap you up and squeeze!");
        }});

        // unlock the player before cutting away so they aren't frozen when control returns to the map
        scriptActions.add(new UnlockPlayerScriptAction());

        // alert all listeners (which includes play level screen) that it's time to cut away to the fight
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                for (GameListener listener : listeners) {
                    listener.onFight();
                }
                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}