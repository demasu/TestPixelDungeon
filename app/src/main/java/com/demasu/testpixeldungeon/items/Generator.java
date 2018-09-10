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
package com.demasu.testpixeldungeon.items;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.items.armor.Armor;
import com.demasu.testpixeldungeon.items.armor.ClothArmor;
import com.demasu.testpixeldungeon.items.armor.LeatherArmor;
import com.demasu.testpixeldungeon.items.armor.MailArmor;
import com.demasu.testpixeldungeon.items.armor.PlateArmor;
import com.demasu.testpixeldungeon.items.armor.ScaleArmor;
import com.demasu.testpixeldungeon.items.bags.Bag;
import com.demasu.testpixeldungeon.items.food.Food;
import com.demasu.testpixeldungeon.items.food.MysteryMeat;
import com.demasu.testpixeldungeon.items.food.Pasty;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.items.potions.PotionOfExperience;
import com.demasu.testpixeldungeon.items.potions.PotionOfFrost;
import com.demasu.testpixeldungeon.items.potions.PotionOfHealing;
import com.demasu.testpixeldungeon.items.potions.PotionOfInvisibility;
import com.demasu.testpixeldungeon.items.potions.PotionOfLevitation;
import com.demasu.testpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.demasu.testpixeldungeon.items.potions.PotionOfMana;
import com.demasu.testpixeldungeon.items.potions.PotionOfMight;
import com.demasu.testpixeldungeon.items.potions.PotionOfMindVision;
import com.demasu.testpixeldungeon.items.potions.PotionOfParalyticGas;
import com.demasu.testpixeldungeon.items.potions.PotionOfPurity;
import com.demasu.testpixeldungeon.items.potions.PotionOfStrength;
import com.demasu.testpixeldungeon.items.potions.PotionOfToxicGas;
import com.demasu.testpixeldungeon.items.rings.Ring;
import com.demasu.testpixeldungeon.items.rings.RingOfAccuracy;
import com.demasu.testpixeldungeon.items.rings.RingOfDetection;
import com.demasu.testpixeldungeon.items.rings.RingOfElements;
import com.demasu.testpixeldungeon.items.rings.RingOfEvasion;
import com.demasu.testpixeldungeon.items.rings.RingOfHaggler;
import com.demasu.testpixeldungeon.items.rings.RingOfHaste;
import com.demasu.testpixeldungeon.items.rings.RingOfHerbalism;
import com.demasu.testpixeldungeon.items.rings.RingOfMending;
import com.demasu.testpixeldungeon.items.rings.RingOfPower;
import com.demasu.testpixeldungeon.items.rings.RingOfSatiety;
import com.demasu.testpixeldungeon.items.rings.RingOfShadows;
import com.demasu.testpixeldungeon.items.rings.RingOfThorns;
import com.demasu.testpixeldungeon.items.scrolls.Scroll;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfBloodyRitual;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfChallenge;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfHome;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfSacrifice;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfSkill;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfTerror;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.demasu.testpixeldungeon.items.wands.Wand;
import com.demasu.testpixeldungeon.items.wands.WandOfAmok;
import com.demasu.testpixeldungeon.items.wands.WandOfAvalanche;
import com.demasu.testpixeldungeon.items.wands.WandOfBlink;
import com.demasu.testpixeldungeon.items.wands.WandOfDisintegration;
import com.demasu.testpixeldungeon.items.wands.WandOfFirebolt;
import com.demasu.testpixeldungeon.items.wands.WandOfFlock;
import com.demasu.testpixeldungeon.items.wands.WandOfLightning;
import com.demasu.testpixeldungeon.items.wands.WandOfMagicMissile;
import com.demasu.testpixeldungeon.items.wands.WandOfPoison;
import com.demasu.testpixeldungeon.items.wands.WandOfReach;
import com.demasu.testpixeldungeon.items.wands.WandOfRegrowth;
import com.demasu.testpixeldungeon.items.wands.WandOfSlowness;
import com.demasu.testpixeldungeon.items.wands.WandOfTeleportation;
import com.demasu.testpixeldungeon.items.weapon.Weapon;
import com.demasu.testpixeldungeon.items.weapon.melee.BattleAxe;
import com.demasu.testpixeldungeon.items.weapon.melee.Dagger;
import com.demasu.testpixeldungeon.items.weapon.melee.DualSwords;
import com.demasu.testpixeldungeon.items.weapon.melee.Glaive;
import com.demasu.testpixeldungeon.items.weapon.melee.Knuckles;
import com.demasu.testpixeldungeon.items.weapon.melee.Longsword;
import com.demasu.testpixeldungeon.items.weapon.melee.Mace;
import com.demasu.testpixeldungeon.items.weapon.melee.NecroBlade;
import com.demasu.testpixeldungeon.items.weapon.melee.Quarterstaff;
import com.demasu.testpixeldungeon.items.weapon.melee.ShortSword;
import com.demasu.testpixeldungeon.items.weapon.melee.Spear;
import com.demasu.testpixeldungeon.items.weapon.melee.Sword;
import com.demasu.testpixeldungeon.items.weapon.melee.WarHammer;
import com.demasu.testpixeldungeon.items.weapon.missiles.Arrow;
import com.demasu.testpixeldungeon.items.weapon.missiles.BombArrow;
import com.demasu.testpixeldungeon.items.weapon.missiles.Boomerang;
import com.demasu.testpixeldungeon.items.weapon.missiles.Bow;
import com.demasu.testpixeldungeon.items.weapon.missiles.CurareDart;
import com.demasu.testpixeldungeon.items.weapon.missiles.Dart;
import com.demasu.testpixeldungeon.items.weapon.missiles.FlameBow;
import com.demasu.testpixeldungeon.items.weapon.missiles.FrostBow;
import com.demasu.testpixeldungeon.items.weapon.missiles.IncendiaryDart;
import com.demasu.testpixeldungeon.items.weapon.missiles.Javelin;
import com.demasu.testpixeldungeon.items.weapon.missiles.Shuriken;
import com.demasu.testpixeldungeon.items.weapon.missiles.Tamahawk;
import com.demasu.testpixeldungeon.plants.Dreamweed;
import com.demasu.testpixeldungeon.plants.Earthroot;
import com.demasu.testpixeldungeon.plants.Fadeleaf;
import com.demasu.testpixeldungeon.plants.Firebloom;
import com.demasu.testpixeldungeon.plants.Icecap;
import com.demasu.testpixeldungeon.plants.Plant;
import com.demasu.testpixeldungeon.plants.Rotberry;
import com.demasu.testpixeldungeon.plants.Sorrowmoss;
import com.demasu.testpixeldungeon.plants.Sungrass;
import com.watabou.utils.Random;

