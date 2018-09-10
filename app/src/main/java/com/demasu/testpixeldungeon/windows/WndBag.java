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
import com.demasu.testpixeldungeon.actors.hero.Belongings;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.items.Gold;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.armor.Armor;
import com.demasu.testpixeldungeon.items.bags.Bag;
import com.demasu.testpixeldungeon.items.bags.Keyring;
import com.demasu.testpixeldungeon.items.bags.PotionBelt;
import com.demasu.testpixeldungeon.items.bags.ScrollHolder;
import com.demasu.testpixeldungeon.items.bags.SeedPouch;
import com.demasu.testpixeldungeon.items.bags.WandHolster;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.items.potions.PotionOfHealing;
import com.demasu.testpixeldungeon.items.wands.Wand;
import com.demasu.testpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.demasu.testpixeldungeon.items.weapon.missiles.Boomerang;
import com.demasu.testpixeldungeon.items.weapon.missiles.Bow;
import com.demasu.testpixeldungeon.plants.Plant.Seed;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.ui.Icons;
import com.demasu.testpixeldungeon.ui.ItemSlot;
import com.demasu.testpixeldungeon.ui.QuickSlot;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.GameMath;

public class WndBag extends WndTabbed {

    protected static final int COLS_P = 4;
    protected static final int COLS_L = 6;
    protected static final int SLOT_WIDTH = 28;
    protected static final int SLOT_HEIGHT = 28;
    protected static final int SLOT_MARGIN = 1;
    protected static final int TAB_WIDTH = 25;
    protected static final int TITLE_HEIGHT = 12;
    private static Mode lastMode;
    private static Bag lastBag;
    public boolean noDegrade = PixelDungeon.itemDeg();
    protected int count;
    protected int col;
    protected int row;
    private Listener listener;
    private WndBag.Mode mode;
    private String title;
    private int nCols;
    private int nRows;

    public WndBag ( Bag bag, Listener listener, Mode mode, String title ) {

        super();

        this.listener = listener;
        this.mode = mode;
        this.title = title;

        lastMode = mode;
        lastBag = bag;

        nCols = PixelDungeon.landscape() ? COLS_L : COLS_P;
        nRows = ( Belongings.BACKPACK_SIZE + 4 + 1 ) / nCols + ( ( Belongings.BACKPACK_SIZE + 4 + 1 ) % nCols > 0 ? 1 : 0 );

        int slotsWidth = SLOT_WIDTH * nCols + SLOT_MARGIN * ( nCols - 1 );
        int slotsHeight = SLOT_HEIGHT * nRows + SLOT_MARGIN * ( nRows - 1 );

        BitmapText txtTitle = PixelScene.createText( title != null ? title : Utils.capitalize( bag.name() ), 9 );
        txtTitle.hardlight( TITLE_COLOR );
        txtTitle.measure();
        txtTitle.x = (int) ( slotsWidth - txtTitle.width() ) / 2;
        txtTitle.y = (int) ( TITLE_HEIGHT - txtTitle.height() ) / 2;
        add( txtTitle );

        placeItems( bag );

        resize( slotsWidth, slotsHeight + TITLE_HEIGHT );

        Belongings stuff = Dungeon.getHero().belongings;
        Bag[] bags = {
                stuff.backpack,
                stuff.getItem( SeedPouch.class ),
                stuff.getItem( ScrollHolder.class ),
                stuff.getItem( WandHolster.class ),
                stuff.getItem( PotionBelt.class ),
                stuff.getItem( Keyring.class ) };

        int totalTabSpace = 125;
        int tabWidth = totalTabSpace / bags.length + ( ( totalTabSpace % bags.length == 0 ) ? 0 : 1 );
        for ( Bag b : bags ) {
            if ( b != null ) {
                BagTab tab = new BagTab( b );
                tab.setSize( tabWidth, tabHeight() );
                add( tab );

                tab.select( b == bag );
            }
        }
    }

    public static WndBag lastBag ( Listener listener, Mode mode, String title ) {

        if ( mode == lastMode && lastBag != null &&
                Dungeon.getHero().belongings.backpack.contains( lastBag ) ) {

            return new WndBag( lastBag, listener, mode, title );

        } else {

            return new WndBag( Dungeon.getHero().belongings.backpack, listener, mode, title );

        }
    }

    public static WndBag seedPouch ( Listener listener, Mode mode, String title ) {
        SeedPouch pouch = Dungeon.getHero().belongings.getItem( SeedPouch.class );
        return pouch != null ?
                new WndBag( pouch, listener, mode, title ) :
                new WndBag( Dungeon.getHero().belongings.backpack, listener, mode, title );
    }

