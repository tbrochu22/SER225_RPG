package Scripts.TestMap;

import Level.Script;
import ScriptActions.ChangeFlagScriptAction;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import java.util.ArrayList;

public class GiftScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("You found a gift!");
        }});

        scriptActions.add(new ChangeFlagScriptAction("hasGift", true));

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}