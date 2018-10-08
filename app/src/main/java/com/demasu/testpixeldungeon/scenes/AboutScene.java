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

import android.content.Intent;
import android.net.Uri;

import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.effects.Flare;
import com.demasu.testpixeldungeon.ui.Archs;
import com.demasu.testpixeldungeon.ui.ExitButton;
import com.demasu.testpixeldungeon.ui.Icons;
import com.demasu.testpixeldungeon.ui.Window;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;

public class AboutScene extends PixelScene {

    private static final String TXTFirst = "SkillFull Pixel Dungeon \n \n"
            + "Code & graphics: BilbolDev\n"
            + "Source code is available on GitHub\n";

    private static final String TXTOther = "Some graphics taken from Nels Dachel & Sarius\n";

    private static final String TXT =
            "Based on Pixel Dungeon \n \n" +
                    "Code & graphics: Watabou\n" +
                    "Music: Cube_Code\n\n";

    private static final String LNK = "pixeldungeon.watabou.ru";
    private static final String LNK_SPD = "https://github.com/bilbolPrime/SPD";
    private static final String LNK_SPD_WIKI = "http://pixeldungeon.wikia.com";

    float GAP = 2;
    float pos = 0f;

    @Override
    public void create () {
        super.create();

        BitmapTextMultiline textfirst = createMultiline( "SkillFull Pixel Dungeon", 8 );
        textfirst.hardlight( Window.TITLE_COLOR );
        textfirst.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        textfirst.measure();
        add( textfirst );

        textfirst.setX( align( ( Camera.getMain().getWidth() - textfirst.width() ) / 2 ) );
        textfirst.setY( align( ( ( Camera.getMain().getHeight() / 2 ) - textfirst.height() ) / 2 ) );

        pos = textfirst.getY() + textfirst.height() + GAP;

        textfirst = createMultiline( "Code & graphics: BilbolDev", 8 );
        textfirst.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        textfirst.measure();
        add( textfirst );

        textfirst.setX( align( ( Camera.getMain().getWidth() - textfirst.width() ) / 2 ) );
        textfirst.setY( pos );

        pos = textfirst.getY() + textfirst.height() + GAP;

        textfirst = createMultiline( "Source code is available on GitHub", 8 );
        textfirst.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        textfirst.measure();
        add( textfirst );

        textfirst.setX( align( ( Camera.getMain().getWidth() - textfirst.width() ) / 2 ) );
        textfirst.setY( pos );

        pos = textfirst.getY() + textfirst.height() + GAP;

        BitmapTextMultiline link_SPD = createMultiline( LNK_SPD, 8 );
        link_SPD.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        link_SPD.measure();
        link_SPD.hardlight( Window.TITLE_COLOR );
        add( link_SPD );


        TouchArea hotArea_SPD = new TouchArea( link_SPD ) {
            @Override
            protected void onClick ( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( LNK_SPD ) );
                Game.getInstance().startActivity( intent );
            }
        };
        add( hotArea_SPD );

        link_SPD.setX( align( ( Camera.getMain().getWidth() - link_SPD.width() ) / 2 ) );
        link_SPD.setY( pos );

        pos = link_SPD.getY() + link_SPD.height() + GAP;

        BitmapTextMultiline link_SPD_Wiki = createMultiline( LNK_SPD_WIKI, 8 );
        link_SPD_Wiki.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        link_SPD_Wiki.measure();
        link_SPD_Wiki.hardlight( Window.TITLE_COLOR );
        add( link_SPD_Wiki );

        link_SPD_Wiki.setX( align( ( Camera.getMain().getWidth() - link_SPD_Wiki.width() ) / 2 ) );
        link_SPD_Wiki.setY( pos );

        pos = link_SPD_Wiki.getY() + link_SPD_Wiki.height() + 4 * GAP;

        TouchArea hotArea_SPD_WIKI = new TouchArea( link_SPD_Wiki ) {
            @Override
            protected void onClick ( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( LNK_SPD_WIKI ) );
                Game.getInstance().startActivity( intent );
            }
        };
        add( hotArea_SPD_WIKI );


        BitmapTextMultiline textOther = createMultiline( "Some ice art: Nels Dachel & Sarius", 8 );
        textOther.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        textOther.measure();
        add( textOther );

        textOther.setX( align( ( Camera.getMain().getWidth() - textOther.width() ) / 2 ) );
        textOther.setY( pos );

        pos = textOther.getY() + textOther.height() + 4 * GAP;

        textOther = createMultiline( "Alternative Sound Track: Jivz & YAPD", 8 );
        textOther.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        textOther.measure();
        add( textOther );

        textOther.setX( align( ( Camera.getMain().getWidth() - textOther.width() ) / 2 ) );
        textOther.setY( pos );

        pos = textOther.getY() + textOther.height() + 4 * GAP;

        BitmapTextMultiline text = createMultiline( "Based on Pixel Dungeon", 8 );
        text.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        text.measure();
        add( text );

        text.setX( align( ( Camera.getMain().getWidth() - text.width() ) / 2 ) );
        text.setY( pos );

        pos = text.getY() + text.height() + GAP;

        text = createMultiline( "Code & graphics: Watabou", 8 );
        text.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        text.measure();
        add( text );

        text.setX( align( ( Camera.getMain().getWidth() - text.width() ) / 2 ) );
        text.setY( pos );

        pos = text.getY() + text.height() + GAP;

        text = createMultiline( "Music: Cube_Code", 8 );
        text.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        text.measure();
        add( text );

        text.setX( align( ( Camera.getMain().getWidth() - text.width() ) / 2 ) );
        text.setY( pos );

        pos = text.getY() + text.height() + GAP;

        BitmapTextMultiline link = createMultiline( LNK, 8 );
        link.setMaxWidth( Math.min( Camera.getMain().getWidth(), 120 ) );
        link.measure();
        link.hardlight( Window.TITLE_COLOR );
        add( link );

        link.setX( align( ( Camera.getMain().getWidth() - link.width() ) / 2 ) );
        link.setY( pos );

        TouchArea hotArea = new TouchArea( link ) {
            @Override
            protected void onClick ( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK ) );
                Game.getInstance().startActivity( intent );
            }
        };
        add( hotArea );

        Image wata = Icons.WATA.get();
        wata.setX( align( ( Camera.getMain().getWidth() - wata.getWidth() ) / 2 ) );
        wata.setY( text.getY() + text.height() + wata.getHeight() + 8 );
        add( wata );

        new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).setAngularSpeed( +20 );

        Archs archs = new Archs();
        archs.setSize( Camera.getMain().getWidth(), Camera.getMain().getHeight() );
        addToBack( archs );

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.getMain().getWidth() - btnExit.width(), 0 );
        add( btnExit );

        fadeIn();
    }

    @Override
    protected void onBackPressed () {
        PixelDungeon.switchNoFade( TitleScene.class );
    }
}
