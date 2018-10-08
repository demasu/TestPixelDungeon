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
package com.demasu.testpixeldungeon.scenes;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.DungeonTilemap;
import com.demasu.testpixeldungeon.FogOfWar;
import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.Statistics;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.blobs.Blob;
import com.demasu.testpixeldungeon.actors.mobs.ColdGirl;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.effects.BannerSprites;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.effects.EmoIcon;
import com.demasu.testpixeldungeon.effects.Flare;
import com.demasu.testpixeldungeon.effects.FloatingText;
import com.demasu.testpixeldungeon.effects.Ripple;
import com.demasu.testpixeldungeon.effects.SpellSprite;
import com.demasu.testpixeldungeon.items.Heap;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.items.wands.WandOfBlink;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.MovieLevel;
import com.demasu.testpixeldungeon.levels.RegularLevel;
import com.demasu.testpixeldungeon.levels.features.Chasm;
import com.demasu.testpixeldungeon.plants.Plant;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.sprites.DiscardedItemSprite;
import com.demasu.testpixeldungeon.sprites.HeroSprite;
import com.demasu.testpixeldungeon.sprites.ItemSprite;
import com.demasu.testpixeldungeon.sprites.PlantSprite;
import com.demasu.testpixeldungeon.ui.AttackIndicator;
import com.demasu.testpixeldungeon.ui.Banner;
import com.demasu.testpixeldungeon.ui.BusyIndicator;
import com.demasu.testpixeldungeon.ui.GameLog;
import com.demasu.testpixeldungeon.ui.HealthIndicator;
import com.demasu.testpixeldungeon.ui.QuickSlot;
import com.demasu.testpixeldungeon.ui.StatusPane;
import com.demasu.testpixeldungeon.ui.Toast;
import com.demasu.testpixeldungeon.ui.Toolbar;
import com.demasu.testpixeldungeon.ui.Window;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.windows.WndBag;
import com.demasu.testpixeldungeon.windows.WndBag.Mode;
import com.demasu.testpixeldungeon.windows.WndGame;
import com.demasu.testpixeldungeon.windows.WndStory;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;

public class GameScene extends PixelScene {

    protected static final String TXT_WELCOME = "Welcome to the level %d of Pixel Dungeon!";
    protected static final String TXT_WELCOME_BACK = "Welcome back to the level %d of Pixel Dungeon!";
    protected static final String TXT_NIGHT_MODE = "Be cautious, since the dungeon is even more dangerous at night!";

    protected static final String TXT_CHASM = "Your steps echo across the dungeon.";
    protected static final String TXT_WATER = "You hear the water splashing around you.";
    protected static final String TXT_GRASS = "The smell of vegetation is thick in the air.";
    protected static final String TXT_SECRETS = "The atmosphere hints that this floor hides many secrets.";

    protected static final String TXT_WARN_DEGRADATION = "Your items will wear down with time. You can disable item degradation from settings.";

    protected static final String TXT_FROST = "The portal spits you out in a cold domain...";
    private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
        @Override
        public void onSelect ( Integer cell ) {
            if ( Dungeon.getHero().handle( cell ) ) {
                Dungeon.getHero().next();
            }
        }

