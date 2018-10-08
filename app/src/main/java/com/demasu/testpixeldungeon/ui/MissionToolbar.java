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
package com.demasu.testpixeldungeon.ui;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.DungeonTilemap;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.mobs.ColdGirl;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.items.Heap;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.plants.Plant;
import com.demasu.testpixeldungeon.scenes.CellSelector;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.MissionScene;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.sprites.ItemSprite;
import com.demasu.testpixeldungeon.windows.WndBag;
import com.demasu.testpixeldungeon.windows.WndCatalogus;
import com.demasu.testpixeldungeon.windows.WndHero;
import com.demasu.testpixeldungeon.windows.WndInfoCell;
import com.demasu.testpixeldungeon.windows.WndInfoItem;
import com.demasu.testpixeldungeon.windows.WndInfoMob;
import com.demasu.testpixeldungeon.windows.WndInfoPlant;
import com.demasu.testpixeldungeon.windows.WndMessage;
import com.demasu.testpixeldungeon.windows.WndSkills;
import com.demasu.testpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

public class MissionToolbar extends Component {

    public static boolean tapAgainToSearch = false;
    private static MissionToolbar instance;
    private static CellSelector.Listener informer = new CellSelector.Listener() {
        @Override
        public void onSelect ( Integer cell ) {

            tapAgainToSearch = false;

            if ( cell == null ) {
                return;
            }

            if ( cell < 0 || cell > Level.LENGTH || ( !Dungeon.getLevel().visited[cell] && !Dungeon.getLevel().mapped[cell] ) ) {
                GameScene.show( new WndMessage( "You don't know what is there." ) );
                return;
            }

            if ( !Dungeon.getVisible()[cell] ) {
                GameScene.show( new WndInfoCell( cell ) );
                return;
            }

            if ( cell == Dungeon.getHero().pos ) {
                GameScene.show( new WndHero() );
                return;
            }

            Mob mob = (Mob) Actor.findChar( cell );
            if ( mob != null ) {
                GameScene.show( new WndInfoMob( mob ) );
                return;
            }

            Heap heap = Dungeon.getLevel().heaps.get( cell );
            if ( heap != null && heap.type != Heap.Type.HIDDEN ) {
                if ( heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0 ) {
                    GameScene.show( new WndTradeItem( heap, false ) );
                } else {
                    GameScene.show( new WndInfoItem( heap ) );
                }
                return;
            }

            Plant plant = Dungeon.getLevel().plants.get( cell );
            if ( plant != null ) {
                GameScene.show( new WndInfoPlant( plant ) );
                return;
            }

            GameScene.show( new WndInfoCell( cell ) );
        }

        @Override
        public String prompt () {
            return "Select a cell to examine.\nTap again to search.";
        }
    };
    private Tool btnWait;
    private Tool btnSkill;
    private Tool btnMerc;
    private Tool btnLastUsed;
    //private Tool btnSearch;
    // private Tool btnKing;
    private Tool btnInfoSearch;
    private Tool btnInventory;
    private Tool btnQuick1;
    private Tool btnQuick2;
    private PickedUpItem pickedUp;
    private boolean lastEnabled = true;

    public MissionToolbar () {
        super();

        instance = this;

        setHeight( btnInventory.height() );
    }

    public static boolean secondQuickslot () {
        return instance.btnQuick2.getVisible();
    }

    public static void secondQuickslot ( boolean value ) {
        instance.btnQuick2.setVisible( value );
        instance.btnQuick2.setActive( value );
        instance.layout();
    }

    @Override
    protected void createChildren () {

        add( btnWait = new Tool( 0, 7, 20, 25 ) {
            @Override
            protected void onClick () {
                Dungeon.getHero().rest( false );
            }

            protected boolean onLongClick () {
                Dungeon.getHero().rest( true );
                return true;
            }

        } );

        add( btnSkill = new Tool( 20, 7, 20, 25 ) {
            @Override
            protected void onClick () {
                GameScene.show( new WndSkills( null, null ) );
            }

            protected boolean onLongClick () {
                GameScene.show( new WndSkills( null, null ) );
                return true;
            }

        } );


        add( btnLastUsed = new Tool( 40, 7, 20, 25 ) {
            @Override
            protected void onClick () {
                Dungeon.getHero().heroSkills.showLastUsed();
            }

            protected boolean onLongClick () {
                Dungeon.getHero().heroSkills.showLastUsed();
                return true;
            }

        } );

        add( btnMerc = new Tool( 252, 7, 20, 25 ) {
            @Override
            protected void onClick () {
                Dungeon.getHero().sprite.showStatus( CharSprite.NEUTRAL, "I don't trust mercs" );

            }

            protected boolean onLongClick () {
                Dungeon.getHero().sprite.showStatus( CharSprite.NEUTRAL, "I don't trust mercs" );
                return true;
            }

        } );



        /*
        add( btnSearch = new Tool( 20, 7, 20, 25 ) {
            @Override
            protected void onClick() {
                Dungeon.hero.search( true );
            }
        } );

        */
        add( btnInfoSearch = new Tool( 107, 7, 20, 25 ) {
            @Override
            protected void onClick () {
                if ( !tapAgainToSearch ) {
                    GameScene.selectCell( informer );
                } else {
                    Dungeon.getHero().search( true );
                }
                tapAgainToSearch = !tapAgainToSearch;
            }

            @Override
            protected boolean onLongClick () {
                Dungeon.getHero().search( true );
                return true;
            }
        } );

        add( btnInventory = new Tool( 60, 7, 23, 25 ) {
            private GoldIndicator gold;

            @Override
            protected void onClick () {
                GameScene.show( new WndBag( Dungeon.getHero().belongings.backpack, null, WndBag.Mode.ALL, null ) );
            }

            protected boolean onLongClick () {
                GameScene.show( new WndCatalogus() );
                return true;
            }

            @Override
            protected void createChildren () {
                super.createChildren();
                gold = new GoldIndicator();
                add( gold );
            }

            @Override
            protected void layout () {
                super.layout();
                gold.fill( this );
            }

        } );

        add( btnQuick1 = new QuickslotTool( 83, 7, 22, 25, true ) );
        add( btnQuick2 = new QuickslotTool( 83, 7, 22, 25, false ) );
        btnQuick2.setVisible( ( QuickSlot.getSecondaryValue() != null ) );

        add( pickedUp = new PickedUpItem() );
    }

