/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.engine;
interface CONST
{
    /** Script data format constants */
    interface READ {
        String CODE_DELIM = "+";
        String QUERY_DELIM = "&";
        String SPLIT_DELIM_1 = ";";
        String OR_CODE_DELIM = "|";
        String SPLIT_DELIM_2 = ":";
        String VAR_DELIM_1 = "<";
        String VAR_DELIM_2 = ">";
        String IGNORE_DELIM = "/";
        String FUNCTION_DELIM = "%";
        String ARGUMENT_DELIM = ",";
    }
    /** Save data format constants */
    interface SAVE {
        String VAR_STR_DELIM = "$";
        String VAR_VAL_DELIM = "=";
        String VAR_TYPE_DELIM = ":";
    }
}