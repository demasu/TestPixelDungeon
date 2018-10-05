/*
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

package com.watabou.noosa;

import android.graphics.RectF;

public class MovieClip extends Image {

    public boolean paused = false;
    public Listener listener;
    protected Animation curAnim;
    protected int curFrame;
    private float frameTimer;
    private boolean finished;

    public MovieClip () {
        super();
    }

    public MovieClip ( Object tx ) {
        super( tx );
    }

    @Override
    public void update () {
        super.update();
        if ( !paused ) {
            updateAnimation();
        }
    }

    private void updateAnimation () {
        if ( curAnim != null && curAnim.delay > 0 && ( curAnim.looped || !finished ) ) {

            int lastFrame = curFrame;

            frameTimer += Game.getElapsed();
            while ( frameTimer > curAnim.delay ) {
                frameTimer -= curAnim.delay;
                if ( curFrame == curAnim.frames.length - 1 ) {
                    if ( curAnim.looped ) {
                        curFrame = 0;
                    }
                    finished = true;
                    if ( listener != null ) {
                        listener.onComplete( curAnim );
                        // This check can probably be removed
                        if ( curAnim == null ) {
                            return;
                        }
                    }

                } else {
                    curFrame++;
                }
            }

            if ( curFrame != lastFrame ) {
                frame( curAnim.frames[curFrame] );
            }

        }
    }

    public void play ( Animation anim ) {
        play( anim, false );
    }

    public void play ( Animation anim, boolean force ) {

        if ( !force && ( curAnim != null ) && ( curAnim == anim ) && ( curAnim.looped || !finished ) ) {
            return;
        }

        curAnim = anim;
        curFrame = 0;
        finished = false;

        frameTimer = 0;

        if ( anim != null ) {
            frame( anim.frames[curFrame] );
        }
    }

    public interface Listener {
        void onComplete ( Animation anim );
    }

    public static class Animation {

        public float delay;
        public RectF[] frames;
        final boolean looped;

        public Animation ( int fps, boolean looped ) {
            this.delay = 1f / fps;
            this.looped = looped;
        }

        Animation frames ( RectF... frames ) {
            this.frames = frames;
            return this;
        }

        public Animation frames ( TextureFilm film, Object... frames ) {
            this.frames = new RectF[frames.length];
            for ( int i = 0; i < frames.length; i++ ) {
                this.frames[i] = film.get( frames[i] );
            }
            return this;
        }

        @SuppressWarnings ( { "MethodDoesntCallSuperMethod", "override" } )
        public Animation clone () {
            return new Animation( Math.round( 1 / delay ), looped ).frames( frames );
        }
    }
}
