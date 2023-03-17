package Entity;

public class PlayerSave {

    private static int health = 5;
    private static int score=0;
    Player player;

    public static void init() {

        health = 5;
    }


    public static int getHealth() { return health; }
    public static void setHealth(int i) { health = i; }
    public static void setScore(int i){score=i;}
    public static int getScore(){return score;}

}
