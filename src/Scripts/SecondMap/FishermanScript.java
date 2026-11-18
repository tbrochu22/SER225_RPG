package Scripts.SecondMap;

import Battle.SacredFlame;
import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

// script for talking to the fisherman npc
public class FishermanScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            // Stage 3: quest finished
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("fishermanQuestComplete", true));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Thanks again for finding that gem!");
                    addText("I can finally pay off my debts to the merchant....");
                    addText("Hopefully he won't be too mad..."); 
                    addText("About the whole 'giant red octopus' thing.");
                    addText("Anyway, I should get back to fishing.");
                    addText("I hope you enjoy your new power!");
                }});
            }});

            // Stage 2: player has the item, hand it in
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("fishermanQuestStarted", true));
                addRequirement(new FlagRequirement("hasGem", true));
                addRequirement(new FlagRequirement("fishermanQuestComplete", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("You found the gem!");
                    addText("Incredible work hero!");
                    addText("Now I can pay off my debts to the merchant!");
                    addText("Surely...");
                    addText("Anyway, here's your reward!");
                    addText("It'll allow you to harness the power of fire!");
                    addText("You obtained a Flame Emblem!");
                    addText("You learned "
                            + new SacredFlame().getName() + "!");
                    addText("Use 'E' to open the menu and equip it!");
                }});
                addScriptAction(new ChangeFlagScriptAction("fishermanQuestComplete", true));
                addScriptAction(new ChangeFlagScriptAction("move4Unlocked", true));
            }});

            // Stage 1b: quest started, player doesn't have the item yet
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("fishermanQuestStarted", true));
                addRequirement(new FlagRequirement("hasGem", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Any luck finding the gem?");
                    addText("Remember, the clue I gave you."); 
                    addText("'Between the clay, a stone must lie.'");
                    addText("'Only then can the jewel be seen with the eye.'");
                    addText("Now go forth and solve the pyramid's mystery!");
                }});
            }});

            // Stage 1a: quest not started yet
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("fishermanQuestStarted", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Shh... you'll scare the fish.");
                    addText("I once caught one bigger than a cactus!");
                    addText("Nobody ever believes me.");
                    addText("...");
                    addText("You're still here?");
                    addText("Hmm..."); 
                    addText("Perhaps you can indulge me with a favor.");
                    addText("There's this pyramid east of here.");
                    addText("Inside, there's a puzzle that needs solving.");
                    addText("Try as I might, I can't seem to figure it out.");
                    addText("Legend says that solving it will reward you.");
                    addText("With an ancient gemstone.");
                    addText("That gem must be worth a lot of money, right?");
                    addText("I would be forever grateful if you could retrieve it for me.");
                    addText("If you do, I have a gift for you in return.");
                    addText("There's a clue that I once uncovered about the puzzle.");
                    addText("I shall read it to you.");
                    addText("'Between the clay, a stone must lie.'");
                    addText("'Only then can the jewel be seen with the eye.'");
                    addText("Here. It's the key to getting into the pyramid.");
                    addText("Now go forth! Solve the pyramid's mystery!");
                }});
                addScriptAction(new ChangeFlagScriptAction("fishermanQuestStarted", true));
            }});

        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}