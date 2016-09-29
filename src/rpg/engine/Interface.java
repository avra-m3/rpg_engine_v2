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
                    "lad"
            };
    String[] GENDER_TERMS_2 =
            {
                    "son",
                    "lass",
                    "you"
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