package Scripts.SecondMap;

import Level.Script;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.NPCFacePlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import java.util.ArrayList;

// script for talking to the nomad at the start of the desert
public class NomadScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("Ah, a traveler! You made it past the slime?");
            addText("Careful down this path.");
            addText("Something big and red lurks at the end of it.");
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}