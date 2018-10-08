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
package com.demasu.testpixeldungeon.windows;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.Difficulties;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.Statistics;
import com.demasu.testpixeldungeon.actors.hero.Belongings;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.HeroSprite;
import com.demasu.testpixeldungeon.ui.BadgesList;
import com.demasu.testpixeldungeon.ui.Icons;
import com.demasu.testpixeldungeon.ui.ItemSlot;
import com.demasu.testpixeldungeon.ui.QuickSlot;
import com.demasu.testpixeldungeon.ui.RedButton;
import com.demasu.testpixeldungeon.ui.ScrollPane;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

import java.util.Locale;

public class WndRanking extends WndTabbed {

    private static final String TXT_ERROR = "Unable to load additional information";

    private static final String TXT_STATS = "Stats";
    private static final String TXT_ITEMS = "Items";
    private static final String TXT_BADGES = "Badges";

    private static final int WIDTH = 112;
    private static final int HEIGHT = 134;

    private static final int TAB_WIDTH = 40;

    private Thread thread;
    private String error = null;

    private Image busy;

    public WndRanking ( final String gameFile ) {

        super();
        resize( WIDTH, HEIGHT );

        thread = new Thread() {
            @Override
            public void run () {
                try {
                    Badges.loadGlobal();
                    Dungeon.loadGame( gameFile );
                } catch ( Exception e ) {
                    error = TXT_ERROR;
                }
            }
        };
        thread.start();


        busy = Icons.BUSY.get();
        busy.getOrigin().set( busy.getWidth() / 2, busy.getHeight() / 2 );
        busy.setAngularSpeed( 720 );
        busy.setX( ( WIDTH - busy.getWidth() ) / 2 );
        busy.setY( ( HEIGHT - busy.getHeight() ) / 2 );
        add( busy );
    }

    @Override
    public void update () {
        super.update();

        if ( thread != null && !thread.isAlive() ) {
            thread = null;
            if ( Dungeon.getHero() == null ) {
                error = TXT_ERROR;
            }
            if ( error == null ) {
                remove( busy );
                createControls();
            } else {
                hide();
                Game.scene().add( new WndError( TXT_ERROR ) );
            }
        }
    }

    private void createControls () {

        String[] labels =
                { TXT_STATS, TXT_ITEMS, TXT_BADGES };
        Group[] pages =
                { new StatsTab(), new ItemsTab(), new BadgesTab() };

        for ( int i = 0; i < pages.length; i++ ) {

            add( pages[i] );

            Tab tab = new RankingTab( labels[i], pages[i] );
            tab.setSize( TAB_WIDTH, tabHeight() );
            add( tab );
        }

        select( 0 );
    }

    private class RankingTab extends LabeledTab {

        private Group page;

        public RankingTab ( String label, Group page ) {
            super( label );
            this.page = page;
        }

        @Override
        protected void select ( boolean value ) {
            super.select( value );
            if ( page != null ) {
                page.setVisible( selected );
                page.setActive( selected );
            }
        }
    }

    private class StatsTab extends Group {

        private static final int GAP = 4;

        private static final String TXT_TITLE = "Level %d %s (SPD)";

        private static final String TXT_CHALLENGES = "Challenges";

        private static final String TXT_HEALTH = "Health";
        private static final String TXT_STR = "Strength";

        private static final String TXT_DURATION = "Game Duration";

        private static final String TXT_DEPTH = "Maximum Depth";
        private static final String TXT_ENEMIES = "Mobs Killed";
        private static final String TXT_GOLD = "Gold Collected";

        private static final String TXT_FOOD = "Food Eaten";
        private static final String TXT_ALCHEMY = "Potions Cooked";
        private static final String TXT_ANKHS = "Ankhs Used";

        private static final String TXT_DIFF = "Difficulty";