        @Override
        public String prompt () {
            return null;
        }
    };
    protected static GameScene scene;
    protected static CellSelector cellSelector;
    protected SkinnedBlock water;
    protected DungeonTilemap tiles;
    protected FogOfWar fog;
    protected HeroSprite hero;
    protected GameLog log;
    protected BusyIndicator busy;
    protected Group terrain;
    protected Group ripples;
    protected Group plants;
    protected Group heaps;
    protected Group mobs;
    protected Group emitters;
    protected Group effects;
    protected Group gases;
    protected Group spells;
    protected Group statuses;
    protected Group emoicons;
    protected Toolbar toolbar;
    protected Toast prompt;

    public static void add ( Plant plant ) {
        if ( scene != null ) {
            scene.addPlantSprite( plant );
        }
    }

    public static void add ( Blob gas ) {
        Actor.add( gas );
        if ( scene != null ) {
            scene.addBlobSprite( gas );
        }
    }

    public static void add ( Heap heap ) {
        if ( scene != null ) {
            scene.addHeapSprite( heap );
        }
    }

    public static void discard ( Heap heap ) {
        if ( scene != null ) {
            scene.addDiscardedSprite( heap );
        }
    }

    public static void add ( Mob mob ) {
        Dungeon.getLevel().mobs.add( mob );
        Actor.add( mob );
        Actor.occupyCell( mob );
        scene.addMobSprite( mob );
    }

    public static void add ( Mob mob, float delay ) {
        Dungeon.getLevel().mobs.add( mob );
        Actor.addDelayed( mob, delay );
        Actor.occupyCell( mob );
        scene.addMobSprite( mob );
    }

    public static void add ( EmoIcon icon ) {
        scene.emoicons.add( icon );
    }

    public static void effect ( Visual effect ) {
        scene.effects.add( effect );
    }

    public static Ripple ripple ( int pos ) {
        Ripple ripple = (Ripple) scene.ripples.recycle( Ripple.class );
        ripple.reset( pos );
        return ripple;
    }

    public static SpellSprite spellSprite () {
        return (SpellSprite) scene.spells.recycle( SpellSprite.class );
    }

    public static Emitter emitter () {
        if ( scene != null ) {
            Emitter emitter = (Emitter) scene.emitters.recycle( Emitter.class );
            emitter.revive();
            return emitter;
        } else {
            return null;
        }
    }

    public static FloatingText status () {
        return scene != null ? (FloatingText) scene.statuses.recycle( FloatingText.class ) : null;
    }

    public static void pickUp ( Item item ) {
        scene.toolbar.pickup( item );
    }

    public static void updateMap () {
        if ( scene != null ) {
            scene.tiles.getUpdated().set( 0, 0, Level.WIDTH, Level.HEIGHT );
        }
    }

    // -------------------------------------------------------

    public static void updateMap ( int cell ) {
        if ( scene != null ) {
            scene.tiles.getUpdated().union( cell % Level.WIDTH, cell / Level.WIDTH );
        }
    }

    public static void discoverTile ( int pos, int oldValue ) {
        if ( scene != null ) {
            scene.tiles.discover( pos, oldValue );
        }
    }

    public static void show ( Window wnd ) {
        cancelCellSelector();
        scene.add( wnd );
    }

    public static void afterObserve () {
        if ( scene != null ) {
            scene.fog.updateVisibility( Dungeon.getVisible(), Dungeon.getLevel().visited, Dungeon.getLevel().mapped );

            for ( Mob mob : Dungeon.getLevel().mobs ) {
                mob.sprite.setVisible( Dungeon.getVisible()[mob.pos] );
            }
        }
    }

    public static void flash ( int color ) {
        scene.fadeIn( 0xFF000000 | color, true );
    }

    public static void gameOver () {
        Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
        gameOver.show( 0x000000, 1f );
        scene.showBanner( gameOver );

        Sample.INSTANCE.play( Assets.SND_DEATH );
    }

    public static void bossSlain () {
        if ( Dungeon.getHero().isAlive() ) {
            Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
            bossSlain.show( 0xFFFFFF, 0.3f, 5f );
            scene.showBanner( bossSlain );

            Sample.INSTANCE.play( Assets.SND_BOSS );
        }
    }

    public static void handleCell ( int cell ) {
        cellSelector.select( cell );
    }

    public static void selectCell ( CellSelector.Listener listener ) {
        cellSelector.listener = listener;
        scene.prompt( listener.prompt() );
    }

    private static boolean cancelCellSelector () {
        if ( cellSelector.listener != null && cellSelector.listener != defaultCellListener ) {
            cellSelector.cancel();
            return true;
        } else {
            return false;
        }
    }

    public static WndBag selectItem ( WndBag.Listener listener, WndBag.Mode mode, String title ) {
        cancelCellSelector();

        WndBag wnd = mode == Mode.SEED ?
                WndBag.seedPouch( listener, mode, title ) :
                WndBag.lastBag( listener, mode, title );
        scene.add( wnd );

        return wnd;
    }

    static boolean cancel () {
        if ( Dungeon.getHero().curAction != null || Dungeon.getHero().restoreHealth ) {

            Dungeon.getHero().curAction = null;
            Dungeon.getHero().restoreHealth = false;
            return true;

        } else {

            return cancelCellSelector();

        }
    }

    public static void ready () {
        selectCell( defaultCellListener );
        QuickSlot.cancel();
    }

    public void originalCreate () {
        super.create();
    }

    @Override
    public void create () {
        if ( Dungeon.getDepth() != 0 && Dungeon.getDepth() != ColdGirl.FROST_DEPTH ) {
            Music.INSTANCE.play( Assets.TUNE, true );
            Music.INSTANCE.volume( 1f );
        } else {
            Music.INSTANCE.play( Assets.TUNE_SPECIAL, true );
            Music.INSTANCE.volume( 1f );
        }

        PixelDungeon.lastClass( Dungeon.getHero().getHeroClass().ordinal() );

        super.create();
        Camera.getMain().zoom( defaultZoom + PixelDungeon.zoom() );

        scene = this;

        terrain = new Group();
        add( terrain );

        water = new SkinnedBlock(
                Level.WIDTH * DungeonTilemap.SIZE,
                Level.HEIGHT * DungeonTilemap.SIZE,
                Dungeon.getLevel().waterTex() );
        terrain.add( water );

        ripples = new Group();
        terrain.add( ripples );

        tiles = new DungeonTilemap();
        terrain.add( tiles );

        Dungeon.getLevel().addVisuals( this );

        plants = new Group();
        add( plants );

        int size = Dungeon.getLevel().plants.size();
        for ( int i = 0; i < size; i++ ) {
            addPlantSprite( Dungeon.getLevel().plants.valueAt( i ) );
        }

        heaps = new Group();
        add( heaps );

        size = Dungeon.getLevel().heaps.size();
        for ( int i = 0; i < size; i++ ) {
            addHeapSprite( Dungeon.getLevel().heaps.valueAt( i ) );
        }

        emitters = new Group();
        effects = new Group();
        emoicons = new Group();

        mobs = new Group();
        add( mobs );

        for ( Mob mob : Dungeon.getLevel().mobs ) {
            addMobSprite( mob );
            if ( Statistics.amuletObtained ) {
                mob.beckon( Dungeon.getHero().pos );
            }
        }

        add( emitters );
        add( effects );

        gases = new Group();
        add( gases );

        for ( Blob blob : Dungeon.getLevel().blobs.values() ) {
            blob.emitter = null;
            addBlobSprite( blob );
        }

        fog = new FogOfWar( Level.WIDTH, Level.HEIGHT );
        fog.updateVisibility( Dungeon.getVisible(), Dungeon.getLevel().visited, Dungeon.getLevel().mapped );
        add( fog );

        brightness( PixelDungeon.brightness() );


        spells = new Group();
        add( spells );

        statuses = new Group();
        add( statuses );

        add( emoicons );

        hero = new HeroSprite();
        hero.place( Dungeon.getHero().pos );
        hero.updateArmor();
        mobs.add( hero );

        add( new HealthIndicator() );

        add( cellSelector = new CellSelector( tiles ) );

        StatusPane sb = new StatusPane();
        sb.setCamera( uiCamera );
        sb.setSize( uiCamera.getWidth(), 0 );
        add( sb );

        toolbar = new Toolbar();
        toolbar.setCamera( uiCamera );
        toolbar.setRect( 0, uiCamera.getHeight() - toolbar.height(), uiCamera.getWidth(), toolbar.height() );
        add( toolbar );

        AttackIndicator attack = new AttackIndicator();
        attack.setCamera( uiCamera );
        attack.setPos(
                uiCamera.getWidth() - attack.width(),
                toolbar.top() - attack.height() );
        add( attack );

        log = new GameLog();
        log.setCamera( uiCamera );
        log.setRect( 0, toolbar.top(), attack.left(), 0 );
        add( log );

        busy = new BusyIndicator();
        busy.setCamera( uiCamera );
        busy.setX( 1 );
        busy.setY( sb.bottom() + 1 );
        add( busy );

        switch ( InterlevelScene.mode ) {
            case RESURRECT:
                WandOfBlink.appear( Dungeon.getHero(), Dungeon.getLevel().entrance );
                new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f );
                break;
            case RETURN:
                WandOfBlink.appear( Dungeon.getHero(), Dungeon.getHero().pos );
                break;
            case FALL:
                Chasm.heroLand();
                break;
            case DESCEND:
                switch ( Dungeon.getDepth() ) {
                    case 1:
                        WndStory.showChapter( WndStory.ID_SEWERS );
                        if ( !PixelDungeon.itemDeg() ) {
                            WndStory.showStory( TXT_WARN_DEGRADATION );
                        }
                        break;
                    case 6:
                        WndStory.showChapter( WndStory.ID_PRISON );
                        break;
                    case 11:
                        WndStory.showChapter( WndStory.ID_CAVES );
                        break;
                    case 16:
                        WndStory.showChapter( WndStory.ID_METROPOLIS );
                        break;
                    case 22:
                        WndStory.showChapter( WndStory.ID_HALLS );
                        break;
                }
                if ( Dungeon.getHero().isAlive() && Dungeon.getDepth() != 22 ) {
                    Badges.validateNoKilling();
                }
                break;
            default:
        }

        ArrayList<Item> dropped = Dungeon.getDroppedItems().get( Dungeon.getDepth() );
        if ( dropped != null ) {
            for ( Item item : dropped ) {
                int pos = Dungeon.getLevel().randomRespawnCell();
                if ( item instanceof Potion ) {
                    ( (Potion) item ).shatter( pos );
                } else if ( item instanceof Plant.Seed ) {
                    Dungeon.getLevel().plant( (Plant.Seed) item, pos );
                } else {
                    Dungeon.getLevel().drop( item, pos );
                }
            }
            Dungeon.getDroppedItems().remove( Dungeon.getDepth() );
        }

        Camera.getMain().setTarget( hero );

        if ( InterlevelScene.mode != InterlevelScene.Mode.NONE && Dungeon.getDepth() != 0 ) {
            if ( Dungeon.getDepth() < Statistics.deepestFloor ) {
                GLog.h( TXT_WELCOME_BACK, Dungeon.getDepth() );
            } else {
                if ( Dungeon.getDepth() != ColdGirl.FROST_DEPTH ) {
                    GLog.h( TXT_WELCOME, Dungeon.getDepth() );
                    Sample.INSTANCE.play( Assets.SND_DESCEND );
                } else {
                    GLog.h( TXT_FROST );
                    Sample.INSTANCE.play( Assets.SND_TELEPORT );
                }
            }
            switch ( Dungeon.getLevel().feeling ) {
                case CHASM:
                    GLog.w( TXT_CHASM );
                    break;
                case WATER:
                    GLog.w( TXT_WATER );
                    break;
                case GRASS:
                    GLog.w( TXT_GRASS );
                    break;
                default:
            }
            if ( Dungeon.getLevel() instanceof RegularLevel &&
                    ( (RegularLevel) Dungeon.getLevel() ).secretDoors > Random.IntRange( 3, 4 ) ) {
                GLog.w( TXT_SECRETS );
            }
            if ( Dungeon.isNightMode() && !Dungeon.bossLevel() ) {
                GLog.w( TXT_NIGHT_MODE );
            }

            InterlevelScene.mode = InterlevelScene.Mode.NONE;

            fadeIn();
        }
    }

    public void destroy () {

        scene = null;
        Badges.saveGlobal();

        super.destroy();
    }

    @Override
    public synchronized void pause () {
        try {
            Dungeon.saveAll();
            Badges.saveGlobal();
        } catch ( IOException e ) {
            //
        }
    }

    @Override
    public synchronized void update () {
        if ( Dungeon.getHero() == null ) {
            return;
        }

        super.update();

        water.offset( 0, -5 * Game.getElapsed() );

        Actor.process();

        if ( Dungeon.getHero().ready && !Dungeon.getHero().paralysed ) {
            log.newLine();
        }

        cellSelector.enabled = Dungeon.getHero().ready;
    }

    @Override
    protected void onBackPressed () {
        if ( Dungeon.getDepth() == 0 && Dungeon.getLevel() instanceof MovieLevel ) {
            Music.INSTANCE.enable( PixelDungeon.music() );
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene( InterlevelScene.class );
            Dungeon.observe();
        } else if ( !cancel() ) {
            add( new WndGame() );
        }
    }

    @Override
    protected void onMenuPressed () {
        if ( Dungeon.getHero().ready ) {
            selectItem( null, WndBag.Mode.ALL, null );
        }
    }

    public void brightness ( boolean value ) {
        float value1 = value ? 1.5f : 1.0f;
        water.setRm( value1 );
        water.setGm( value1 );
        water.setBm( value1 );
        tiles.setRm( value1 );
        tiles.setGm( value1 );
        tiles.setBm( value1 );
        if ( value ) {
            fog.setAm( +2f );
            fog.setAa( -1f );
        } else {
            fog.setAm( +1f );
            fog.setAa( 0f );
        }
    }

    protected void addHeapSprite ( Heap heap ) {
        ItemSprite sprite = heap.sprite = (ItemSprite) heaps.recycle( ItemSprite.class );
        sprite.revive();
        sprite.link( heap );
        heaps.add( sprite );
    }

    protected void addDiscardedSprite ( Heap heap ) {
        heap.sprite = (DiscardedItemSprite) heaps.recycle( DiscardedItemSprite.class );
        heap.sprite.revive();
        heap.sprite.link( heap );
        heaps.add( heap.sprite );
    }

    protected void addPlantSprite ( Plant plant ) {
        ( plant.sprite = (PlantSprite) plants.recycle( PlantSprite.class ) ).reset( plant );
    }

    protected void addBlobSprite ( final Blob gas ) {
        if ( gas.emitter == null ) {
            gases.add( new BlobEmitter( gas ) );
        }
    }

    protected void addMobSprite ( Mob mob ) {
        CharSprite sprite = mob.sprite();
        sprite.setVisible( Dungeon.getVisible()[mob.pos] );
        mobs.add( sprite );
        sprite.link( mob );
    }

    protected void prompt ( String text ) {

        if ( prompt != null ) {
            prompt.killAndErase();
            prompt = null;
        }

        if ( text != null ) {
            prompt = new Toast( text ) {
                @Override
                protected void onClose () {
                    cancel();
                }
            };
            prompt.setCamera( uiCamera );
            prompt.setPos( ( uiCamera.getWidth() - prompt.width() ) / 2, uiCamera.getHeight() - 60 );
            add( prompt );
        }
    }

    protected void showBanner ( Banner banner ) {
        banner.setCamera( uiCamera );
        banner.setX( align( uiCamera, ( uiCamera.getWidth() - banner.getWidth() ) / 2 ) );
        banner.setY( align( uiCamera, ( uiCamera.getHeight() - banner.getHeight() ) / 3 ) );
        add( banner );
    }
}
