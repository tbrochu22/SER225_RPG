package Scripts.TestMap;

import java.util.ArrayList;

import Level.GameListener;
import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.NPCFacePlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

// script for talking to the boss npc
// ends by signaling to the play level screen that it's time to cut away to the boss fight
public class Boss1Script extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("You dare enter my domain?");
            addText("I've squashed heroes far braver than you, little cat.");
            addText("Let's see if you can survive this...");
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