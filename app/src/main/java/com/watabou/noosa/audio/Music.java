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

package com.watabou.noosa.audio;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

import com.watabou.noosa.Game;

import java.io.IOException;
import java.util.Objects;

public enum Music implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    INSTANCE;

    private MediaPlayer player;

    @Nullable
    private String lastPlayed;
    private boolean lastLooping;

    private boolean enabled = true;

    public void play ( String assetName, boolean looping ) {

        if ( isPlaying() && Objects.equals( lastPlayed, assetName ) ) {
            return;
        }

        stop();

        lastPlayed = assetName;
        lastLooping = looping;

        if ( !enabled || assetName == null ) {
            return;
        }

        try {

            AssetFileDescriptor afd = Game.getInstance().getAssets().openFd( assetName );

            player = new MediaPlayer();
            player.setAudioStreamType( AudioManager.STREAM_MUSIC );
            player.setDataSource( afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength() );
            player.setOnPreparedListener( this );
            player.setOnErrorListener( this );
            player.setLooping( looping );
            player.prepareAsync();

        } catch ( IOException e ) {

            player.release();
        }
    }

    public void mute () {
        lastPlayed = null;
        stop();
    }

    @Override
    public void onPrepared ( MediaPlayer player ) {
        player.start();
    }

    @Override
    public boolean onError ( MediaPlayer mp, int what, int extra ) {
        if ( player != null ) {
            player.release();
        }
        return true;
    }

    public void pause () {
        if ( player != null ) {
            player.pause();
        }
    }

    public void resume () {
        if ( player != null ) {
            player.start();
        }
    }

    private void stop () {
        if ( player != null ) {
            player.stop();
            player.release();
        }
    }

    public void volume ( float value ) {
        if ( player != null ) {
            player.setVolume( value, value );
        }
    }

    private boolean isPlaying () {
        return player != null && player.isPlaying();
    }

    public void enable ( boolean value ) {
        enabled = value;
        if ( isPlaying() && !value ) {
            stop();
        } else if ( !isPlaying() && value ) {
            play( lastPlayed, lastLooping );
        }
    }

}
