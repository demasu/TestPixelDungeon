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

import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.hero.Storage;
import com.demasu.testpixeldungeon.actors.mobs.npcs.HiredMerc;
import com.demasu.testpixeldungeon.actors.skills.BranchSkill;
import com.demasu.testpixeldungeon.actors.skills.Endurance;
import com.demasu.testpixeldungeon.actors.skills.Skill;
import com.demasu.testpixeldungeon.items.Gold;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.armor.Armor;
import com.demasu.testpixeldungeon.items.bags.Bag;
import com.demasu.testpixeldungeon.items.bags.Keyring;
import com.demasu.testpixeldungeon.items.bags.ScrollHolder;
import com.demasu.testpixeldungeon.items.bags.SeedPouch;
import com.demasu.testpixeldungeon.items.bags.WandHolster;
import com.demasu.testpixeldungeon.items.potions.PotionOfHealing;
import com.demasu.testpixeldungeon.items.wands.Wand;
import com.demasu.testpixeldungeon.items.weapon.Weapon;
import com.demasu.testpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.demasu.testpixeldungeon.items.weapon.missiles.Boomerang;
import com.demasu.testpixeldungeon.items.weapon.missiles.Bow;
import com.demasu.testpixeldungeon.plants.Plant.Seed;
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

public class WndMerc extends WndTabbed {

	public static enum Mode {
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

	protected static final int COLS_P	= 4;
	protected static final int COLS_L	= 6;

	protected static final int SLOT_SIZE	= 28;
	protected static final int SLOT_MARGIN	= 1;

	protected static final int TAB_WIDTH	= 25;

	protected static final int TITLE_HEIGHT	= 12;

	private Listener listener;
	private WndMerc.Mode mode;
	private String title;

	private int nCols;
	private int nRows;

	protected int count;
	protected int col;
	protected int row;

	private static Mode lastMode;
	private static Storage lastBag;

    private static final int WIDTH = 120;

    public boolean noDegrade = !PixelDungeon.itemDeg();
    private static final float GAP	= 2;

	public WndMerc(Storage bag, Listener listener) {
		
		super();
		
		this.listener = listener;
		this.mode = mode;
		this.title = title;
		
		lastMode = mode;
		lastBag = bag;
		
		nCols = PixelDungeon.landscape() ? COLS_L : COLS_P;
		nRows = (5) / nCols + ((5) % nCols > 0 ? 1 : 0);
		
		int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
		int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1);
		


        IconTitle titlebar = new IconTitle();
        titlebar.icon( new SkillSprite( Dungeon.hero.hiredMerc.mercType.getImage() ) );
        titlebar.label( Utils.capitalize( Dungeon.hero.hiredMerc.getNameAndLevel() ) );
        titlebar.health( (float) Dungeon.hero.hiredMerc.HP / Dungeon.hero.hiredMerc.HT);
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add(titlebar);


        BitmapTextMultiline info = PixelScene.createMultiline(Dungeon.hero.hiredMerc.mercType.getDescription(), 6);
        info.maxWidth = WIDTH;
        info.measure();
        info.x = titlebar.left();
        info.y = titlebar.bottom() + GAP;
        add( info );

        //if(Dungeon.hero.hiredMerc.mercType != HiredMerc.MERC_TYPES.ArcherMaiden && Dungeon.hero.hiredMerc.mercType != HiredMerc.MERC_TYPES.Archer)
        add( new ItemButton( Dungeon.hero.hiredMerc.weapon == null ? new Placeholder( Dungeon.hero.hiredMerc.mercType.getWeaponPlaceHolder() ) :  Dungeon.hero.hiredMerc.weapon , false).setPos( SLOT_MARGIN, info.y + info.height() + GAP) );

        if(Dungeon.hero.hiredMerc.mercType != HiredMerc.MERC_TYPES.ArcherMaiden)
        {
            add( new ItemButton( Dungeon.hero.hiredMerc.armor == null ? new Placeholder( Dungeon.hero.hiredMerc.mercType.getArmorPlaceHolder() ) :  Dungeon.hero.hiredMerc.armor, false).setPos( SLOT_SIZE + 2 * SLOT_MARGIN , info.y + info.height() + GAP) );
            if(Dungeon.hero.hiredMerc.mercType != HiredMerc.MERC_TYPES.Brute)
                add( new ItemButton( Dungeon.hero.hiredMerc.carrying == null ? new Placeholder( ItemSpriteSheet.POTION_PLACEHOLDER ) : Dungeon.hero.hiredMerc.carrying , false) .setPos( 2 * SLOT_SIZE + 3 * SLOT_MARGIN, info.y + info.height() + GAP) );
            else
                add( new ItemButton( Dungeon.hero.hiredMerc.carrying == null ? new Placeholder( ItemSpriteSheet.SMTH ) : Dungeon.hero.hiredMerc.carrying , true) .setPos( 2 * SLOT_SIZE + 3 * SLOT_MARGIN, info.y + info.height() + GAP) );
        }
        else
        {
            add( new ItemButton( Dungeon.hero.hiredMerc.carrying == null ? new Placeholder( ItemSpriteSheet.POTION_PLACEHOLDER ) : Dungeon.hero.hiredMerc.carrying , false) .setPos( SLOT_SIZE + 2 * SLOT_MARGIN , info.y + info.height() + GAP) );
        }


