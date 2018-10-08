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

import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.ui.BuffIndicator;
import com.demasu.testpixeldungeon.ui.HealthBar;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends WndTitledMessage {

    public WndInfoMob ( Mob mob ) {

        super( new MobTitle( mob ), desc( mob ) );

    }

    private static String desc ( Mob mob ) {

        StringBuilder builder = new StringBuilder( mob.description() );

        builder.append( "\n\n" + mob.state.status() + "." );

        return builder.toString();
    }

    private static class MobTitle extends Component {

        private static final int GAP = 2;

        private CharSprite image;
        private BitmapText name;
        private HealthBar health;
        private BuffIndicator buffs;

        public MobTitle ( Mob mob ) {

            name = PixelScene.createText( Utils.capitalize( mob.name ), 9 );
            name.hardlight( TITLE_COLOR );
            name.measure();
            add( name );

            image = mob.sprite();
            add( image );

            health = new HealthBar();
            health.level( (float) mob.getHP() / mob.getHT() );
            add( health );

            buffs = new BuffIndicator( mob );
            add( buffs );
        }

        @Override
        protected void layout () {

            image.setX( 0 );
            image.setY( Math.max( 0, name.height() + GAP + health.height() - image.getHeight() ) );

            name.setX( image.getWidth() + GAP );
            name.setY( image.getHeight() - health.height() - GAP - name.baseLine() );

            float w = getWidth() - image.getWidth() - GAP;

            health.setRect( image.getWidth() + GAP, image.getHeight() - health.height(), w, health.height() );

            buffs.setPos(
                    name.getX() + name.width() + GAP,
                    name.getY() + name.baseLine() - BuffIndicator.SIZE );

            setHeight( health.bottom() );
        }
    }
}
