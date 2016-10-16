package rpg.engine;

public interface Action {
    /**String run(**args) function takes an arbitrary amount of string arguments and returns a string
     * code to be added to the story string*/
    String run(String[] args);
}
