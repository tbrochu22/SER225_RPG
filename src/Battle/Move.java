package Battle;

public class Move {
    protected String name;
    protected int power;
    protected int accuracy;
    protected String type;

    public Move(String name, int power, int accuracy, String type){
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        this.type = type;
    }

    public String getName(){
        return name;
    }
    public int getPower(){
        return power;
    }
    public int getAccuracy(){
        return accuracy;
    }
    public String getType(){
        return type;
    }
}