        public StatsTab () {
            super();

            if ( Dungeon.getHero() == null ) {
                IconTitle title = new IconTitle();
                title.label( "ERROR loading data" );
                title.setRect( 0, 0, WIDTH, 0 );
                add( title );
                return;
            }

            String heroClass = Dungeon.getHero().className();

            IconTitle title = new IconTitle();
            title.icon( HeroSprite.avatar( Dungeon.getHero().getHeroClass(), Dungeon.getHero().tier() ) );
            title.label( Utils.format( TXT_TITLE, Dungeon.getHero().getLvl(), heroClass ).toUpperCase( Locale.ENGLISH ) );
            title.setRect( 0, 0, WIDTH, 0 );
            add( title );

            float pos = title.bottom();

            if ( Dungeon.getChallenges() > 0 ) {
                RedButton btnCatalogus = new RedButton( TXT_CHALLENGES ) {
                    @Override
                    protected void onClick () {
                        Game.scene().add( new WndChallenges( Dungeon.getChallenges(), false ) );
                    }
                };
                btnCatalogus.setRect( 0, pos + GAP, btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
                add( btnCatalogus );

                pos = btnCatalogus.bottom();
            }

            pos += GAP + GAP;

            pos = statSlot( this, TXT_STR, Integer.toString( Dungeon.getHero().getSTR() ), pos );
            pos = statSlot( this, TXT_HEALTH, Integer.toString( Dungeon.getHero().getHT() ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_DURATION, Integer.toString( (int) Statistics.duration ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_DEPTH, Integer.toString( Statistics.deepestFloor ), pos );
            pos = statSlot( this, TXT_ENEMIES, Integer.toString( Statistics.enemiesSlain ), pos );
            pos = statSlot( this, TXT_GOLD, Integer.toString( Statistics.goldCollected ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_FOOD, Integer.toString( Statistics.foodEaten ), pos );
            pos = statSlot( this, TXT_ALCHEMY, Integer.toString( Statistics.potionsCooked ), pos );
            pos = statSlot( this, TXT_ANKHS, Integer.toString( Statistics.ankhsUsed ), pos );

            pos = statSlot( this, TXT_DIFF, Difficulties.title( Dungeon.getHero().difficulty ), pos );
        }

        private float statSlot ( Group parent, String label, String value, float pos ) {

            BitmapText txt = PixelScene.createText( label, 7 );
            txt.setY( pos );
            parent.add( txt );

            txt = PixelScene.createText( value, 7 );
            txt.measure();
            txt.setX( PixelScene.align( WIDTH * 0.65f ) );
            txt.setY( pos );
            parent.add( txt );

            return pos + GAP + txt.baseLine();
        }
    }

    private class ItemsTab extends Group {

        private int count;
        private float pos;

        public ItemsTab () {
            super();

            if ( Dungeon.getHero() == null ) {
                IconTitle title = new IconTitle();
                title.label( "ERROR loading data" );
                title.setRect( 0, 0, WIDTH, 0 );
                add( title );
                return;
            }

            Belongings stuff = Dungeon.getHero().belongings;
            if ( stuff.weapon != null ) {
                addItem( stuff.weapon );
            }
            if ( stuff.armor != null ) {
                addItem( stuff.armor );
            }
            if ( stuff.ring1 != null ) {
                addItem( stuff.ring1 );
            }
            if ( stuff.ring2 != null ) {
                addItem( stuff.ring2 );
            }

            Item primary = getQuickslot( QuickSlot.getPrimaryValue() );
            Item secondary = getQuickslot( QuickSlot.getSecondaryValue() );

            if ( count >= 4 && primary != null && secondary != null ) {

                float size = ItemButton.SIZE;

                ItemButton slot = new ItemButton( primary );
                slot.setRect( 0, pos, size, size );
                add( slot );

                slot = new ItemButton( secondary );
                slot.setRect( size + 1, pos, size, size );
                add( slot );
            } else {
                if ( primary != null ) {
                    addItem( primary );
                }
                if ( secondary != null ) {
                    addItem( secondary );
                }
            }
        }

        private void addItem ( Item item ) {
            LabelledItemButton slot = new LabelledItemButton( item );
            slot.setRect( 0, pos, width, LabelledItemButton.SIZE );
            add( slot );

            pos += slot.height() + 1;
            count++;
        }

        private Item getQuickslot ( Object value ) {
            if ( value instanceof Item && Dungeon.getHero().belongings.backpack.contains( (Item) value ) ) {

                return (Item) value;

            } else if ( value instanceof Class ) {

                @SuppressWarnings ( "unchecked" )
                Item item = Dungeon.getHero().belongings.getItem( (Class<? extends Item>) value );
                if ( item != null ) {
                    return item;
                }
            }

            return null;
        }
    }

    private class BadgesTab extends Group {

        public BadgesTab () {
            super();

            if ( Dungeon.getHero() == null ) {
                IconTitle title = new IconTitle();
                title.label( "ERROR loading data" );
                title.setRect( 0, 0, WIDTH, 0 );
                add( title );
                return;
            }

            setCamera( WndRanking.this.getCamera() );

            ScrollPane list = new BadgesList( false );
            add( list );

            list.setSize( WIDTH, HEIGHT );
        }
    }

    private class ItemButton extends Button {

        public static final int SIZE = 26;

        protected Item item;

        protected ItemSlot slot;
        private ColorBlock bg;

        public ItemButton ( Item item ) {

            super();

            this.item = item;

            slot.item( item );
            if ( item.cursed && item.cursedKnown ) {
                bg.setRa( +0.2f );
                bg.setGa( -0.1f );
            } else if ( !item.isIdentified() ) {
                bg.setRa( 0.1f );
                bg.setBa( 0.1f );
            }
        }

        @Override
        protected void createChildren () {

            bg = new ColorBlock( SIZE, SIZE, 0xFF4A4D44 );
            add( bg );

            slot = new ItemSlot();
            add( slot );

            super.createChildren();
        }

        @Override
        protected void layout () {
            bg.setX( getX() );
            bg.setY( getY() );

            slot.setRect( getX(), getY(), SIZE, SIZE );

            super.layout();
        }

        @Override
        protected void onTouchDown () {
            bg.brightness( 1.5f );
            Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
        }

        protected void onTouchUp () {
            bg.brightness( 1.0f );
        }

        @Override
        protected void onClick () {
            Game.scene().add( new WndItem( null, item ) );
        }
    }

    private class LabelledItemButton extends ItemButton {
        private BitmapText name;

        public LabelledItemButton ( Item item ) {
            super( item );
        }

        @Override
        protected void createChildren () {
            super.createChildren();

            name = PixelScene.createText( "?", 7 );
            add( name );
        }

        @Override
        protected void layout () {

            super.layout();

            name.setX( slot.right() + 2 );
            name.setY( getY() + ( getHeight() - name.baseLine() ) / 2 );

            String str = Utils.capitalize( item.name() );
            name.text( str );
            name.measure();
            if ( name.width() > getWidth() - name.getX() ) {
                do {
                    str = str.substring( 0, str.length() - 1 );
                    name.text( str + "..." );
                    name.measure();
                } while ( name.width() > getWidth() - name.getX() );
            }
        }
    }
}
