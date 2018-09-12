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

import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Amok;
import com.demasu.testpixeldungeon.actors.buffs.Light;
import com.demasu.testpixeldungeon.actors.buffs.Rage;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.hero.HeroClass;
import com.demasu.testpixeldungeon.actors.hero.Legend;
import com.demasu.testpixeldungeon.actors.mobs.ColdGirl;
import com.demasu.testpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.demasu.testpixeldungeon.actors.mobs.npcs.Ghost;
import com.demasu.testpixeldungeon.actors.mobs.npcs.Imp;
import com.demasu.testpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.demasu.testpixeldungeon.actors.skills.CurrentSkills;
import com.demasu.testpixeldungeon.items.Ankh;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.items.rings.Ring;
import com.demasu.testpixeldungeon.items.scrolls.Scroll;
import com.demasu.testpixeldungeon.items.wands.Wand;
import com.demasu.testpixeldungeon.levels.CavesBossLevel;
import com.demasu.testpixeldungeon.levels.CavesLevel;
import com.demasu.testpixeldungeon.levels.CityBossLevel;
import com.demasu.testpixeldungeon.levels.CityLevel;
import com.demasu.testpixeldungeon.levels.DeadEndLevel;
import com.demasu.testpixeldungeon.levels.FrostLevel;
import com.demasu.testpixeldungeon.levels.HallsBossLevel;
import com.demasu.testpixeldungeon.levels.HallsLevel;
import com.demasu.testpixeldungeon.levels.LastLevel;
import com.demasu.testpixeldungeon.levels.LastShopLevel;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.PrisonBossLevel;
import com.demasu.testpixeldungeon.levels.PrisonLevel;
import com.demasu.testpixeldungeon.levels.Room;
import com.demasu.testpixeldungeon.levels.SewerBossLevel;
import com.demasu.testpixeldungeon.levels.SewerLevel;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.StartScene;
import com.demasu.testpixeldungeon.ui.QuickSlot;
import com.demasu.testpixeldungeon.utils.BArray;
import com.demasu.testpixeldungeon.utils.Utils;
import com.demasu.testpixeldungeon.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class Dungeon {

    private static final String RG_GAME_FILE = "game.dat";
    private static final String RG_DEPTH_FILE = "depth%d.dat";
    private static final String WR_GAME_FILE = "warrior.dat";
    private static final String WR_DEPTH_FILE = "warrior%d.dat";
    private static final String MG_GAME_FILE = "mage.dat";
    private static final String MG_DEPTH_FILE = "mage%d.dat";
    private static final String RN_GAME_FILE = "ranger.dat";
    private static final String RN_DEPTH_FILE = "ranger%d.dat";
    private static final String VERSION = "version";
    private static final String CHALLENGES = "challenges";
    private static final String HERO = "hero";
    private static final String GOLD = "gold";
    private static final String DEPTH = "depth";
    private static final String LEVEL = "level";
    private static final String DROPPED = "dropped%d";
    private static final String POS = "potionsOfStrength";
    private static final String SOU = "scrollsOfEnhancement";
    private static final String SOE = "scrollsOfEnchantment";
    private static final String DV = "dewVial";
    private static final String CHAPTERS = "chapters";
    private static final String QUESTS = "quests";
    private static final String BADGES = "badges";
    public static boolean[] passable = new boolean[Level.LENGTH];
    private static int potionOfStrength;
    private static int scrollsOfUpgrade;
    private static int scrollsOfEnchantment;
    private static boolean dewVial;        // true if the dew vial can be spawned
    private static int challenges;
    private static Hero hero;
    private static Level level;
    private static int depth;
    private static int gold;
    private static int difficulty;
    private static Difficulties currentDifficulty;
    // Reason of death
    private static String resultDescription;
    private static HashSet<Integer> chapters;
    // Hero's field of view
    private static boolean[] visible = new boolean[Level.LENGTH];
    private static boolean nightMode;
    private static SparseArray<ArrayList<Item>> droppedItems;

    public static void init () {

        setChallenges( PixelDungeon.challenges() );

        Actor.clear();

        PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );

        Scroll.initLabels();
        Potion.initColors();
        Wand.initWoods();
        Ring.initGems();

        Statistics.reset();
        Journal.reset();

        setDepth( 0 );
        setGold( 0 );

        setDroppedItems( new SparseArray<ArrayList<Item>>() );

        setPotionOfStrength( 0 );
        setScrollsOfUpgrade( 0 );
        setScrollsOfEnchantment( 0 );
        setDewVial( true );

        setChapters( new HashSet<Integer>() );

        Ghost.Quest.reset();
        Wandmaker.Quest.reset();
        Blacksmith.Quest.reset();
        Imp.Quest.reset();

        Room.shuffleTypes();

        QuickSlot.setPrimaryValue( null );
        QuickSlot.setSecondaryValue( null );

        setHero( new Hero() );
        getHero().difficulty = getDifficulty();
        getHero().live();

        Badges.reset();

        StartScene.curClass.initHero( getHero() );

    }

    public static void initStartingStats () {
        final int GOLD = 10_000;
        setBeginningHealth();
        getCurrentDifficulty().difficultyStartItemBonus();
        setGold( Dungeon.getGold() + GOLD ); // For debug
    }

    public static int getGold () {
        return gold;
    }

    public static void setGold ( int gold ) {
        Dungeon.gold = gold;
    }

    public static int getDepth () {
        return depth;
    }

    public static void setDepth ( int depth ) {
        Dungeon.depth = depth;
    }

    public static void initLegend () {

        setChallenges( PixelDungeon.challenges() );

        Actor.clear();

        PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );

        Scroll.initLabels();
        Potion.initColors();
        Wand.initWoods();
        Ring.initGems();

        Statistics.reset();
        Journal.reset();

        setDepth( 0 );
        setGold( 0 );

        setDroppedItems( new SparseArray<ArrayList<Item>>() );

        setPotionOfStrength( 0 );
        setScrollsOfUpgrade( 0 );
        setScrollsOfEnchantment( 0 );
        setDewVial( true );

        setChapters( new HashSet<Integer>() );

        Ghost.Quest.reset();
        Wandmaker.Quest.reset();
        Blacksmith.Quest.reset();
        Imp.Quest.reset();

        Room.shuffleTypes();

        QuickSlot.setPrimaryValue( null );
        QuickSlot.setSecondaryValue( null );

        setHero( new Legend() );
        getHero().difficulty = getDifficulty();
        getHero().live();

        Badges.reset();

        StartScene.curClass = HeroClass.HATSUNE;
        StartScene.curClass.initHero( getHero() );
        getHero().heroSkills = CurrentSkills.HATSUNE;
        getHero().heroSkills.init();

    }

    public static boolean isChallenged ( int mask ) {
        return ( getChallenges() & mask ) != 0;
    }

    public static Level newLevel () {

        Dungeon.setLevel( null );
        Actor.clear();

        setDepth( getDepth() + 1 );
        if ( getDepth() % ColdGirl.FROST_DEPTH > Statistics.deepestFloor ) {
            Statistics.deepestFloor = getDepth();

            Statistics.completedWithNoKilling = Statistics.qualifiedForNoKilling;
        }

        Arrays.fill( getVisible(), false );

        Level level;
        switch ( getDepth() ) {
            case 1:
            case 2:
            case 3:
            case 4:
                level = new SewerLevel();
                break;
            case 5:
                level = new SewerBossLevel();
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                level = new PrisonLevel();
                break;
            case 10:
                level = new PrisonBossLevel();
                break;
            case 11:
            case 12:
            case 13:
            case 14:
                level = new CavesLevel();
                break;
            case 15:
                level = new CavesBossLevel();
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                level = new CityLevel();
                break;
            case 20:
                level = new CityBossLevel();
                break;
            case 21:
                level = new LastShopLevel();
                break;
            case 22:
            case 23:
            case 24:
                level = new HallsLevel();
                break;
            case 25:
                level = new HallsBossLevel();
                break;
            case 26:
                level = new LastLevel();
                break;
            case ColdGirl.FROST_DEPTH:
                level = new FrostLevel();
                break;
            default:
                level = new DeadEndLevel();
                Statistics.deepestFloor--;
        }

        level.create();

        Statistics.qualifiedForNoKilling = !bossLevel();

        return level;
    }

    public static void resetLevel () {

        Actor.clear();

        Arrays.fill( getVisible(), false );

        getLevel().reset();
        switchLevel( getLevel(), getLevel().entrance );
    }

    public static boolean shopOnLevel () {
        return getDepth() == 1 || getDepth() == 6 || getDepth() == 11 || getDepth() == 16;
    }

    public static boolean bossLevel () {
        return bossLevel( getDepth() );
    }

    public static boolean bossLevel ( int depth ) {
        return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25 || depth == ColdGirl.FROST_DEPTH || depth == 0;
    }

    @SuppressWarnings ( "deprecation" )
    public static void switchLevel ( final Level level, int pos ) {

        setNightMode( new Date().getHours() < 7 );

        try {
            if ( Dungeon.getCurrentDifficulty().isNight == Difficulties.isNightOverwrite.ALWAYS_NIGHT ) {
                setNightMode( true );
            }
            if ( Dungeon.getCurrentDifficulty().isNight == Difficulties.isNightOverwrite.ALWAYS_DAY ) {
                setNightMode( false );
            }
        } catch ( Exception e ) {

        }
        Dungeon.setLevel( level );
        Actor.init();


        Actor respawner = level.respawner();
        if ( respawner != null ) {
            Actor.add( level.respawner() );
        }

        if ( getHero().hiredMerc != null ) {
            getHero().checkMerc = true;
        }
        if ( getDepth() != ColdGirl.FROST_DEPTH && getDepth() != 0 ) {
            Actor mercRespawn = level.mercRespawner();
            if ( mercRespawn != null ) {
                Actor.add( mercRespawn );
            }
        }
        getHero().pos = pos != -1 ? pos : level.exit;

        Light light = getHero().buff( Light.class );
        getHero().viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );

        observe();
    }

    public static void dropToChasm ( Item item ) {
        int depth = Dungeon.getDepth() + 1;
        ArrayList<Item> dropped = (ArrayList<Item>) Dungeon.getDroppedItems().get( depth );
        if ( dropped == null ) {
            Dungeon.getDroppedItems().put( depth, dropped = new ArrayList<Item>() );
        }
        dropped.add( item );
    }

    public static boolean posNeeded () {
        int[] quota = { 16, 8, 36, 16, 56, 24, 76, 32, 96, 36 };
        return chance( quota, getPotionOfStrength() );
    }

    public static boolean souNeeded () {
        int[] quota = { 20, 12, 40, 24, 60, 36, 80, 48, 100, 52 };
        return chance( quota, getScrollsOfUpgrade() );
    }

    public static boolean soeNeeded () {
        return Random.Int( 12 * ( 1 + getScrollsOfEnchantment() ) ) < getDepth();
    }

    private static boolean chance ( int[] quota, int number ) {

        for ( int i = 0; i < quota.length; i += 2 ) {
            int qDepth = quota[i];
            if ( getDepth() <= qDepth ) {
                int qNumber = quota[i + 1];
                return Random.Float() < (float) ( qNumber - number ) / ( qDepth - getDepth() + 1 );
            }
        }

        return false;
    }

    public static String gameFile ( HeroClass cl ) {
        switch ( cl ) {
            case WARRIOR:
                return WR_GAME_FILE;
            case MAGE:
                return MG_GAME_FILE;
            case HUNTRESS:
                return RN_GAME_FILE;
            default:
                return RG_GAME_FILE;
        }
    }

    private static String depthFile ( HeroClass cl ) {
        switch ( cl ) {
            case WARRIOR:
                return WR_DEPTH_FILE;
            case MAGE:
                return MG_DEPTH_FILE;
            case HUNTRESS:
                return RN_DEPTH_FILE;
            default:
                return RG_DEPTH_FILE;
        }
    }

    public static void saveGame ( String fileName ) throws IOException {
        try {
            Bundle bundle = new Bundle();

            bundle.put( VERSION, Game.version );
            bundle.put( CHALLENGES, getChallenges() );
            bundle.put( HERO, getHero() );
            bundle.put( GOLD, getGold() );
            bundle.put( DEPTH, getDepth() );

            for ( int d : getDroppedItems().keyArray() ) {
                bundle.put( String.format( Locale.US, DROPPED, d ), getDroppedItems().get( d ) );
            }

            bundle.put( POS, getPotionOfStrength() );
            bundle.put( SOU, getScrollsOfUpgrade() );
            bundle.put( SOE, getScrollsOfEnchantment() );
            bundle.put( DV, isDewVial() );

            int count = 0;
            int ids[] = new int[getChapters().size()];
            for ( Integer id : getChapters() ) {
                ids[count++] = id;
            }
            bundle.put( CHAPTERS, ids );

            Bundle quests = new Bundle();
            Ghost.Quest.storeInBundle( quests );
            Wandmaker.Quest.storeInBundle( quests );
            Blacksmith.Quest.storeInBundle( quests );
            Imp.Quest.storeInBundle( quests );
            bundle.put( QUESTS, quests );

            Room.storeRoomsInBundle( bundle );

            Statistics.storeInBundle( bundle );
            Journal.storeInBundle( bundle );

            QuickSlot.save( bundle );

            Scroll.save( bundle );
            Potion.save( bundle );
            Wand.save( bundle );
            Ring.save( bundle );

            Bundle badges = new Bundle();
            Badges.saveLocal( badges );
            bundle.put( BADGES, badges );

            OutputStream output = Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE );
            Bundle.write( bundle, output );
            output.close();

        } catch ( Exception e ) {

            GamesInProgress.setUnknown( getHero().getHeroClass() );
        }
    }

    public static void saveLevel () throws IOException {
        Bundle bundle = new Bundle();
        bundle.put( LEVEL, getLevel() );

        OutputStream output = Game.instance.openFileOutput( Utils.format( depthFile( getHero().getHeroClass() ), getDepth() ), Game.MODE_PRIVATE );
        Bundle.write( bundle, output );
        output.close();
    }

    public static void saveAll () throws IOException {
        if ( getHero().isAlive() ) {

            Actor.fixTime();
            saveGame( gameFile( getHero().getHeroClass() ) );
            saveLevel();

            GamesInProgress.set( getHero().getHeroClass(), getDepth(), getHero().lvl, getChallenges() != 0 );

        } else if ( WndResurrect.instance != null ) {

            WndResurrect.instance.hide();
            Hero.reallyDie( WndResurrect.causeOfDeath );

        }
    }

    public static void loadGame ( HeroClass cl ) throws IOException {
        loadGame( gameFile( cl ), true );
    }

    public static void loadGame ( String fileName ) throws IOException {
        loadGame( fileName, false );
    }

    public static void loadGame ( String fileName, boolean fullLoad ) throws IOException {

        Bundle bundle = gameBundle( fileName );

        Dungeon.setChallenges( bundle.getInt( CHALLENGES ) );

        Dungeon.setLevel( null );
        Dungeon.setDepth( -1 );

        if ( fullLoad ) {
            PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
        }

        Scroll.restore( bundle );
        Potion.restore( bundle );
        Wand.restore( bundle );
        Ring.restore( bundle );

        setPotionOfStrength( bundle.getInt( POS ) );
        setScrollsOfUpgrade( bundle.getInt( SOU ) );
        setScrollsOfEnchantment( bundle.getInt( SOE ) );
        setDewVial( bundle.getBoolean( DV ) );

        if ( fullLoad ) {
            setChapters( new HashSet<Integer>() );
            int ids[] = bundle.getIntArray( CHAPTERS );
            if ( ids != null ) {
                for ( int id : ids ) {
                    getChapters().add( id );
                }
            }

            Bundle quests = bundle.getBundle( QUESTS );
            if ( !quests.isNull() ) {
                Ghost.Quest.restoreFromBundle( quests );
                Wandmaker.Quest.restoreFromBundle( quests );
                Blacksmith.Quest.restoreFromBundle( quests );
                Imp.Quest.restoreFromBundle( quests );
            } else {
                Ghost.Quest.reset();
                Wandmaker.Quest.reset();
                Blacksmith.Quest.reset();
                Imp.Quest.reset();
            }

            Room.restoreRoomsFromBundle( bundle );
        }

        Bundle badges = bundle.getBundle( BADGES );
        if ( !badges.isNull() ) {
            Badges.loadLocal( badges );
        } else {
            Badges.reset();
        }

        QuickSlot.restore( bundle );

        @SuppressWarnings ( "unused" )
        String version = bundle.getString( VERSION );

        setHero( null );
        setHero( (Hero) bundle.get( HERO ) );
        QuickSlot.compress();

        setGold( bundle.getInt( GOLD ) );
        setDepth( bundle.getInt( DEPTH ) );

        Statistics.restoreFromBundle( bundle );
        Journal.restoreFromBundle( bundle );

        setDroppedItems( new SparseArray<ArrayList<Item>>() );
        for ( int i = 2; i <= Statistics.deepestFloor + 1; i++ ) {
            ArrayList<Item> dropped = new ArrayList<Item>();
            for ( Bundlable b : bundle.getCollection( String.format( Locale.US, DROPPED, i ) ) ) {
                dropped.add( (Item) b );
            }
            if ( !dropped.isEmpty() ) {
                getDroppedItems().put( i, dropped );
            }
        }
    }

    public static Level loadLevel ( HeroClass cl ) throws IOException {

        Dungeon.setLevel( null );
        Actor.clear();

        InputStream input = Game.instance.openFileInput( Utils.format( depthFile( cl ), getDepth() ) );
        Bundle bundle = Bundle.read( input );
        input.close();

        return (Level) bundle.get( "level" );
    }

    public static void deleteGame ( HeroClass cl, boolean deleteLevels ) {

        Game.instance.deleteFile( gameFile( cl ) );

        if ( deleteLevels ) {
            int depth = 1;
            while ( Game.instance.deleteFile( Utils.format( depthFile( cl ), depth ) ) ) {
                depth++;
            }
        }

        GamesInProgress.delete( cl );
    }

    public static Bundle gameBundle ( String fileName ) throws IOException {

        InputStream input = Game.instance.openFileInput( fileName );
        Bundle bundle = Bundle.read( input );
        input.close();

        return bundle;
    }

    public static void preview ( GamesInProgress.Info info, Bundle bundle ) {
        info.depth = bundle.getInt( DEPTH );
        info.challenges = ( bundle.getInt( CHALLENGES ) != 0 );
        if ( info.depth == -1 ) {
            info.depth = bundle.getInt( "maxDepth" );    // FIXME
        }
        Hero.preview( info, bundle.getBundle( HERO ) );
    }

    public static void fail ( String desc ) {
        setResultDescription( desc );
        if ( getHero().belongings.getItem( Ankh.class ) == null || Dungeon.getDepth() == ColdGirl.FROST_DEPTH ) {
            if ( Dungeon.getDepth() == ColdGirl.FROST_DEPTH ) {
                setResultDescription( ColdGirl.TXT_DEATH );
            }

            Rankings.INSTANCE.submit( false );
        }
    }

    public static void win ( String desc ) {

        getHero().belongings.identify();

        if ( getChallenges() != 0 ) {
            Badges.validateChampion();
        }

        setResultDescription( desc );
        Rankings.INSTANCE.submit( true );
    }

    public static void observe () {

        if ( getLevel() == null ) {
            return;
        }

        getLevel().updateFieldOfView( getHero() );
        System.arraycopy( Level.fieldOfView, 0, getVisible(), 0, getVisible().length );

        BArray.or( getLevel().visited, getVisible(), getLevel().visited );

        GameScene.afterObserve();
    }

    public static int findPath ( Char ch, int from, int to, boolean pass[], boolean[] visible ) {

        if ( Level.adjacent( from, to ) ) {
            return Actor.findChar( to ) == null && ( pass[to] || Level.avoid[to] ) ? to : -1;
        }

        if ( ch.flying || ch.buff( Amok.class ) != null || ch.buff( Rage.class ) != null ) {
            BArray.or( pass, Level.avoid, passable );
        } else {
            System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
        }

        for ( Actor actor : Actor.all() ) {
            if ( actor instanceof Char ) {
                int pos = ( (Char) actor ).pos;
                if ( visible[pos] ) {
                    passable[pos] = false;
                }
            }
        }

        return PathFinder.getStep( from, to, passable );

    }

    public static int flee ( Char ch, int cur, int from, boolean pass[], boolean[] visible ) {

        if ( ch.flying ) {
            BArray.or( pass, Level.avoid, passable );
        } else {
            System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
        }

        for ( Actor actor : Actor.all() ) {
            if ( actor instanceof Char ) {
                int pos = ( (Char) actor ).pos;
                if ( visible[pos] ) {
                    passable[pos] = false;
                }
            }
        }
        passable[cur] = true;

        return PathFinder.getStepBack( cur, from, passable );

    }

    public static int getPotionOfStrength () {
        return potionOfStrength;
    }

    public static void setPotionOfStrength ( int potionOfStrength ) {
        Dungeon.potionOfStrength = potionOfStrength;
    }

    public static int getScrollsOfUpgrade () {
        return scrollsOfUpgrade;
    }

    public static void setScrollsOfUpgrade ( int scrollsOfUpgrade ) {
        Dungeon.scrollsOfUpgrade = scrollsOfUpgrade;
    }

    public static int getScrollsOfEnchantment () {
        return scrollsOfEnchantment;
    }

    public static void setScrollsOfEnchantment ( int scrollsOfEnchantment ) {
        Dungeon.scrollsOfEnchantment = scrollsOfEnchantment;
    }

    public static boolean isDewVial () {
        return dewVial;
    }

    public static void setDewVial ( boolean dewVial ) {
        Dungeon.dewVial = dewVial;
    }

    public static int getChallenges () {
        return challenges;
    }

    public static void setChallenges ( int challenges ) {
        Dungeon.challenges = challenges;
    }

    public static Hero getHero () {
        return hero;
    }

    public static void setHero ( Hero hero ) {
        Dungeon.hero = hero;
    }

    public static Level getLevel () {
        return level;
    }

    public static void setLevel ( Level level ) {
        Dungeon.level = level;
    }

    public static int getDifficulty () {
        return difficulty;
    }

    public static void setDifficulty ( int difficulty ) {
        Dungeon.difficulty = difficulty;
    }

    public static Difficulties getCurrentDifficulty () {
        return currentDifficulty;
    }

    public static void setCurrentDifficulty ( Difficulties currentDifficulty ) {
        Dungeon.currentDifficulty = currentDifficulty;
    }

    public static String getResultDescription () {
        return resultDescription;
    }

    public static void setResultDescription ( String resultDescription ) {
        Dungeon.resultDescription = resultDescription;
    }

    public static HashSet<Integer> getChapters () {
        return chapters;
    }

    public static void setChapters ( HashSet<Integer> chapters ) {
        Dungeon.chapters = chapters;
    }

    public static boolean[] getVisible () {
        return visible;
    }

    public static void setVisible ( boolean[] visible ) {
        Dungeon.visible = visible;
    }

    public static boolean isNightMode () {
        return nightMode;
    }

    public static void setNightMode ( boolean nightMode ) {
        Dungeon.nightMode = nightMode;
    }

    public static SparseArray<ArrayList<Item>> getDroppedItems () {
        return droppedItems;
    }

    public static void setDroppedItems ( SparseArray<ArrayList<Item>> droppedItems ) {
        Dungeon.droppedItems = droppedItems;
    }

    public static void setBeginningHealth () {
        Hero hero = getHero();
        Difficulties diff = getCurrentDifficulty();
        int bonus         = diff.difficultyHPStartPenalty();
        hero.setStartingHealth( bonus );
    }
}
