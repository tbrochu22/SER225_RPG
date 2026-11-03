package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import Battle.HighJumpKick;
import ScriptActions.*;

// script for talking to the mage npc at the start of the rocky path
// gives the player a short introduction
public class MageScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            //Stage 4
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
            addRequirement(new FlagRequirement("killQuestComplete", true));
            addScriptAction(new TextboxScriptAction("The path is much safer thanks to you."));
            }});
            //Stage 3
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
            addRequirement(new FlagRequirement("killQuestReady", true));
            addRequirement(new FlagRequirement("killQuestComplete", false));
            addScriptAction(new TextboxScriptAction() {{
            addText("You defeated the beasts roaming this path!");
            addText("As thanks, let me teach you a technique.");
            addText("You learned " + new HighJumpKick().getName() + "!");
            addText("Use 'E' to open the menu and equip it!");
            }});
            addScriptAction(new ChangeFlagScriptAction("killQuestStarted", true));
            addScriptAction(new ChangeFlagScriptAction("killQuestComplete", true));
            addScriptAction(new ChangeFlagScriptAction("move7Unlocked", true));
            }});
            //Stage 2
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
            addRequirement(new FlagRequirement("killQuestStarted", true));
            addScriptAction(new TextboxScriptAction() {{
            addText("Some of those beasts are still roaming around.");
            addText("Defeat all three and come back to me!");
            }});
            }});
            //Stage 1
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
            addRequirement(new FlagRequirement("killQuestStarted", false));
            addScriptAction(new TextboxScriptAction() {{
            addText("Ah, a traveler! Welcome.");
            addText("There are dangerous creatures here.");
            addText("Worse, three beasts roam freely along this path.");
            addText("Defeat all three and I will teach you a secret move.");
        }});
    addScriptAction(new ChangeFlagScriptAction("killQuestStarted", true));
}});
        }});
        

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}