/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.engine;

/**
 *
 * @author A
 */
interface Action
{
    String run(String[] args);
}
interface StoryGuide
{

    // Constant lists
    String[] GENDER_TERMS_1 =
            {
                    "boy",
                    "girl",
                    "lizard person"
            };
    String[] GENDER_TERMS_2 =
            {
                    "lad",
                    "lass",
                    "you"
            };
    String[] GENDER_TERMS_3 =
            {
                    "young man",
                    "young lady",
                    "young one"
            };
    String[] NAMES_MALE =
            {
                    "John",
                    "Davy",
                    "Jean-Luc",
                    "Chelton",
                    "John",
                    "Nick",
            };
    String[] NAMES_FEMALE =
            {
                    "Jane",
                    "Emilia",
                    "Katherine",
                    "Joan",
                    "Yooor",
                    "Kate",
                    "Sarah",
            };
    String[] NAMES_LASTNAMES =
            {
                    "Mulgrew",
                    "Evans",
                    "Einstein",
                    "Doe",
                    "Smith",
                    "Hammer",
                    "Cage",
                    "Cena",
                    "Mother",
                    "Janeway",
                    "Picard",
            };
}