import java.util.HashMap;

public class Generator {

    private static HashMap<Category, Float> categoryProbs = new HashMap<Generator.Category, Float>();

    static {

        Category.GOLD.classes = new Class<?>[] {
                Gold.class };
        Category.GOLD.probs = new float[] { 1 };

        Category.SCROLL.classes = new Class<?>[] {
                ScrollOfIdentify.class,
                ScrollOfTeleportation.class,
                ScrollOfRemoveCurse.class,
                ScrollOfRecharging.class,
                ScrollOfMagicMapping.class,
                ScrollOfChallenge.class,
                ScrollOfTerror.class,
                ScrollOfLullaby.class,
                ScrollOfPsionicBlast.class,
                ScrollOfMirrorImage.class,
                ScrollOfUpgrade.class,
                ScrollOfEnchantment.class,
                ScrollOfHome.class,
                ScrollOfSacrifice.class,
                ScrollOfBloodyRitual.class,
                ScrollOfSkill.class };
        Category.SCROLL.probs = new float[] { 30, 10, 15, 10, 15, 12, 8, 8, 4, 6, 0, 1, 10, 1, 5, 10 };

        Category.POTION.classes = new Class<?>[] {
                PotionOfHealing.class,
                PotionOfExperience.class,
                PotionOfToxicGas.class,
                PotionOfParalyticGas.class,
                PotionOfLiquidFlame.class,
                PotionOfLevitation.class,
                PotionOfStrength.class,
                PotionOfMindVision.class,
                PotionOfPurity.class,
                PotionOfInvisibility.class,
                PotionOfMight.class,
                PotionOfFrost.class,
                PotionOfMana.class };
        Category.POTION.probs = new float[] { 45, 4, 15, 10, 15, 10, 0, 20, 12, 10, 0, 10, 20 };

        Category.WAND.classes = new Class<?>[] {
                WandOfTeleportation.class,
                WandOfSlowness.class,
                WandOfFirebolt.class,
                WandOfRegrowth.class,
                WandOfPoison.class,
                WandOfBlink.class,
                WandOfLightning.class,
                WandOfAmok.class,
                WandOfReach.class,
                WandOfFlock.class,
                WandOfMagicMissile.class,
                WandOfDisintegration.class,
                WandOfAvalanche.class };
        Category.WAND.probs = new float[] { 10, 10, 15, 6, 10, 11, 15, 10, 6, 10, 0, 5, 5 };

        Category.WEAPON.classes = new Class<?>[] {
                Dagger.class,
                Knuckles.class,
                Quarterstaff.class,
                Spear.class,
                Mace.class,
                Sword.class,
                Longsword.class,
                BattleAxe.class,
                WarHammer.class,
                Glaive.class,
                ShortSword.class,
                Dart.class,
                Javelin.class,
                IncendiaryDart.class,
                CurareDart.class,
                Shuriken.class,
                Boomerang.class,
                Tamahawk.class,
                Bow.class,
                FrostBow.class,
                FlameBow.class,
                Arrow.class,
                BombArrow.class,
                NecroBlade.class,
                DualSwords.class
        };
        Category.WEAPON.probs = new float[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 3, 1, 3, 3 };

        Category.ARMOR.classes = new Class<?>[] {
                ClothArmor.class,
                LeatherArmor.class,
                MailArmor.class,
                ScaleArmor.class,
                PlateArmor.class };
        Category.ARMOR.probs = new float[] { 1, 1, 1, 1, 1 };

        Category.FOOD.classes = new Class<?>[] {
                Food.class,
                Pasty.class,
                MysteryMeat.class };
        Category.FOOD.probs = new float[] { 4, 1, 0 };

        Category.RING.classes = new Class<?>[] {
                RingOfMending.class,
                RingOfDetection.class,
                RingOfShadows.class,
                RingOfPower.class,
                RingOfHerbalism.class,
                RingOfAccuracy.class,
                RingOfEvasion.class,
                RingOfSatiety.class,
                RingOfHaste.class,
                RingOfElements.class,
                RingOfHaggler.class,
                RingOfThorns.class };
        Category.RING.probs = new float[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 };

        Category.SEED.classes = new Class<?>[] {
                Firebloom.Seed.class,
                Icecap.Seed.class,
                Sorrowmoss.Seed.class,
                Dreamweed.Seed.class,
                Sungrass.Seed.class,
                Earthroot.Seed.class,
                Fadeleaf.Seed.class,
                Rotberry.Seed.class };
        Category.SEED.probs = new float[] { 1, 1, 1, 1, 1, 1, 1, 0 };

        Category.MISC.classes = new Class<?>[] {
                Bomb.class,
                Honeypot.class };
        Category.MISC.probs = new float[] { 2, 1 };
    }

