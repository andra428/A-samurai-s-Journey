package GameState;


public class GameStateManager {

    private GameState[] gameStates;
    private int currentState;

    public static final int NUMGAMESTATES = 8;

    public static final int MENUSTATE = 0;

    public static final int LEVEL1STATE = 1;
    public static final int LEVEL2STATE = 2;
    public static final int GAMEOVER = 3;

    public static final int FINISHSTATE=4;
    public static final int LEVEL3STATE=5;

    public static final int LEVEL=6;
    public static final int RULES=7;



    public GameStateManager() {

        gameStates = new GameState[NUMGAMESTATES];

        currentState = MENUSTATE;
        loadState(currentState);

    }

    private void loadState(int state) {
        if(state == MENUSTATE)
            gameStates[state] = new MenuState(this);
        else if(state == LEVEL1STATE)
            gameStates[state] = new Level1State(this);
            else if(state == LEVEL2STATE)
            gameStates[state] = new LEVEL2STATE(this);
                else if(state == GAMEOVER)
                    gameStates[state] = new GameOver(this);

                         else if(state == FINISHSTATE)
                           gameStates[state] = new FinishState(this);
                              else if(state == LEVEL3STATE)
                                gameStates[state] = new LEVEL3STATE(this);

                                         else if(state == LEVEL)
                                          gameStates[state] = new Level(this);
                                                 else if(state == RULES)
                                                 gameStates[state] = new RulesState(this);
    }

    private void unloadState(int state) {

        gameStates[state] = null;
    }

    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);

    }

    public void update() {
        if(gameStates[currentState] != null) {
            gameStates[currentState].update();
        }
    }

    public void draw(java.awt.Graphics2D g) {
        if(gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
        }
    }

    public void keyPressed(int k) {

        if(gameStates[currentState] != null)
            gameStates[currentState].keyPressed(k);
    }

    public void keyReleased(int k) {

        if(gameStates[currentState] != null)
            gameStates[currentState].keyReleased(k);
    }

}