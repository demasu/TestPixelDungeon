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
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.Statistics;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.ui.BuffIndicator;
import com.demasu.testpixeldungeon.ui.RedButton;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

import java.util.Locale;

public class WndHero extends WndTabbed {

    private static final String TXT_STATS = "Stats";
    private static final String TXT_BUFFS = "Buffs";

    private static final String TXT_EXP = "Experience";
    private static final String TXT_STR = "Strength";
    private static final String TXT_MANA = "Mana";
    private static final String TXT_HEALTH = "Health";
    private static final String TXT_GOLD = "Gold Collected";
    private static final String TXT_DEPTH = "Maximum Depth";


    private static final String TXT_Difficulty = "Difficulty";

    private static final int WIDTH = 100;
    private static final int TAB_WIDTH = 40;

    private StatsTab stats;
    private BuffsTab buffs;

    private SmartTexture icons;
    private TextureFilm film;

    public WndHero () {

        super();

        icons = TextureCache.get( Assets.BUFFS_LARGE );
        film = new TextureFilm( icons, 16, 16 );

        stats = new StatsTab();
        add( stats );

        buffs = new BuffsTab();
        add( buffs );

        add( new LabeledTab( TXT_STATS ) {
            protected void select ( boolean value ) {
                super.select( value );
                stats.setVisible( selected );
                stats.setActive( selected );
            }

        } );
        add( new LabeledTab( TXT_BUFFS ) {
            protected void select ( boolean value ) {
                super.select( value );
                buffs.setVisible( selected );
                buffs.setActive( selected );
            }

        } );
        for ( Tab tab : tabs ) {
            tab.setSize( TAB_WIDTH, tabHeight() );
        }

        resize( WIDTH, (int) Math.max( stats.height(), buffs.height() ) );

        select( 0 );
    }

    private class StatsTab extends Group {

        private static final String TXT_TITLE = "Level %d %s";
        private static final String TXT_CATALOGUS = "Catalogus";
        private static final String TXT_JOURNAL = "Journal";

        private static final int GAP = 5;

        private float pos;

        public StatsTab () {

            Hero hero = Dungeon.getHero();

            BitmapText title = PixelScene.createText(
                    Utils.format( TXT_TITLE, hero.getLvl(), hero.className() ).toUpperCase( Locale.ENGLISH ), 9 );
            title.hardlight( TITLE_COLOR );
            title.measure();
            add( title );

            RedButton btnCatalogus = new RedButton( TXT_CATALOGUS ) {
                @Override
                protected void onClick () {
                    hide();
                    GameScene.show( new WndCatalogus() );
                }
            };
            btnCatalogus.setRect( 0, title.y + title.height(), btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
            add( btnCatalogus );

            RedButton btnJournal = new RedButton( TXT_JOURNAL ) {
                @Override
                protected void onClick () {
                    hide();
                    GameScene.show( new WndJournal() );
                }
            };
            btnJournal.setRect(
                    btnCatalogus.right() + 1, btnCatalogus.top(),
                    btnJournal.reqWidth() + 2, btnJournal.reqHeight() + 2 );
            add( btnJournal );

            pos = btnCatalogus.bottom() + GAP;

            statSlot( TXT_STR, hero.STR() );
            statSlot( TXT_HEALTH, hero.getHP() + "/" + hero.getHT() );
            statSlot( TXT_MANA, hero.getMP() + "/" + hero.getMMP() );
            statSlot( TXT_EXP, hero.exp + "/" + hero.maxExp() );

            pos += GAP;

            statSlot( TXT_Difficulty, Dungeon.getCurrentDifficulty().title() );

            statSlot( TXT_GOLD, Statistics.goldCollected );
            statSlot( TXT_DEPTH, Statistics.deepestFloor );

            pos += GAP;
        }

        private void statSlot ( String label, String value ) {

            BitmapText txt = PixelScene.createText( label, 8 );
            txt.y = pos;
            add( txt );

            txt = PixelScene.createText( value, 8 );
            txt.measure();
            txt.x = PixelScene.align( WIDTH * 0.65f );
            txt.y = pos;
            add( txt );

            pos += GAP + txt.baseLine();
        }

        private void statSlot ( String label, int value ) {
            statSlot( label, Integer.toString( value ) );
        }

        public float height () {
            return pos;
        }
    }

    private class BuffsTab extends Group {

        private static final int GAP = 2;

        private float pos;

        public BuffsTab () {
            for ( Buff buff : Dungeon.getHero().buffs() ) {
                buffSlot( buff );
            }
        }

        private void buffSlot ( Buff buff ) {

            int index = buff.icon();

            if ( index != BuffIndicator.NONE ) {

                Image icon = new Image( icons );
                icon.frame( film.get( index ) );
                icon.y = pos;
                add( icon );

                BitmapText txt = PixelScene.createText( buff.toString(), 8 );
                txt.x = icon.width + GAP;
                txt.y = pos + (int) ( icon.height - txt.baseLine() ) / 2;
                add( txt );

                pos += GAP + icon.height;
            }
        }

        public float height () {
            return pos;
        }
    }
}
