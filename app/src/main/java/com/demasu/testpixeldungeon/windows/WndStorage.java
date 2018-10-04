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

import android.graphics.RectF;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.hero.Storage;
import com.demasu.testpixeldungeon.items.Gold;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.armor.Armor;
import com.demasu.testpixeldungeon.items.bags.Bag;
import com.demasu.testpixeldungeon.items.bags.Keyring;
import com.demasu.testpixeldungeon.items.bags.PotionBelt;
import com.demasu.testpixeldungeon.items.bags.ScrollHolder;
import com.demasu.testpixeldungeon.items.bags.SeedPouch;
import com.demasu.testpixeldungeon.items.bags.WandHolster;
import com.demasu.testpixeldungeon.items.wands.Wand;
import com.demasu.testpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.demasu.testpixeldungeon.items.weapon.missiles.Boomerang;
import com.demasu.testpixeldungeon.plants.Plant.Seed;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.ui.Icons;
import com.demasu.testpixeldungeon.ui.ItemSlot;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class WndStorage extends WndTabbed {

    protected static final int COLS_P = 4;
    protected static final int COLS_L = 6;
    protected static final int SLOT_SIZE = 28;
    protected static final int SLOT_MARGIN = 1;
    protected static final int TAB_WIDTH = 25;
    protected static final int TITLE_HEIGHT = 12;
    private static Mode lastMode;
    private static Storage lastBag;
    public boolean noDegrade = !PixelDungeon.itemDeg();
    protected int count;
    protected int col;
    protected int row;
    private Listener listener;
    private WndStorage.Mode mode;
    private String title;
    private int nCols;
    private int nRows;
    public WndStorage ( Storage bag, Listener listener, Mode mode, String title ) {

        super();

        this.listener = listener;
        this.mode = mode;
        this.title = title;

        lastMode = mode;
        lastBag = bag;

        nCols = PixelDungeon.landscape() ? COLS_L : COLS_P;
        nRows = ( 5 ) / nCols + ( ( 5 ) % nCols > 0 ? 1 : 0 );

        int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * ( nCols - 1 );
        int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * ( nRows - 1 );

        BitmapText txtTitle = PixelScene.createText( title != null ? title : Utils.capitalize( "Storage" ), 9 );
        txtTitle.hardlight( TITLE_COLOR );
        txtTitle.measure();
        txtTitle.x = (int) ( slotsWidth - txtTitle.width() ) / 2;
        txtTitle.y = (int) ( TITLE_HEIGHT - txtTitle.height() ) / 2;
        add( txtTitle );

        placeItems( bag );

        resize( slotsWidth, slotsHeight + TITLE_HEIGHT );

    }

    protected void placeItems ( Storage container ) {


        boolean backpack = ( container == Dungeon.getHero().storage );
        if ( !backpack ) {
            count = nCols;
            col = 0;
            row = 1;
        }

        // Items in the bag
        for ( Item item : container.backpack.items ) {
            placeItem( item );
        }

        // Free space
        while ( count - ( backpack ? 0 : nCols ) < 5 ) {
            placeItem( null );
        }

    }

    protected void placeItem ( final Item item ) {

        int x = col * ( SLOT_SIZE + SLOT_MARGIN );
        int y = TITLE_HEIGHT + row * ( SLOT_SIZE + SLOT_MARGIN );

        add( new ItemButton( item ).setPos( x, y ) );

        if ( ++col >= nCols ) {
            col = 0;
            row++;
        }

        count++;
    }

    @Override
    public void onMenuPressed () {
        if ( listener == null ) {
            hide();
        }
    }

    @Override
    public void onBackPressed () {
        if ( listener != null ) {
            listener.onSelect( null );
        }
        super.onBackPressed();
    }

    @Override
    protected void onClick ( Tab tab ) {
        hide();
        //GameScene.show( new WndStorage( ((BagTab)tab).bag, listener, mode, title ) );
    }

    @Override
    protected int tabHeight () {
        return 20;
    }

    public enum Mode {
        ALL,
        UNIDENTIFED,
        UPGRADEABLE,
        QUICKSLOT,
        FOR_SALE,
        WEAPON,
        ARMOR,
        ENCHANTABLE,
        WAND,
        SEED
    }

    public interface Listener {
        void onSelect ( Item item );
    }

    private static class Placeholder extends Item {
        {
            name = null;
        }

        public Placeholder ( int image ) {
            this.image = image;
        }

        @Override
        public boolean isIdentified () {
            return true;
        }

        @Override
        public boolean isEquipped ( Hero hero ) {
            return true;
        }
    }

    private class BagTab extends Tab {

        private Image icon;

        private Bag bag;

        public BagTab ( Bag bag ) {
            super();

            this.bag = bag;

            icon = icon();
            add( icon );
        }

        @Override
        protected void select ( boolean value ) {
            super.select( value );
            icon.am = selected ? 1.0f : 0.6f;
        }

        @Override
        protected void layout () {
            super.layout();

            icon.copy( icon() );
            icon.x = getX() + ( getWidth() - icon.width ) / 2;
            icon.y = getY() + ( getHeight() - icon.height ) / 2 - 2 - ( selected ? 0 : 1 );
            if ( !selected && icon.y < getY() + CUT ) {
                RectF frame = icon.frame();
                frame.top += ( getY() + CUT - icon.y ) / icon.texture.getHeight();
                icon.frame( frame );
                icon.y = getY() + CUT;
            }
        }

        private Image icon () {
            if ( bag instanceof SeedPouch ) {
                return Icons.get( Icons.SEED_POUCH );
            } else if ( bag instanceof ScrollHolder ) {
                return Icons.get( Icons.SCROLL_HOLDER );
            } else if ( bag instanceof WandHolster ) {
                return Icons.get( Icons.WAND_HOLSTER );
            } else if ( bag instanceof PotionBelt ) {
                return Icons.get( Icons.POTION_BELT );
            } else if ( bag instanceof Keyring ) {
                return Icons.get( Icons.KEYRING );
            } else {
                return Icons.get( Icons.BACKPACK );
            }
        }
    }

    private class ItemButton extends ItemSlot {

        private static final int NORMAL = 0xFF4A4D44;
        private static final int EQUIPPED = 0xFF63665B;

        private static final int NBARS = 3;

        private Item item;
        private ColorBlock bg;

        private ColorBlock durability[];

        public ItemButton ( Item item ) {

            super( item );

            this.item = item;
            if ( item instanceof Gold ) {
                bg.setVisible( false );
            }

            setWidth( SLOT_SIZE );
            setHeight( SLOT_SIZE );
        }

        @Override
        protected void createChildren () {
            bg = new ColorBlock( SLOT_SIZE, SLOT_SIZE, NORMAL );
            add( bg );

            super.createChildren();
        }

        @Override
        protected void layout () {
            bg.x = getX();
            bg.y = getY();

            if ( noDegrade ) {
                durability = null; // no durability
            }

            if ( durability != null ) {
                for ( int i = 0; i < NBARS; i++ ) {
                    durability[i].x = getX() + 1 + i * 3;
                    durability[i].y = getY() + getHeight() - 3;
                }
            }

            super.layout();
        }

        @Override
        public void item ( Item item ) {

            super.item( item );
            if ( item != null ) {

                bg.texture( TextureCache.createSolid( item.isEquipped( Dungeon.getHero() ) ? EQUIPPED : NORMAL ) );
                if ( item.cursed && item.cursedKnown ) {
                    bg.ra = +0.2f;
                    bg.ga = -0.1f;
                } else if ( !item.isIdentified() ) {
                    bg.ra = 0.1f;
                    bg.ba = 0.1f;
                }


                if ( item.name() == null ) {
                    enable( false );
                } else {
                    enable(
                            mode == Mode.QUICKSLOT && ( item.defaultAction != null ) ||
                                    mode == Mode.FOR_SALE && ( item.price() > 0 ) && ( !item.isEquipped( Dungeon.getHero() ) || !item.cursed ) ||
                                    mode == Mode.UPGRADEABLE && item.isUpgradable() ||
                                    mode == Mode.UNIDENTIFED && !item.isIdentified() ||
                                    mode == Mode.WEAPON && ( item instanceof MeleeWeapon || item instanceof Boomerang ) ||
                                    mode == Mode.ARMOR && ( item instanceof Armor ) ||
                                    mode == Mode.ENCHANTABLE && ( item instanceof MeleeWeapon || item instanceof Boomerang || item instanceof Armor ) ||
                                    mode == Mode.WAND && ( item instanceof Wand ) ||
                                    mode == Mode.SEED && ( item instanceof Seed ) ||
                                    mode == Mode.ALL
                    );
                }
            } else {
                bg.color( NORMAL );
            }
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
            if ( listener != null ) {

                hide();
                listener.onSelect( item );

            } else {

                WndStorage.this.add( new WndItemStorage( WndStorage.this, item ) );

            }
        }

        @Override
        protected boolean onLongClick () {

            return false;

        }
    }
}
