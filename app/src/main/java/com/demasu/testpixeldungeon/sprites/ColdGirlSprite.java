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
package com.demasu.testpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.actors.mobs.ColdGirl;
import com.demasu.testpixeldungeon.effects.ArcherMaidenHalo;
import com.demasu.testpixeldungeon.scenes.GameScene;

public class ColdGirlSprite extends MobSprite {

    protected static final int FRAME_WIDTH = 12;
    protected static final int FRAME_HEIGHT = 15;

    protected static final int RUN_FRAMERATE = 20;

    public ArcherMaidenHalo halo = null;
    public boolean hasHalo = false;

    public boolean isSister = false;


    public ColdGirlSprite () {
        super();


        if ( isSister ) {
            texture( Assets.COLD_GIRL_SISTER );
        } else {
            texture( Assets.COLD_GIRL );
        }

        TextureFilm frames = new TextureFilm( texture, FRAME_WIDTH, FRAME_HEIGHT );


        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new Animation( RUN_FRAMERATE, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        die = new Animation( 20, false );
        die.frames( frames, 8, 9, 10, 11, 12, 11 );

        attack = new Animation( 15, false );
        attack.frames( frames, 13, 14, 15, 0 );

        zap = attack.clone();

        play( idle );
    }

    public void turnToSis () {
        isSister = true;

        texture( Assets.COLD_GIRL_SISTER );
        TextureFilm frames = new TextureFilm( texture, FRAME_WIDTH, FRAME_HEIGHT );


        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new Animation( RUN_FRAMERATE, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        die = new Animation( 20, false );
        die.frames( frames, 8, 9, 10, 11, 12, 11 );

        attack = new Animation( 15, false );
        attack.frames( frames, 13, 14, 15, 0 );

        zap = attack.clone();

        play( idle );
    }

    public void haloUp () {
        if ( hasHalo ) {
            return;
        }

        hasHalo = true;
        add( State.ARCHERMAIDEN );
        GameScene.effect( halo = new ArcherMaidenHalo( this ) );
    }
}
