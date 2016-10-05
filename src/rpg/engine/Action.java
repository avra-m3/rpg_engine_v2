package rpg.engine;

/** Interface action used as a template for mapping story actions to functions in java*/
public interface Action
{
    /**String run(**args) function takes an arbitrary amount of string arguments and returns a string
     * code to be added to the story string*/
    String run(String[] args);
}
