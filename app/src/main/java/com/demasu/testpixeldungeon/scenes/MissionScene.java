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
import com.demasu.testpixeldungeon.effects.Flare;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.items.wands.WandOfBlink;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.MovieLevel;
import com.demasu.testpixeldungeon.levels.RegularLevel;
import com.demasu.testpixeldungeon.levels.features.Chasm;
import com.demasu.testpixeldungeon.plants.Plant;
import com.demasu.testpixeldungeon.sprites.LegendSprite;
import com.demasu.testpixeldungeon.ui.AttackIndicator;
import com.demasu.testpixeldungeon.ui.BusyIndicator;
import com.demasu.testpixeldungeon.ui.GameLog;
import com.demasu.testpixeldungeon.ui.HealthIndicator;
import com.demasu.testpixeldungeon.ui.MissionStatusPane;
import com.demasu.testpixeldungeon.ui.MissionToolbar;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.windows.WndBag.Mode;
import com.demasu.testpixeldungeon.windows.WndGame;
import com.demasu.testpixeldungeon.windows.WndStory;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MissionScene extends GameScene {


    public static boolean scenePause = false;
    public LegendSprite hero;
    protected MissionToolbar toolbar;


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

        super.originalCreate();
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
            blob.setEmitter( null );
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

        hero = new LegendSprite();
        hero.place( Dungeon.getHero().pos );
        hero.updateArmor();
        mobs.add( hero );

        add( new HealthIndicator() );

        add( cellSelector = new CellSelector( tiles ) );

        MissionStatusPane sb = new MissionStatusPane();
        sb.setCamera( uiCamera );
        sb.setSize( uiCamera.getWidth(), 0 );
        add( sb );

        toolbar = new MissionToolbar();
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
        //	Badges.saveGlobal();

        super.destroy();
    }

    @Override
    public synchronized void pause () {
        //try {
        //	Dungeon.saveAll();
        //	Badges.saveGlobal();
        //} catch (IOException e) {
        //
        //}
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
            Game.switchScene( TitleScene.class );
            Dungeon.observe();
        } else if ( !cancel() ) {
            add( new WndGame() );
        }
    }

    @Override
    protected void onMenuPressed () {
        if ( Dungeon.getHero().ready ) {
            selectItem( null, Mode.ALL, null );
        }
    }

}
