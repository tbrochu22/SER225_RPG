package Scripts.TestMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

// script for talking to the archer npc, placed partway down the rocky path
public class ArcherScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            // Stage 3: quest finished
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("archerQuestComplete", true));
                addScriptAction(new TextboxScriptAction("Thanks again! I can enjoy my gift."));
            }});

            // Stage 2: player has the item, hand it in
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("archerQuestStarted", true));
                addRequirement(new FlagRequirement("hasGift", true));
                addRequirement(new FlagRequirement("archerQuestComplete", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("You found my gift!");
                    addText("Thank you so much!");
                }});
                addScriptAction(new ChangeFlagScriptAction("archerQuestComplete", true));
            }});

            // Stage 1b: quest started, player doesn't have the item yet
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("archerQuestStarted", true));
                addRequirement(new FlagRequirement("hasGift", false));
                addScriptAction(new TextboxScriptAction("Any luck finding my gift?"));
            }});

            // Stage 1a: quest not started yet
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("archerQuestStarted", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Hi!");
                    addText("I lost my gift somewhere around here.");
                    addText("Could you help me find it?");
                }});
                addScriptAction(new ChangeFlagScriptAction("archerQuestStarted", true));
            }});

        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}