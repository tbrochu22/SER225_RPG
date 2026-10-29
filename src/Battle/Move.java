package Battle;

public class Move {
    protected String name;
    protected int power;
    protected int effect;
    protected int accuracy;
    protected int uses;
    protected String type;
    protected String description;

    public Move(String name, int power, int effect, int accuracy, int uses, String type, String description){
        this.name = name;
        this.power = power;
        this.effect = effect;
        this.accuracy = accuracy;
        this.uses = uses;
        this.type = type;
        this.description = description;
    }

    public String getName(){
        return name;
    }
    public int getPower(){
        return power;
    }
    public int getEffect(){
        return effect;
    }
    public int getAccuracy(){
        return accuracy;
    }
    public int getUses(){
        return uses;
    }
    public String getType(){
        return type;
    }
    public String getDescription(){
        return description;
    }
}
