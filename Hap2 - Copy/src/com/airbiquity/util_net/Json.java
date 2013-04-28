package com.airbiquity.util_net;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json
{
  /**
   * Get boolean from given JSON object by given key.
   * @param j : the JSONObject to use.
   * @param id : ID of the value to get.
   * @param deflt : default value.
   * @return boolean for the given ID or the default value if not found. 
   */
  public static boolean gb( JSONObject j, String id, boolean deflt )
  {
    try
    {
      return j.getBoolean( id );
    }
    catch( JSONException e )
    {
      return deflt;
    }
  }

  /**
   * Get integer from given JSON object by given key.
   * @param j : the JSONObject to use.
   * @param id : ID of the integer to get.
   * @param deflt : default value.
   * @return integer for the given ID or the default value if not found. 
   */
  public static int gi( JSONObject j, String id, int deflt )
  {
    try
    {
      return j.getInt( id );
    }
    catch( JSONException e )
    {
      return deflt;
    }
  }
  
  /**
   * Get long from given JSON object by given key.
   * @param j : the JSONObject to use.
   * @param id : ID of the long to get.
   * @param deflt : default value.
   * @return long for the given ID or the default value if not found. 
   */
  public static long gl( JSONObject j, String id, long deflt )
  {
    try
    {
      return j.getLong( id );
    }
    catch( JSONException e )
    {
      return deflt;
    }
  }
  
  /**
   * Get double from given JSON object by given key.
   * @param j : the JSONObject to use.
   * @param id : ID of the double to get.
   * @param deflt : default value.
   * @return double for the given ID or the default value if not found. 
   */
  public static double gd( JSONObject j, String id, double deflt )
  {
    try
    {
      return j.getDouble( id );
    }
    catch( JSONException e )
    {
      return deflt;
    }
  }

  /**
   * Get String from given JSON object by given key.
   * @param j : the JSONObject to use.
   * @param id : ID of the string to get.
   * @param deflt : default value.
   * @return string for the given ID or the default value if not found. 
   */
  public static String gs( JSONObject j, String id, String deflt )
  {
    try
    {
      return j.getString( id );
    }
    catch( JSONException e )
    {
      return deflt;
    }
  }

  
  /**
   * Get JSONArray.
   * @param id : ID of the JSONArray to get.
   * @param deflt : default value.
   * @return JSONArray for the given ID or the default value if not found. 
   */
  public static JSONArray ga( JSONObject j, String id, JSONArray deflt )
  {
    try
    {
      return j.getJSONArray( id );
    }
    catch( JSONException e )
    {
      return deflt;
    }
  }
  
  
  /**
   * Parse a list of int values.
   * @param list : string that contains a list of int values in JSON format.
   * @return List of Integers.
   * @throws JSONException if the content is not in a valid JSON format.
   */
  static public ArrayList<Integer> parseListInts( String list ) throws JSONException
  {
    JSONArray jItems = new JSONArray(list);    
    ArrayList<Integer> vals = new ArrayList<Integer> ( jItems.length() );
    
    for( int i = 0; i < jItems.length(); i++ )
    {
      int v = jItems.getInt( i );
      vals.add( v );
    }
  
    return vals;    
  } 
  
  
}
