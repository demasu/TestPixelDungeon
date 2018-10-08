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
import com.demasu.testpixeldungeon.actors.mobs.npcs.HiredMerc;
import com.demasu.testpixeldungeon.actors.skills.BranchSkill;
import com.demasu.testpixeldungeon.actors.skills.Skill;
import com.demasu.testpixeldungeon.items.Gold;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.armor.Armor;
import com.demasu.testpixeldungeon.items.bags.Bag;
import com.demasu.testpixeldungeon.items.bags.Keyring;
import com.demasu.testpixeldungeon.items.bags.PotionBelt;
import com.demasu.testpixeldungeon.items.bags.ScrollHolder;
import com.demasu.testpixeldungeon.items.bags.SeedPouch;
import com.demasu.testpixeldungeon.items.bags.WandHolster;
import com.demasu.testpixeldungeon.items.potions.PotionOfHealing;
import com.demasu.testpixeldungeon.items.weapon.Weapon;
import com.demasu.testpixeldungeon.items.weapon.missiles.Bow;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.HeroSprite;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.sprites.MercSprite;
import com.demasu.testpixeldungeon.sprites.SkillSprite;
import com.demasu.testpixeldungeon.ui.Icons;
import com.demasu.testpixeldungeon.ui.ItemSlot;
import com.demasu.testpixeldungeon.ui.SkillSlot;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class WndMerc extends WndTabbed {

    protected static final int COLS_P = 4;
    protected static final int COLS_L = 6;
    protected static final int SLOT_SIZE = 28;
    protected static final int SLOT_MARGIN = 1;
    protected static final int TAB_WIDTH = 25;
    protected static final int TITLE_HEIGHT = 12;
    private static final int WIDTH = 120;
    private static final float GAP = 2;
    private static Mode lastMode;
    private static Storage lastBag;
    public boolean noDegrade = !PixelDungeon.itemDeg();
    protected int count;
    protected int col;
    protected int row;
    private Listener listener;
    private WndMerc.Mode mode;
    private String title;
    private int nCols;
    private int nRows;
    public WndMerc ( Storage bag, Listener listener ) {

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


        IconTitle titlebar = new IconTitle();
        titlebar.icon( new SkillSprite( Dungeon.getHero().hiredMerc.mercType.getImage() ) );
        titlebar.label( Utils.capitalize( Dungeon.getHero().hiredMerc.getNameAndLevel() ) );
        titlebar.health( (float) Dungeon.getHero().hiredMerc.getHP() / Dungeon.getHero().hiredMerc.getHT() );
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );


        BitmapTextMultiline info = PixelScene.createMultiline( Dungeon.getHero().hiredMerc.mercType.getDescription(), 6 );
        info.setMaxWidth( WIDTH );
        info.measure();
        info.setX( titlebar.left() );
        info.setY( titlebar.bottom() + GAP );
        add( info );

        //if(Dungeon.hero.hiredMerc.mercType != HiredMerc.MERC_TYPES.ArcherMaiden && Dungeon.hero.hiredMerc.mercType != HiredMerc.MERC_TYPES.Archer)
        add( new ItemButton( Dungeon.getHero().hiredMerc.weapon == null ? new Placeholder( Dungeon.getHero().hiredMerc.mercType.getWeaponPlaceHolder() ) : Dungeon.getHero().hiredMerc.weapon, false ).setPos( SLOT_MARGIN, info.getY() + info.height() + GAP ) );

        if ( Dungeon.getHero().hiredMerc.mercType != HiredMerc.MERC_TYPES.ArcherMaiden ) {
            add( new ItemButton( Dungeon.getHero().hiredMerc.armor == null ? new Placeholder( Dungeon.getHero().hiredMerc.mercType.getArmorPlaceHolder() ) : Dungeon.getHero().hiredMerc.armor, false ).setPos( SLOT_SIZE + 2 * SLOT_MARGIN, info.getY() + info.height() + GAP ) );
            if ( Dungeon.getHero().hiredMerc.mercType != HiredMerc.MERC_TYPES.Brute ) {
                add( new ItemButton( Dungeon.getHero().hiredMerc.carrying == null ? new Placeholder( ItemSpriteSheet.POTION_PLACEHOLDER ) : Dungeon.getHero().hiredMerc.carrying, false ).setPos( 2 * SLOT_SIZE + 3 * SLOT_MARGIN, info.getY() + info.height() + GAP ) );
            } else {
                add( new ItemButton( Dungeon.getHero().hiredMerc.carrying == null ? new Placeholder( ItemSpriteSheet.SMTH ) : Dungeon.getHero().hiredMerc.carrying, true ).setPos( 2 * SLOT_SIZE + 3 * SLOT_MARGIN, info.getY() + info.height() + GAP ) );
            }
        } else {
            add( new ItemButton( Dungeon.getHero().hiredMerc.carrying == null ? new Placeholder( ItemSpriteSheet.POTION_PLACEHOLDER ) : Dungeon.getHero().hiredMerc.carrying, false ).setPos( SLOT_SIZE + 2 * SLOT_MARGIN, info.getY() + info.height() + GAP ) );
        }


        if ( Dungeon.getHero().hiredMerc.mercType != HiredMerc.MERC_TYPES.ArcherMaiden ) {
            add( new SkillButton( Dungeon.getHero().hiredMerc.skill ).setPos( WIDTH - SLOT_SIZE - SLOT_MARGIN, info.getY() + info.height() + GAP ) );
        } else {
            add( new SkillButton( Dungeon.getHero().hiredMerc.skill ).setPos( WIDTH - 2 * ( SLOT_SIZE + SLOT_MARGIN ), info.getY() + info.height() + GAP ) );
            add( new SkillButton( Dungeon.getHero().hiredMerc.skillb ).setPos( WIDTH - SLOT_SIZE - SLOT_MARGIN, info.getY() + info.height() + GAP ) );
        }

        resize( WIDTH, (int) info.getY() + (int) info.height() + SLOT_SIZE + (int) GAP );

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
            icon.setAm( selected ? 1.0f : 0.6f );
        }

        @Override
        protected void layout () {
            super.layout();

            icon.copy( icon() );
            icon.setX( getX() + ( getWidth() - icon.getWidth() ) / 2 );
            icon.setY( getY() + ( getHeight() - icon.getHeight() ) / 2 - 2 - ( selected ? 0 : 1 ) );
            if ( !selected && icon.getY() < getY() + CUT ) {
                RectF frame = icon.frame();
                frame.top += ( getY() + CUT - icon.getY() ) / icon.getTexture().getHeight();
                icon.frame( frame );
                icon.setY( getY() + CUT );
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

    private class ItemButton extends ItemSlot implements WndBag.Listener {

        private static final int NORMAL = 0xFF4A4D44;
        private static final int EQUIPPED = 0xFF63665B;

        private static final int NBARS = 3;
        public boolean holdOnly = false;
        private Item item;
        private ColorBlock bg;
        private ColorBlock durability[];

        public ItemButton ( Item item, boolean holdOnly ) {

            super( item );

            this.holdOnly = holdOnly;

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
            bg.setX( getX() );
            bg.setY( getY() );


            super.layout();

            topRight.setVisible( false );
        }


        @Override
        public void item ( Item item ) {

            super.item( item );
            if ( item != null ) {

                bg.texture( TextureCache.createSolid( EQUIPPED ) );


                enable( true );


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
            if ( holdOnly ) {
                GameScene.selectItem( this, WndBag.Mode.BRUTE_HOLD, "Ask Brute To Hold" );
            } else if ( item instanceof Bow || item.image() == ItemSpriteSheet.EMPTY_BOW ) {
                GameScene.selectItem( this, WndBag.Mode.BOW, "Equip Bow On Merc" );
            } else if ( item instanceof Weapon || item.image() == ItemSpriteSheet.WEAPON ) {
                GameScene.selectItem( this, WndBag.Mode.WEAPON, "Equip Weapon On Merc" );
            } else if ( item instanceof Armor || item.image() == ItemSpriteSheet.ARMOR ) {
                GameScene.selectItem( this, WndBag.Mode.ARMOR, "Equip Armor On Merc" );
            } else if ( item instanceof PotionOfHealing || item.image() == ItemSpriteSheet.POTION_PLACEHOLDER ) {
                GameScene.selectItem( this, WndBag.Mode.HEALING_POTION, "Give Potion Of Healing" );
            }
        }

        @Override
        protected boolean onLongClick () {
            //GameScene.selectItem(this, (item instanceof Weapon || item.image() == ItemSpriteSheet.WEAPON) ? WndBag.Mode.WEAPON : WndBag.Mode.ARMOR, "Equip On Merc");
            WndMerc.this.hide();
            if ( holdOnly || item instanceof PotionOfHealing ) {
                Dungeon.getHero().hiredMerc.unEquipItem();
                return true;
            }
            if ( item instanceof Weapon ) {
                Dungeon.getHero().hiredMerc.unEquipWeapon();
            } else {
                Dungeon.getHero().hiredMerc.unEquipArmor();
            }
            return true;
        }

        @Override
        public void onSelect ( Item item ) {
            if ( item != null ) {
                if ( item instanceof Weapon && !holdOnly ) {
                    if ( Dungeon.getHero().belongings.weapon == item ) {
                        Dungeon.getHero().belongings.weapon = null;
                    } else {
                        item.detach( Dungeon.getHero().belongings.backpack );
                    }
                    Dungeon.getHero().hiredMerc.equipWeapon( item );
                } else if ( item instanceof Armor && !holdOnly ) {
                    if ( Dungeon.getHero().belongings.armor == item ) {
                        Dungeon.getHero().belongings.armor = null;
                    } else {
                        item.detach( Dungeon.getHero().belongings.backpack );
                    }
                    Dungeon.getHero().hiredMerc.equipArmor( item );
                } else if ( item instanceof PotionOfHealing ) {
                    item.detach( Dungeon.getHero().belongings.backpack );

                    Dungeon.getHero().hiredMerc.equipItem( new PotionOfHealing() );
                } else {
                    Dungeon.getHero().hiredMerc.equipItem( item.detachAll( Dungeon.getHero().belongings.backpack ) );
                }
            }
            ( (HeroSprite) Dungeon.getHero().sprite ).updateArmor();
            ( (MercSprite) Dungeon.getHero().hiredMerc.sprite ).updateArmor();
            Dungeon.getHero().spend( 1f );
            WndMerc.this.hide();
        }
    }

    private class SkillButton extends SkillSlot {

        private static final int NORMAL = 0xFF4A4D44;
        private static final int EQUIPPED = 0xFF63665B;


        private Skill skill;
        private ColorBlock bg;

        private ColorBlock durability[];

        public SkillButton ( Skill skill ) {

            super( skill );

            this.skill = skill;


            setWidth( SLOT_SIZE );
            setHeight( SLOT_SIZE );

            durability = new ColorBlock[Skill.MAX_LEVEL];

            if ( skill != null && skill.name != null && skill.level > 0 && skill.level <= Skill.MAX_LEVEL ) {
                for ( int i = 0; i < skill.level; i++ ) {
                    durability[i] = new ColorBlock( 2, 2, 0xFF00EE00 );
                    add( durability[i] );
                }
                for ( int i = skill.level; i < Skill.MAX_LEVEL; i++ ) {
                    durability[i] = new ColorBlock( 2, 2, 0x4000EE00 );
                    add( durability[i] );
                }
            }

            if ( skill instanceof BranchSkill ) {
                bg.setVisible( false );
            }
        }

        @Override
        protected void createChildren () {
            bg = new ColorBlock( SLOT_SIZE, SLOT_SIZE, NORMAL );
            add( bg );

            super.createChildren();
        }

        @Override
        protected void layout () {
            bg.setX( getX() );
            bg.setY( getY() );


            if ( skill != null && skill.name != null && skill.level > 0 && skill.level <= Skill.MAX_LEVEL ) {
                for ( int i = 0; i < Skill.MAX_LEVEL; i++ ) {
                    durability[i].setX( getX() + getWidth() - 9 + i * 3 );
                    durability[i].setY( getY() + 3 );

                }
            }

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
            if ( listener != null ) {

                hide();
                //listener.onSelect(skill);

            } else {

                GameScene.show( ( new WndSkill( null, skill ) ) );

            }
        }

        @Override
        protected boolean onLongClick () {
            GameScene.show( ( new WndSkill( null, skill ) ) );
            return true;
        }
    }
}