    public static void reset () {
        for ( Category cat : Category.values() ) {
            categoryProbs.put( cat, cat.prob );
        }
    }

    public static Item random () {
        return random( Random.chances( categoryProbs ) );
    }

    public static Item random ( Category cat ) {
        try {

            categoryProbs.put( cat, categoryProbs.get( cat ) / 2 );

            switch ( cat ) {
                case ARMOR:
                    return randomArmor();
                case WEAPON:
                    return randomWeapon();
                default:
                    return ( (Item) cat.classes[Random.chances( cat.probs )].newInstance() ).random();
            }

        } catch ( Exception e ) {

            return null;

        }
    }

    public static Item random ( Class<? extends Item> cl ) {
        try {

            return ( (Item) cl.newInstance() ).random();

        } catch ( Exception e ) {

            return null;

        }
    }

    public static Armor randomArmor () throws Exception {

        int curStr = Hero.STARTING_STR + Dungeon.getPotionOfStrength();

        Category cat = Category.ARMOR;

        Armor a1 = (Armor) cat.classes[Random.chances( cat.probs )].newInstance();
        Armor a2 = (Armor) cat.classes[Random.chances( cat.probs )].newInstance();

        a1.random();
        a2.random();

        return Math.abs( curStr - a1.STR ) < Math.abs( curStr - a2.STR ) ? a1 : a2;
    }

    public static Weapon randomWeapon () throws Exception {

        int curStr = Hero.STARTING_STR + Dungeon.getPotionOfStrength();

        Category cat = Category.WEAPON;

        Weapon w1 = (Weapon) cat.classes[Random.chances( cat.probs )].newInstance();
        Weapon w2 = (Weapon) cat.classes[Random.chances( cat.probs )].newInstance();

        w1.random();
        w2.random();

        return Math.abs( curStr - w1.STR ) < Math.abs( curStr - w2.STR ) ? w1 : w2;
    }

    public enum Category {
        WEAPON( 15, Weapon.class ),
        ARMOR( 10, Armor.class ),
        POTION( 50, Potion.class ),
        SCROLL( 40, Scroll.class ),
        WAND( 4, Wand.class ),
        RING( 2, Ring.class ),
        SEED( 5, Plant.Seed.class ),
        FOOD( 0, Food.class ),
        GOLD( 50, Gold.class ),
        MISC( 5, Item.class );

        public Class<?>[] classes;
        public float[] probs;

        public float prob;
        public Class<? extends Item> superClass;

        Category ( float prob, Class<? extends Item> superClass ) {
            this.prob = prob;
            this.superClass = superClass;
        }

        public static int order ( Item item ) {
            for ( int i = 0; i < values().length; i++ ) {
                if ( values()[i].superClass.isInstance( item ) ) {
                    return i;
                }
            }

            return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
        }
    }
}
