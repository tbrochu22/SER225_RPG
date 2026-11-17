package Scripts.ThirdMap;

import java.util.ArrayList;

import Battle.Taunt;
import Level.Script;
import ScriptActions.*;

// script for talking to the imp near the entrance of the volcano map
// the imp challenges the player to race to the checkered finish line at the end of the path before the timer runs out
// the race itself (timer, finish line, win/lose) is run by PlayLevelScreen: this script only sets "impRaceRunning" to start it
public class ImpScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            // Stage 3: the player already won, the imp is sulking at the finish line
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("impRaceWon", true));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Hmph. Beginner's luck, that's all it was.");
                    addText("Go on, use " + new Taunt().getName() + ". Make 'em as mad as you made me!");
                }});
            }});

            // Stage 2: the race was offered before (and lost or abandoned), offer a rematch
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("impRaceStarted", true));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Back again, slowpoke?");
                    addText("Race me again?", new String[] { "Yes", "No" });
                }});
                addScriptAction(new ConditionalScriptAction() {{
                    addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        addRequirement(new CustomRequirement() {
                            @Override
                            public boolean isRequirementMet() {
                                int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                return answer == 0;
                            }
                        });
                        addScriptAction(new TextboxScriptAction() {{
                            addText("Same rules. Reach the checkered line at the end.");
                            addText("Ready... set... GO!");
                        }});
                        addScriptAction(new ChangeFlagScriptAction("impRaceRunning", true));
                    }});
                    addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        addRequirement(new CustomRequirement() {
                            @Override
                            public boolean isRequirementMet() {
                                int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                return answer == 1;
                            }
                        });
                        addScriptAction(new TextboxScriptAction("Heh. Scared? I'll be right here."));
                    }});
                }});
            }});

            // Stage 1: first time talking, the imp explains the race and starts it
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("impRaceStarted", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Hee hee! A knight, all the way out here?");
                    addText("You look slow. Real slow. Clanky, even.");
                    addText("I'll make you a deal.");
                    addText("Race to the end of this path before time runs out.");
                    addText("Cross the checkered line in the corner to win.");
                    addText("Win, and I'll teach you how to make enemies furious.");
                    addText("Tip: hold SHIFT to run. You'll need it.");
                    addText("Ready?", new String[] { "Let's go!", "Not yet" });
                }});
                addScriptAction(new ChangeFlagScriptAction("impRaceStarted", true));
                addScriptAction(new ConditionalScriptAction() {{
                    addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        addRequirement(new CustomRequirement() {
                            @Override
                            public boolean isRequirementMet() {
                                int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                return answer == 0;
                            }
                        });
                        addScriptAction(new TextboxScriptAction("Ready... set... GO!"));
                        addScriptAction(new ChangeFlagScriptAction("impRaceRunning", true));
                    }});
                    addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        addRequirement(new CustomRequirement() {
                            @Override
                            public boolean isRequirementMet() {
                                int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                return answer == 1;
                            }
                        });
                        addScriptAction(new TextboxScriptAction("Ha! Come back when you've worked up the nerve."));
                    }});
                }});
            }});

        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}