    protected void placeItems ( Bag container ) {

        // Equipped items
        Belongings stuff = Dungeon.getHero().belongings;
        placeItem( stuff.weapon != null ? stuff.weapon : new Placeholder( ItemSpriteSheet.WEAPON ) );
        placeItem( stuff.armor != null ? stuff.armor : new Placeholder( ItemSpriteSheet.ARMOR ) );
        placeItem( stuff.ring1 != null ? stuff.ring1 : new Placeholder( ItemSpriteSheet.RING ) );
        placeItem( stuff.ring2 != null ? stuff.ring2 : new Placeholder( ItemSpriteSheet.RING ) );
        placeItem( stuff.bow != null ? stuff.bow : new Placeholder( ItemSpriteSheet.EMPTY_BOW ) );

        boolean backpack = ( container == Dungeon.getHero().belongings.backpack );
        if ( !backpack ) {
            count = nCols;
            col = 0;
            row = 1;
        }

        // Items in the bag
        for ( Item item : container.items ) {
            placeItem( item );
        }

        // Free space
        while ( count - ( backpack ? 5 : nCols ) < container.size ) { // took out 1 for bow spot
            placeItem( null );
        }

        // Gold in the backpack
        if ( container == Dungeon.getHero().belongings.backpack ) {
            row = nRows - 1;
            col = nCols - 1;
            placeItem( new Gold( Dungeon.getGold() ) );
        }
    }

    protected void placeItem ( final Item item ) {

        int x = col * ( SLOT_WIDTH + SLOT_MARGIN );
        int y = TITLE_HEIGHT + row * ( SLOT_HEIGHT + SLOT_MARGIN );

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
        GameScene.show( new WndBag( ( (BagTab) tab ).bag, listener, mode, title ) );
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
        SEED,
        BOW,
        HEALING_POTION,
        BRUTE_HOLD
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
            icon.x = x + ( width - icon.width ) / 2;
            icon.y = y + ( height - icon.height ) / 2 - 2 - ( selected ? 0 : 1 );
            if ( !selected && icon.y < y + CUT ) {
                RectF frame = icon.frame();
                frame.top += ( y + CUT - icon.y ) / icon.texture.height;
                icon.frame( frame );
                icon.y = y + CUT;
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
                bg.visible = false;
            }

            width = SLOT_WIDTH;
            height = SLOT_HEIGHT;
        }

        @Override
        protected void createChildren () {
            bg = new ColorBlock( SLOT_WIDTH, SLOT_HEIGHT, NORMAL );
            add( bg );

            super.createChildren();
        }

        @Override
        protected void layout () {
            bg.x = x;
            bg.y = y;

            if ( noDegrade ) {
                durability = null; // no durability
            }

            if ( durability != null ) {
                for ( int i = 0; i < NBARS; i++ ) {
                    durability[i].x = x + 1 + i * 3;
                    durability[i].y = y + height - 3;
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

                if ( lastBag.owner.isAlive() && item.isUpgradable() && item.levelKnown ) {
                    durability = new ColorBlock[NBARS];
                    int nBars = (int) GameMath.gate( 0, Math.round( (float) NBARS * item.durability() / item.maxDurability() ), NBARS );
                    for ( int i = 0; i < nBars; i++ ) {
                        durability[i] = new ColorBlock( 2, 2, 0xFF00EE00 );
                        add( durability[i] );
                    }
                    for ( int i = nBars; i < NBARS; i++ ) {
                        durability[i] = new ColorBlock( 2, 2, 0xFFCC0000 );
                        add( durability[i] );
                    }
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
                                    mode == Mode.ENCHANTABLE && ( item instanceof MeleeWeapon || item instanceof Boomerang || item instanceof Bow || item instanceof Armor ) ||
                                    mode == Mode.WAND && ( item instanceof Wand ) ||
                                    mode == Mode.SEED && ( item instanceof Seed ) ||
                                    mode == Mode.ALL
                    );

                    if ( mode == Mode.BOW ) {
                        enable( item instanceof Bow && Dungeon.getHero().belongings.bow != item );
                    }
                    if ( mode == Mode.HEALING_POTION ) {
                        enable( item instanceof PotionOfHealing && ( (Potion) item ).isKnown() );
                    }
                    if ( mode == Mode.BRUTE_HOLD ) {
                        enable( !( item instanceof Bag ) && item != Dungeon.getHero().belongings.weapon && item != Dungeon.getHero().belongings.armor && item != Dungeon.getHero().belongings.ring1 && item != Dungeon.getHero().belongings.ring2 && item != Dungeon.getHero().belongings.bow );
                    }
                    if ( mode == Mode.ARMOR && title.contains( "Merc" ) && item instanceof Armor && ( (Armor) item ).tier > 5 ) {
                        enable( false );
                    }
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

                WndBag.this.add( new WndItem( WndBag.this, item ) );

            }
        }

        @Override
        protected boolean onLongClick () {
            if ( listener == null && item.defaultAction != null ) {
                hide();
                QuickSlot.primaryValue = item.stackable ? item.getClass() : item;
                QuickSlot.refresh();
                return true;
            } else {
                return false;
            }
        }
    }
}
