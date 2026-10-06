package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.NPCFacePlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

// script for talking to the mage npc at the start of the rocky path
// gives the player a short introduction
public class MageScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("Ah, a traveler! Welcome.");
            addText("I'm the Mage who watches over this path");
            addText("Follow it carefully, and keep an eye out!");
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}