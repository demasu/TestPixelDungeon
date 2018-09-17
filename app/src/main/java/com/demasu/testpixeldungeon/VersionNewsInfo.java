package com.demasu.testpixeldungeon;


import com.demasu.testpixeldungeon.ui.Window;
import com.demasu.testpixeldungeon.windows.WndWelcome;

/**
 * Created by Moussa on 23-Jan-17.
 */
public class VersionNewsInfo {
    public static int versionBuild = 0;
    public static String message = "Welcome to Skillful PixelDungeon!\n \n \n"
            + "The game has been given a complete makeover in terms of class skills and balance.\n \n"
            + "Old ranking details cannot be displayed at this time, but rest assured they are not lost.\n \n"
            + "As of 0.2.3b (Build 28): \n"
            + "- Mercs added\n"
            + "- You cannot hire the merc equivalent to your class.\n"
            + "- Merc equiped weapon and armor affect their stat.\n"
            + "- Enchanted weapons and armors equiped onto mercs take full effect.\n"
            + "Have fun :D";

    private static boolean alreadySeen = false;

    public static boolean haveMessage () {
        return !isAlreadySeen();
    }

    public static String getMessage () {
        setAlreadySeen( true );
        return message;
    }

    public static Window getWelcomeWindow () {
        setAlreadySeen( true );
        return ( new WndWelcome( message ) );
    }

    public static boolean isAlreadySeen () {
        return alreadySeen;
    }

    public static void setAlreadySeen ( boolean alreadySeen ) {
        VersionNewsInfo.alreadySeen = alreadySeen;
    }
}
