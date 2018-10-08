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
import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.Rankings;
import com.demasu.testpixeldungeon.effects.Flare;
import com.demasu.testpixeldungeon.sprites.ItemSprite;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.ui.Archs;
import com.demasu.testpixeldungeon.ui.ExitButton;
import com.demasu.testpixeldungeon.ui.Icons;
import com.demasu.testpixeldungeon.ui.Window;
import com.demasu.testpixeldungeon.windows.WndError;
import com.demasu.testpixeldungeon.windows.WndRanking;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Button;

public class RankingsScene extends PixelScene {

    private static final int DEFAULT_COLOR = 0xCCCCCC;

    private static final String TXT_TITLE = "Top Rankings";
    private static final String TXT_TOTAL = "Games played: ";
    private static final String TXT_NO_GAMES = "No games have been played yet.";

    private static final String TXT_NO_INFO = "No additional information";

    private static final float ROW_HEIGHT_L = 22;
    private static final float ROW_HEIGHT_P = 28;

    private static final float MAX_ROW_WIDTH = 180;

    private static final float GAP = 4;

    private Archs archs;

    @Override
    public void create () {

        super.create();

        Music.INSTANCE.play( Assets.THEME, true );
        Music.INSTANCE.volume( 1f );

        uiCamera.setVisible( false );

        int w = Camera.getMain().getWidth();
        int h = Camera.getMain().getHeight();

        archs = new Archs();
        archs.setSize( w, h );
        add( archs );

        Rankings.INSTANCE.load();

        if ( Rankings.INSTANCE.records.size() > 0 ) {

            float rowHeight = PixelDungeon.landscape() ? ROW_HEIGHT_L : ROW_HEIGHT_P;

            float left = ( w - Math.min( MAX_ROW_WIDTH, w ) ) / 2 + GAP;
            float top = align( ( h - rowHeight * Rankings.INSTANCE.records.size() ) / 2 );

            BitmapText title = PixelScene.createText( TXT_TITLE, 9 );
            title.hardlight( Window.TITLE_COLOR );
            title.measure();
            title.setX( align( ( w - title.width() ) / 2 ) );
            title.setY( align( top - title.height() - GAP ) );
            add( title );

            int pos = 0;

            for ( Rankings.Record rec : Rankings.INSTANCE.records ) {
                Record row = new Record( pos, pos == Rankings.INSTANCE.lastRecord, rec );
                row.setRect( left, top + pos * rowHeight, w - left * 2, rowHeight );
                add( row );

                pos++;
            }

            if ( Rankings.INSTANCE.totalNumber >= Rankings.TABLE_SIZE ) {
                BitmapText label = PixelScene.createText( TXT_TOTAL, 8 );
                label.hardlight( DEFAULT_COLOR );
                label.measure();
                add( label );

                BitmapText won = PixelScene.createText( Integer.toString( Rankings.INSTANCE.wonNumber ), 8 );
                won.hardlight( Window.TITLE_COLOR );
                won.measure();
                add( won );

                BitmapText total = PixelScene.createText( "/" + Rankings.INSTANCE.totalNumber, 8 );
                total.hardlight( DEFAULT_COLOR );
                total.measure();
                total.setX( align( ( w - total.width() ) / 2 ) );
                total.setY( align( top + pos * rowHeight + GAP ) );
                add( total );

                float tw = label.width() + won.width() + total.width();
                label.setX( align( ( w - tw ) / 2 ) );
                won.setX( label.getX() + label.width() );
                total.setX( won.getX() + won.width() );
                label.setY( align( top + pos * rowHeight + GAP ) );
                won.setY( align( top + pos * rowHeight + GAP ) );
                total.setY( align( top + pos * rowHeight + GAP ) );
            }

        } else {

            BitmapText title = PixelScene.createText( TXT_NO_GAMES, 8 );
            title.hardlight( DEFAULT_COLOR );
            title.measure();
            title.setX( align( ( w - title.width() ) / 2 ) );
            title.setY( align( ( h - title.height() ) / 2 ) );
            add( title );

        }

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.getMain().getWidth() - btnExit.width(), 0 );
        add( btnExit );

        fadeIn();
    }

    @Override
    protected void onBackPressed () {
        PixelDungeon.switchNoFade( TitleScene.class );
    }

    public static class Record extends Button {

        private static final float GAP = 4;

        private static final int TEXT_WIN = 0xFFFF88;
        private static final int TEXT_LOSE = 0xCCCCCC;
        private static final int FLARE_WIN = 0x888866;
        private static final int FLARE_LOSE = 0x666666;

        private Rankings.Record rec;

        private ItemSprite shield;
        private Flare flare;
        private BitmapText position;
        private BitmapTextMultiline desc;
        private Image classIcon;

        public Record ( int pos, boolean latest, Rankings.Record rec ) {
            super();

            this.rec = rec;

            if ( latest ) {
                flare = new Flare( 6, 24 );
                flare.setAngularSpeed( 90 );
                flare.color( rec.win ? FLARE_WIN : FLARE_LOSE );
                addToBack( flare );
            }

            position.text( Integer.toString( pos + 1 ) );
            position.measure();

            desc.text( rec.info );
            desc.measure();

            if ( rec.win ) {
                shield.view( ItemSpriteSheet.AMULET, null );
                position.hardlight( TEXT_WIN );
                desc.hardlight( TEXT_WIN );
            } else {
                position.hardlight( TEXT_LOSE );
                desc.hardlight( TEXT_LOSE );
            }

            classIcon.copy( Icons.get( rec.heroClass ) );
        }

        @Override
        protected void createChildren () {

            super.createChildren();

            shield = new ItemSprite( ItemSpriteSheet.TOMB, null );
            add( shield );

            position = new BitmapText( PixelScene.font1x );
            add( position );

            desc = createMultiline( 9 );
            add( desc );

            classIcon = new Image();
            add( classIcon );
        }

        @Override
        protected void layout () {

            super.layout();

            shield.setX( getX() );
            shield.setY( getY() + ( getHeight() - shield.getHeight() ) / 2 );

            position.setX( align( shield.getX() + ( shield.getWidth() - position.width() ) / 2 ) );
            position.setY( align( shield.getY() + ( shield.getHeight() - position.height() ) / 2 + 1 ) );

            if ( flare != null ) {
                flare.point( shield.center() );
            }

            classIcon.setX( align( getX() + getWidth() - classIcon.getWidth() ) );
            classIcon.setY( shield.getY() );

            desc.setX( shield.getX() + shield.getWidth() + GAP );
            desc.setMaxWidth( (int) ( classIcon.getX() - desc.getX() ) );
            desc.measure();
            desc.setY( position.getY() + position.baseLine() - desc.baseLine() );
        }

        @Override
        protected void onClick () {
            if ( rec.gameFile.length() > 0 ) {
                getParent().add( new WndRanking( rec.gameFile ) );
            } else {
                getParent().add( new WndError( TXT_NO_INFO ) );
            }
        }
    }
}
