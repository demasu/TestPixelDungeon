/*
 * Pixel Dungeon
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
package com.demasu.testpixeldungeon;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.scenes.TitleScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import javax.microedition.khronos.opengles.GL10;

public class PixelDungeon extends Game {

    private static boolean immersiveModeChanged = false;

    /*
    TODO: Abstract out the bundle calls if possible
     */
    @SuppressWarnings ( "FeatureEnvy" )
    public PixelDungeon () {
        super( TitleScene.class );

        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.scrolls.ScrollOfUpgrade.class,
                "com.watabou.pixeldungeon.items.scrolls.ScrollOfEnhancement" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.actors.blobs.WaterOfHealth.class,
                "com.watabou.pixeldungeon.actors.blobs.Light" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.rings.RingOfMending.class,
                "com.watabou.pixeldungeon.items.rings.RingOfRejuvenation" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.wands.WandOfReach.class,
                "com.watabou.pixeldungeon.items.wands.WandOfTelekenesis" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.actors.blobs.Foliage.class,
                "com.watabou.pixeldungeon.actors.blobs.Blooming" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.actors.buffs.Shadows.class,
                "com.watabou.pixeldungeon.actors.buffs.Rejuvenation" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.scrolls.ScrollOfPsionicBlast.class,
                "com.watabou.pixeldungeon.items.scrolls.ScrollOfNuclearBlast" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.actors.hero.Hero.class,
                "com.watabou.pixeldungeon.actors.Hero" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.actors.mobs.npcs.Shopkeeper.class,
                "com.watabou.pixeldungeon.actors.mobs.Shopkeeper" );
        // 1.6.1
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.quest.DriedRose.class,
                "com.watabou.pixeldungeon.items.DriedRose" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.actors.mobs.npcs.MirrorImage.class,
                "com.watabou.pixeldungeon.items.scrolls.ScrollOfMirrorImage$MirrorImage" );
        // 1.6.4
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.rings.RingOfElements.class,
                "com.watabou.pixeldungeon.items.rings.RingOfCleansing" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.rings.RingOfElements.class,
                "com.watabou.pixeldungeon.items.rings.RingOfResistance" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.weapon.missiles.Boomerang.class,
                "com.watabou.pixeldungeon.items.weapon.missiles.RangersBoomerang" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.rings.RingOfPower.class,
                "com.watabou.pixeldungeon.items.rings.RingOfEnergy" );
        // 1.7.2
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.plants.Dreamweed.class,
                "com.watabou.pixeldungeon.plants.Blindweed" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.plants.Dreamweed.Seed.class,
                "com.watabou.pixeldungeon.plants.Blindweed$Seed" );
        // 1.7.4
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.weapon.enchantments.Shock.class,
                "com.watabou.pixeldungeon.items.weapon.enchantments.Piercing" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.weapon.enchantments.Shock.class,
                "com.watabou.pixeldungeon.items.weapon.enchantments.Swing" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.scrolls.ScrollOfEnchantment.class,
                "com.watabou.pixeldungeon.items.scrolls.ScrollOfWeaponUpgrade" );
        // 1.7.5
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.scrolls.ScrollOfEnchantment.class,
                "com.watabou.pixeldungeon.items.Stylus" );
        // 1.8.0
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.actors.mobs.FetidRat.class,
                "com.watabou.pixeldungeon.actors.mobs.npcs.Ghost$FetidRat" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.plants.Rotberry.class,
                "com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker$Rotberry" );
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.plants.Rotberry.Seed.class,
                "com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker$Rotberry$Seed" );
        // 1.9.0
        com.watabou.utils.Bundle.addAlias(
                com.demasu.testpixeldungeon.items.wands.WandOfReach.class,
                "com.watabou.pixeldungeon.items.wands.WandOfTelekinesis" );
    }

    public static void switchNoFade ( Class<? extends PixelScene> c ) {
        PixelScene.noFade = true;
        switchScene( c );
    }

    public static void landscape ( boolean value ) {
        Game.getInstance().setRequestedOrientation( value ?
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        Preferences.INSTANCE.put( Preferences.KEY_LANDSCAPE, value );
    }

    /*
     * ---> Prefernces
     */

    public static boolean landscape () {
        return getWidth() > getHeight();
    }

    @SuppressLint ( "NewApi" )
    public static void immerse ( boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_IMMERSIVE, value );

        getInstance().runOnUiThread( new Runnable() {
            @Override
            public void run () {
                updateImmersiveMode();
                setImmersiveModeChanged( true );
            }
        } );
    }

    // *** IMMERSIVE MODE ****

    @SuppressLint ( "NewApi" )
    private static void updateImmersiveMode () {
        try {
            // Sometime NullPointerException happens here
            getInstance().getWindow().getDecorView().setSystemUiVisibility(
                    immersed() ?
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            :
                            0 );
        } catch ( Exception e ) {
            reportException( e );
        }
    }

    public static boolean immersed () {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_IMMERSIVE, false );
    }

    public static void scaleUp ( boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_SCALE_UP, value );
        switchScene( TitleScene.class );
    }

    public static boolean scaleUp () {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_SCALE_UP, true );
    }

    public static void zoom ( int value ) {
        Preferences.INSTANCE.put( Preferences.KEY_ZOOM, value );
    }

    // *****************************

    public static int zoom () {
        return Preferences.INSTANCE.getInt( Preferences.KEY_ZOOM, 0 );
    }

    public static void music ( boolean value ) {
        Music.INSTANCE.enable( value );
        Preferences.INSTANCE.put( Preferences.KEY_MUSIC, value );
    }

    public static boolean music () {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_MUSIC, true );
    }

    public static void soundFx ( boolean value ) {
        Sample.INSTANCE.enable( value );
        Preferences.INSTANCE.put( Preferences.KEY_SOUND_FX, value );
    }

    public static boolean soundFx () {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_SOUND_FX, true );
    }

    public static void itemDeg ( boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_DEGRADATION, value );
    }

    public static boolean itemDeg () {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_DEGRADATION, true );
    }

    public static void brightness ( boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_BRIGHTNESS, value );
        if ( scene() instanceof GameScene ) {
            ( (GameScene) scene() ).brightness( value );
        }
    }

    public static boolean brightness () {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_BRIGHTNESS, false );
    }

    public static void lastClass ( int value ) {
        Preferences.INSTANCE.put( Preferences.KEY_LAST_CLASS, value );
    }

    public static int lastClass () {
        return Preferences.INSTANCE.getInt( Preferences.KEY_LAST_CLASS, 0 );
    }

    public static void challenges ( int value ) {
        Preferences.INSTANCE.put( Preferences.KEY_CHALLENGES, value );
    }

    public static int challenges () {
        return Preferences.INSTANCE.getInt( Preferences.KEY_CHALLENGES, 0 );
    }

    public static void intro ( boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_INTRO, value );
    }

    public static boolean intro () {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_INTRO, true );
    }

    public static void reportException ( Throwable tr ) {
        Log.e( "PD", Log.getStackTraceString( tr ) );
    }

    private static boolean isImmersiveModeChanged () {
        return immersiveModeChanged;
    }

    private static void setImmersiveModeChanged ( boolean immersiveModeChanged ) {
        PixelDungeon.immersiveModeChanged = immersiveModeChanged;
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        updateImmersiveMode();

        DisplayMetrics metrics = new DisplayMetrics();
        getInstance().getWindowManager().getDefaultDisplay().getMetrics( metrics );
        boolean landscape = metrics.widthPixels > metrics.heightPixels;

        if ( Preferences.INSTANCE.getBoolean( Preferences.KEY_LANDSCAPE, false ) != landscape ) {
            landscape( !landscape );
        }

        Music.INSTANCE.enable( music() );
        Sample.INSTANCE.enable( soundFx() );

        Sample.INSTANCE.load(
                Assets.SND_CLICK,
                Assets.SND_BADGE,
                Assets.SND_GOLD,

                Assets.SND_DESCEND,
                Assets.SND_STEP,
                Assets.SND_WATER,
                Assets.SND_OPEN,
                Assets.SND_UNLOCK,
                Assets.SND_ITEM,
                Assets.SND_DEWDROP,
                Assets.SND_HIT,
                Assets.SND_MISS,
                Assets.SND_EAT,
                Assets.SND_READ,
                Assets.SND_LULLABY,
                Assets.SND_DRINK,
                Assets.SND_SHATTER,
                Assets.SND_ZAP,
                Assets.SND_LIGHTNING,
                Assets.SND_LEVELUP,
                Assets.SND_DEATH,
                Assets.SND_CHALLENGE,
                Assets.SND_CURSED,
                Assets.SND_EVOKE,
                Assets.SND_TRAP,
                Assets.SND_TOMB,
                Assets.SND_ALERT,
                Assets.SND_MELD,
                Assets.SND_BOSS,
                Assets.SND_BLAST,
                Assets.SND_PLANT,
                Assets.SND_RAY,
                Assets.SND_BEACON,
                Assets.SND_TELEPORT,
                Assets.SND_CHARMS,
                Assets.SND_MASTERY,
                Assets.SND_PUFF,
                Assets.SND_ROCKS,
                Assets.SND_BURNING,
                Assets.SND_FALLING,
                Assets.SND_GHOST,
                Assets.SND_SECRET,
                Assets.SND_BONES,
                Assets.SND_BEE,
                Assets.SND_DEGRADE,
                Assets.SND_MIMIC );
    }

    @Override
    public void onWindowFocusChanged ( boolean hasFocus ) {

        super.onWindowFocusChanged( hasFocus );

        if ( hasFocus ) {
            updateImmersiveMode();
        }
    }

    /*
     * <--- Preferences
     */

    @Override
    public void onSurfaceChanged ( GL10 gl, int width, int height ) {
        super.onSurfaceChanged( gl, width, height );

        if ( isImmersiveModeChanged() ) {
            setRequestedReset( true );
            setImmersiveModeChanged( false );
        }
    }
}
