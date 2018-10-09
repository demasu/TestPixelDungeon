/*
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Bundle {

    private static final String CLASS_NAME = "__className";

    private static final HashMap<String, String> aliases = new HashMap<>();

    private final JSONObject data;

    public Bundle () {
        this( new JSONObject() );
    }

    private Bundle ( JSONObject data ) {
        this.data = data;
    }

    public static Bundle read ( InputStream stream ) {

        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader( stream ) );

            StringBuilder all = new StringBuilder();
            String line = reader.readLine();
            while ( line != null ) {
                all.append( line );
                line = reader.readLine();
            }

            JSONObject json = (JSONObject) new JSONTokener( all.toString() ).nextValue();
            reader.close();

            return new Bundle( json );
        } catch ( Exception e ) {
            return null;
        }
    }

    public static boolean write ( Bundle bundle, OutputStream stream ) {
        try {
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( stream ) );
            writer.write( bundle.getData().toString() );
            writer.close();

            return true;
        } catch ( IOException e ) {
            return false;
        }
    }

    public static void addAlias ( Class<?> cl, String alias ) {
        aliases.put( alias, cl.getName() );
    }

    public String toString () {
        return getData().toString();
    }

    public boolean isNull () {
        return getData() == null;
    }

    public boolean contains ( String key ) {
        return !getData().isNull( key );
    }

    public boolean getBoolean ( String key ) {
        return getData().optBoolean( key );
    }

    public int getInt ( String key ) {
        return getData().optInt( key );
    }

    public float getFloat ( String key ) {
        return (float) getData().optDouble( key );
    }

    public String getString ( String key ) {
        return getData().optString( key );
    }

    public Bundle getBundle ( String key ) {
        return new Bundle( getData().optJSONObject( key ) );
    }

    private Bundlable get () {
        try {
            String clName = getString( CLASS_NAME );
            if ( aliases.containsKey( clName ) ) {
                clName = aliases.get( clName );
            }

            Class<?> cl = Class.forName( clName );
            if ( cl != null ) {
                Bundlable object = (Bundlable) cl.newInstance();
                object.restoreFromBundle( this );
                return object;
            } else {
                return null;
            }
        } catch ( Exception e ) {
            return null;
        }
    }

    public Bundlable get ( String key ) {
        return getBundle( key ).get();
    }

    public <E extends Enum<E>> E getEnum ( String key, Class<E> enumClass ) {
        try {
            return Enum.valueOf( enumClass, getData().getString( key ) );
        } catch ( JSONException e ) {
            return enumClass.getEnumConstants()[0];
        }
    }

    public int[] getIntArray ( String key ) {
        try {
            JSONArray array = getData().getJSONArray( key );
            int length = array.length();
            int[] result = new int[length];
            for ( int i = 0; i < length; i++ ) {
                result[i] = array.getInt( i );
            }
            return result;
        } catch ( JSONException e ) {
            return null;
        }
    }

    public boolean[] getBooleanArray ( String key ) {
        try {
            JSONArray array = getData().getJSONArray( key );
            int length = array.length();
            boolean[] result = new boolean[length];
            for ( int i = 0; i < length; i++ ) {
                result[i] = array.getBoolean( i );
            }
            return result;
        } catch ( JSONException e ) {
            return null;
        }
    }

    public String[] getStringArray ( String key ) {
        try {
            JSONArray array = getData().getJSONArray( key );
            int length = array.length();
            String[] result = new String[length];
            for ( int i = 0; i < length; i++ ) {
                result[i] = array.getString( i );
            }
            return result;
        } catch ( JSONException e ) {
            return null;
        }
    }

    public Collection<Bundlable> getCollection ( String key ) {

        ArrayList<Bundlable> list = new ArrayList<>();

        try {
            JSONArray array = getData().getJSONArray( key );
            for ( int i = 0; i < array.length(); i++ ) {
                list.add( new Bundle( array.getJSONObject( i ) ).get() );
            }
        } catch ( JSONException e ) {

        }

        return list;
    }

    public void put ( String key, boolean value ) {
        try {
            getData().put( key, value );
        } catch ( JSONException e ) {

        }
    }

    public void put ( String key, int value ) {
        try {
            getData().put( key, value );
        } catch ( JSONException e ) {

        }
    }

    public void put ( String key, float value ) {
        try {
            getData().put( key, value );
        } catch ( JSONException e ) {

        }
    }

    public void put ( String key, String value ) {
        try {
            getData().put( key, value );
        } catch ( JSONException e ) {

        }
    }

    public void put ( String key, Bundle bundle ) {
        try {
            getData().put( key, bundle.getData() );
        } catch ( JSONException e ) {

        }
    }

    public void put ( String key, Bundlable object ) {
        if ( object != null ) {
            try {
                Bundle bundle = new Bundle();
                bundle.put( CLASS_NAME, object.getClass().getName() );
                object.storeInBundle( bundle );
                getData().put( key, bundle.getData() );
            } catch ( JSONException e ) {
            }
        }
    }

    public void put ( String key, Enum<?> value ) {
        if ( value != null ) {
            try {
                getData().put( key, value.name() );
            } catch ( JSONException e ) {
            }
        }
    }

    public void put ( String key, int[] array ) {
        try {
            JSONArray jsonArray = new JSONArray();
            for ( int i = 0; i < array.length; i++ ) {
                jsonArray.put( i, array[i] );
            }
            getData().put( key, jsonArray );
        } catch ( JSONException e ) {

        }
    }

    public void put ( String key, boolean[] array ) {
        try {
            JSONArray jsonArray = new JSONArray();
            for ( int i = 0; i < array.length; i++ ) {
                jsonArray.put( i, array[i] );
            }
            getData().put( key, jsonArray );
        } catch ( JSONException e ) {

        }
    }

    public void put ( String key, String[] array ) {
        try {
            JSONArray jsonArray = new JSONArray();
            for ( int i = 0; i < array.length; i++ ) {
                jsonArray.put( i, array[i] );
            }
            getData().put( key, jsonArray );
        } catch ( JSONException e ) {

        }
    }

    public void put ( String key, Collection<? extends Bundlable> collection ) {
        JSONArray array = new JSONArray();
        for ( Bundlable object : collection ) {
            Bundle bundle = new Bundle();
            bundle.put( CLASS_NAME, object.getClass().getName() );
            object.storeInBundle( bundle );
            array.put( bundle.getData() );
        }
        try {
            getData().put( key, array );
        } catch ( JSONException e ) {

        }
    }

    private JSONObject getData () {
        return data;
    }
}