        if(Dungeon.hero.hiredMerc.mercType != HiredMerc.MERC_TYPES.ArcherMaiden)
            add( new SkillButton(Dungeon.hero.hiredMerc.skill).setPos(WIDTH - SLOT_SIZE - SLOT_MARGIN, info.y + info.height() + GAP) );
        else
        {
            add( new SkillButton(Dungeon.hero.hiredMerc.skill).setPos(WIDTH - 2 * (SLOT_SIZE + SLOT_MARGIN) , info.y + info.height() + GAP) );
            add( new SkillButton(Dungeon.hero.hiredMerc.skillb).setPos(WIDTH - SLOT_SIZE - SLOT_MARGIN, info.y + info.height() + GAP) );
        }

		resize( WIDTH, (int) info.y + (int)info.height() + SLOT_SIZE + (int)GAP );

	}

	

	
	@Override
	public void onMenuPressed() {
		if (listener == null) {
			hide();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (listener != null) {
			listener.onSelect( null );
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onClick( Tab tab ) {
		hide();
		//GameScene.show( new WndStorage( ((BagTab)tab).bag, listener, mode, title ) );
	}
	
	@Override
	protected int tabHeight() {
		return 20;
	}
	
	private class BagTab extends Tab {
		
		private Image icon;

		private Bag bag;
		
		public BagTab( Bag bag ) {
			super();
			
			this.bag = bag;
			
			icon = icon();
			add( icon );
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			icon.am = selected ? 1.0f : 0.6f;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			icon.copy( icon() );
			icon.x = x + (width - icon.width) / 2;
			icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
			if (!selected && icon.y < y + CUT) {
				RectF frame = icon.frame();
				frame.top += (y + CUT - icon.y) / icon.texture.height;
				icon.frame( frame );
				icon.y = y + CUT;
			}
		}
		
		private Image icon() {
			if (bag instanceof SeedPouch) {
				return Icons.get( Icons.SEED_POUCH );
			} else if (bag instanceof ScrollHolder) {
				return Icons.get( Icons.SCROLL_HOLDER );
			} else if (bag instanceof WandHolster) {
				return Icons.get( Icons.WAND_HOLSTER );
			} else if (bag instanceof Keyring) {
				return Icons.get( Icons.KEYRING );
			} else {
				return Icons.get( Icons.BACKPACK );
			}
		}
	}
	
	private static class Placeholder extends Item {
		{
			name = null;
		}
		
		public Placeholder( int image ) {
			this.image = image;
		}
		
		@Override
		public boolean isIdentified() {
			return true;
		}
		
		@Override
		public boolean isEquipped( Hero hero ) {
			return true;
		}
	}
	
	private class ItemButton extends ItemSlot  implements WndBag.Listener{
		
		private static final int NORMAL		= 0xFF4A4D44;
		private static final int EQUIPPED	= 0xFF63665B;
		
		private static final int NBARS	= 3;
		
		private Item item;
		private ColorBlock bg;
		
		private ColorBlock durability[];

        public  boolean holdOnly = false;
		public ItemButton( Item item , boolean holdOnly) {

			super( item );

            this.holdOnly = holdOnly;

			this.item = item;
			if (item instanceof Gold) {
				bg.visible = false;
			}
			
			width = height = SLOT_SIZE;
		}
		
		@Override
		protected void createChildren() {	
			bg = new ColorBlock( SLOT_SIZE, SLOT_SIZE, NORMAL );
			add( bg );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;


			
			super.layout();

            topRight.visible = false;
		}



		@Override
		public void item( Item item ) {
			
			super.item( item );
			if (item != null) {

				bg.texture( TextureCache.createSolid( EQUIPPED  ) );


                enable( true );
				


			} else {
				bg.color(NORMAL);
			}
		}
		
		@Override
		protected void onTouchDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		};
		
		protected void onTouchUp() {
			bg.brightness( 1.0f );
		};
		
		@Override
		protected void onClick() {
            if(holdOnly)
                GameScene.selectItem(this, WndBag.Mode.BRUTE_HOLD, "Ask Brute To Hold");
            else if(item instanceof Bow || item.image() == ItemSpriteSheet.EMPTY_BOW)
                GameScene.selectItem(this, WndBag.Mode.BOW, "Equip Bow On Merc");
            else  if(item instanceof Weapon || item.image() == ItemSpriteSheet.WEAPON)
                GameScene.selectItem(this, WndBag.Mode.WEAPON, "Equip Weapon On Merc");
            else  if(item instanceof Armor || item.image() == ItemSpriteSheet.ARMOR)
                GameScene.selectItem(this, WndBag.Mode.ARMOR, "Equip Armor On Merc");
            else  if(item instanceof PotionOfHealing || item.image() == ItemSpriteSheet.POTION_PLACEHOLDER)
                GameScene.selectItem(this, WndBag.Mode.HEALING_POTION, "Give Potion Of Healing");
		}
		
		@Override
		protected boolean onLongClick() {
            //GameScene.selectItem(this, (item instanceof Weapon || item.image() == ItemSpriteSheet.WEAPON) ? WndBag.Mode.WEAPON : WndBag.Mode.ARMOR, "Equip On Merc");
            WndMerc.this.hide();
            if(holdOnly || item instanceof PotionOfHealing)
            {
                Dungeon.hero.hiredMerc.unEquipItem();
                return true;
            }
            if(item instanceof Weapon)
            {
                Dungeon.hero.hiredMerc.unEquipWeapon();
            }
            else
            {
                Dungeon.hero.hiredMerc.unEquipArmor();
            }
            return true;
		}

        @Override
        public void onSelect( Item item ) {
            if (item != null) {
                if (item instanceof Weapon && holdOnly == false) {
                    if (Dungeon.hero.belongings.weapon == item) {
                        Dungeon.hero.belongings.weapon = null;
                    } else {
                        item.detach(Dungeon.hero.belongings.backpack);
                    }
                    Dungeon.hero.hiredMerc.equipWeapon(item);
                } else if (item instanceof Armor && holdOnly == false) {
                    if (Dungeon.hero.belongings.armor == item) {
                        Dungeon.hero.belongings.armor = null;
                    } else {
                        item.detach(Dungeon.hero.belongings.backpack);
                    }
                    Dungeon.hero.hiredMerc.equipArmor(item);
                }  else if(item instanceof PotionOfHealing) {
                    item.detach(Dungeon.hero.belongings.backpack);

                    Dungeon.hero.hiredMerc.equipItem(new PotionOfHealing());
                }
                else
                {
                    Dungeon.hero.hiredMerc.equipItem(item.detachAll(Dungeon.hero.belongings.backpack ));
                }
            }
            ((HeroSprite)Dungeon.hero.sprite).updateArmor();
            ((MercSprite)Dungeon.hero.hiredMerc.sprite).updateArmor();
            Dungeon.hero.spend(1f);
            WndMerc.this.hide();
        }
	}

    private class SkillButton extends SkillSlot {

        private static final int NORMAL		= 0xFF4A4D44;
        private static final int EQUIPPED	= 0xFF63665B;


        private Skill skill;
        private ColorBlock bg;

        private ColorBlock durability[];

        public SkillButton(Skill skill) {

            super( skill );

            this.skill = skill;


            width = height = SLOT_SIZE;

            durability = new ColorBlock[Skill.MAX_LEVEL];

            if(skill != null && skill.name != null && skill.level > 0 && skill.level <= Skill.MAX_LEVEL) {
                for (int i = 0; i < skill.level; i++) {
                    durability[i] = new ColorBlock(2, 2, 0xFF00EE00);
                    add(durability[i]);
                }
                for (int i = skill.level; i < Skill.MAX_LEVEL; i++) {
                    durability[i] = new ColorBlock(2, 2, 0x4000EE00);
                    add(durability[i]);
                }
            }

            if(skill instanceof BranchSkill)
                bg.visible = false;
        }

        @Override
        protected void createChildren() {
            bg = new ColorBlock( SLOT_SIZE, SLOT_SIZE, NORMAL );
            add( bg );

            super.createChildren();
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;


            if(skill != null && skill.name != null && skill.level > 0 && skill.level <= Skill.MAX_LEVEL) {
                for (int i = 0; i < Skill.MAX_LEVEL; i++) {
                    durability[i].x = x + width - 9 + i * 3;
                    durability[i].y = y + 3;

                }
            }

            super.layout();
        }



        @Override
        protected void onTouchDown() {
            bg.brightness( 1.5f );
            Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
        };

        protected void onTouchUp() {
            bg.brightness( 1.0f );
        };

        @Override
        protected void onClick() {
            if (listener != null) {

                hide();
                //listener.onSelect(skill);

            } else {

                GameScene.show( ( new WndSkill( null, skill ) ));

            }
        }

        @Override
        protected boolean onLongClick() {
            GameScene.show( ( new WndSkill( null, skill ) ));return true;
        }
    }

	public interface Listener {
		void onSelect(Item item);
	}
}