    @Override
    protected void layout () {
        btnWait.setPos( getX(), getY() );
        //btnSearch.setPos( btnWait.right(), y );
        btnInfoSearch.setPos( 0, 70 );
        btnMerc.setPos( 0, 40 );
        btnSkill.setPos( btnWait.right(), getY() );

        btnLastUsed.setPos( btnSkill.right(), getY() );
        btnQuick1.setPos( getWidth() - btnQuick1.width(), getY() );
        if ( btnQuick2.getVisible() ) {
            btnQuick2.setPos( btnQuick1.left() - btnQuick2.width(), getY() );
            btnInventory.setPos( btnQuick2.left() - btnInventory.width(), getY() );
        } else {
            btnInventory.setPos( btnQuick1.left() - btnInventory.width(), getY() );
        }
    }

    @Override
    public void update () {
        super.update();

        setVisible( !MissionScene.scenePause );

        btnInventory.setVisible( false );
        btnInventory.enable( false );
        btnQuick1.setVisible( false );
        btnQuick1.enable( false );
        btnQuick2.setVisible( false );
        btnQuick2.enable( false );

        if ( lastEnabled != Dungeon.getHero().ready ) {
            lastEnabled = Dungeon.getHero().ready;

            for ( Gizmo tool : getMembers() ) {
                if ( tool instanceof Tool ) {
                    ( (Tool) tool ).enable( lastEnabled );

                    if ( tool == btnLastUsed ) {
                        tool.setVisible( Dungeon.getHero().heroSkills.lastUsed != null );
                    }
                    if ( tool == btnMerc && Dungeon.getDepth() == ColdGirl.FROST_DEPTH ) {
                        ( (Tool) tool ).enable( false );
                    }
                }
            }
        }
    }

    public void pickup ( Item item ) {
        pickedUp.reset( item,
                btnInventory.centerX(),
                btnInventory.centerY() );
    }

    private static class Tool extends Button {

        private static final int BGCOLOR = 0x7B8073;

        protected Image base;

        public Tool ( int x, int y, int width, int height ) {
            super();

            base.frame( x, y, width, height );

            this.setWidth( width );
            this.setHeight( height );
        }

        @Override
        protected void createChildren () {
            super.createChildren();

            base = new Image( Assets.TOOLBAR );
            add( base );
        }

        @Override
        protected void layout () {
            super.layout();

            base.setX( getX() );
            base.setY( getY() );
        }

        @Override
        protected void onTouchDown () {
            base.brightness( 1.4f );
        }

        @Override
        protected void onTouchUp () {
            if ( isActive() ) {
                base.resetColor();
            } else {
                base.tint( BGCOLOR, 0.7f );
            }
        }

        public void enable ( boolean value ) {
            if ( value != isActive() ) {
                if ( value ) {
                    base.resetColor();
                } else {
                    base.tint( BGCOLOR, 0.7f );
                }
                setActive( value );
            }
        }
    }

    private static class QuickslotTool extends Tool {

        private QuickSlot slot;

        public QuickslotTool ( int x, int y, int width, int height, boolean primary ) {
            super( x, y, width, height );
            if ( primary ) {
                slot.primary();
            } else {
                slot.secondary();
            }
        }

        @Override
        protected void createChildren () {
            super.createChildren();

            slot = new QuickSlot();
            add( slot );
        }

        @Override
        protected void layout () {
            super.layout();
            slot.setRect( getX() + 1, getY() + 2, getWidth() - 2, getHeight() - 2 );
        }

        @Override
        public void enable ( boolean value ) {
            slot.enable( value );
            super.enable( value );
        }
    }

    private static class PickedUpItem extends ItemSprite {

        private static final float DISTANCE = DungeonTilemap.SIZE;
        private static final float DURATION = 0.2f;

        private float dstX;
        private float dstY;
        private float left;

        public PickedUpItem () {
            super();

            originToCenter();

            setActive( false );
            setVisible( false );
        }

        public void reset ( Item item, float dstX, float dstY ) {
            view( item.image(), item.glowing() );

            setActive( true );
            setVisible( true );

            this.dstX = dstX - ItemSprite.SIZE / 2;
            this.dstY = dstY - ItemSprite.SIZE / 2;
            left = DURATION;

            setX( this.dstX - DISTANCE );
            setY( this.dstY - DISTANCE );
            alpha( 1 );
        }

        @Override
        public void update () {
            super.update();

            if ( ( left -= Game.getElapsed() ) <= 0 ) {

                setVisible( false );
                setActive( false );

            } else {
                float p = left / DURATION;
                getScale().set( (float) Math.sqrt( p ) );
                float offset = DISTANCE * p;
                setX( dstX - offset );
                setY( dstY - offset );
            }
        }
    }
}
