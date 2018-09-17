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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.demasu.testpixeldungeon.VersionNewsInfo;
import com.watabou.glscripts.Script;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.Keys;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BitmapCache;
import com.watabou.utils.SystemTime;

import java.util.ArrayList;
import java.util.Objects;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@SuppressLint ( "Registered" )
public class Game extends Activity implements GLSurfaceView.Renderer, View.OnTouchListener {

    @SuppressLint ( "StaticFieldLeak" )
    public static Game instance;

    // Actual size of the screen
    public static int width;
    public static int height;

    // Density: mdpi=1, hdpi=1.5, xhdpi=2...
    public static float density = 1;

    public static String version;
    public static int versionBuild;
    public static String vanillaVersion = "Vanilla PD v 1.9.2a";
    public static float timeScale = 1f;
    public static float elapsed = 0f;
    // Current scene
    private Scene scene;
    // New scene we are going to switch to
    protected Scene requestedScene;
    // true if scene switch is requested
    private boolean requestedReset = true;
    // New scene class
    private Class<? extends Scene> sceneClass;
    // Current time in milliseconds
    protected long now;
    // Milliseconds passed since previous update
    protected long step;
    protected GLSurfaceView view;
    protected SurfaceHolder holder;

    // Accumulated touch events
    protected final ArrayList<MotionEvent> motionEvents = new ArrayList<MotionEvent>();

    // Accumulated key events
    protected final ArrayList<KeyEvent> keysEvents = new ArrayList<KeyEvent>();

    public Game ( Class<? extends Scene> c ) {
        super();
        setSceneClass( c );
    }

    public static void resetScene () {
        switchScene( instance.getSceneClass() );
    }

    public static void switchScene ( Class<? extends Scene> c ) {
        instance.setSceneClass( c );
        instance.setRequestedReset( true );
    }

    public static Scene scene () {
        return instance.getScene();
    }

    public static void vibrate ( int milliseconds ) {
        ( (Vibrator) Objects.requireNonNull( instance.getSystemService( VIBRATOR_SERVICE ) ) ).vibrate( milliseconds );
    }

    @SuppressLint ( "ClickableViewAccessibility" )
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        BitmapCache.context = TextureCache.context = instance = this;

        DisplayMetrics m = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( m );
        density = m.density;

        try {
            version = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionName;
            versionBuild = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionCode;
        } catch ( NameNotFoundException e ) {
            version = "???";
            versionBuild = 0;
        }

        VersionNewsInfo.alreadySeen = false; // Static variables tend to stick between games

        setVolumeControlStream( AudioManager.STREAM_MUSIC );


        view = new GLSurfaceView( this );
        view.setEGLContextClientVersion( 2 );
        view.setEGLConfigChooser( false );
        // Addresses java.lang.IllegalArgumentException: No config chosen
        view.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
        view.setRenderer( this );
        view.setOnTouchListener( this );
        setContentView( view );
    }

    @Override
    public void onResume () {
        super.onResume();

        now = 0;
        view.onResume();

        Music.INSTANCE.resume();
        Sample.INSTANCE.resume();
    }

    @Override
    public void onPause () {
        super.onPause();

        if ( getScene() != null ) {
            getScene().pause();
        }

        view.onPause();
        Script.reset();

        Music.INSTANCE.pause();
        Sample.INSTANCE.pause();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        destroyGame();

        Music.INSTANCE.mute();
        Sample.INSTANCE.reset();
    }

    @SuppressLint ( { "Recycle", "ClickableViewAccessibility" } )
    @Override
    public boolean onTouch ( View view, MotionEvent event ) {
        synchronized (motionEvents) {
            motionEvents.add( MotionEvent.obtain( event ) );
        }
        return true;
    }

    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event ) {

        if ( keyCode == Keys.VOLUME_DOWN ||
                keyCode == Keys.VOLUME_UP ) {

            return false;
        }

        synchronized (motionEvents) {
            keysEvents.add( event );
        }
        return true;
    }

    @Override
    public boolean onKeyUp ( int keyCode, KeyEvent event ) {

        if ( keyCode == Keys.VOLUME_DOWN ||
                keyCode == Keys.VOLUME_UP ) {

            return false;
        }

        synchronized (motionEvents) {
            keysEvents.add( event );
        }
        return true;
    }

    @Override
    public void onDrawFrame ( GL10 gl ) {

        if ( width == 0 || height == 0 ) {
            return;
        }

        SystemTime.tick();
        long rightNow = SystemTime.now;
        step = ( now == 0 ? 0 : rightNow - now );
        now = rightNow;

        step();

        NoosaScript.get().resetCamera();
        GLES20.glScissor( 0, 0, width, height );
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );
        draw();
    }

    @Override
    public void onSurfaceChanged ( GL10 gl, int width, int height ) {

        GLES20.glViewport( 0, 0, width, height );

        Game.width = width;
        Game.height = height;

    }

    @Override
    public void onSurfaceCreated ( GL10 gl, EGLConfig config ) {
        GLES20.glEnable( GL10.GL_BLEND );
        // For premultiplied alpha:
        // GLES20.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
        GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );

        GLES20.glEnable( GL10.GL_SCISSOR_TEST );

        TextureCache.reload();
    }

    protected void destroyGame () {
        if ( getScene() != null ) {
            getScene().destroy();
            setScene( null );
        }

        instance = null;
    }

    protected void step () {

        if ( isRequestedReset() ) {
            setRequestedReset( false );
            try {
                requestedScene = getSceneClass().newInstance();
                switchScene();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        update();
    }

    protected void draw () {
        getScene().draw();
    }

    protected void switchScene () {

        Camera.reset();

        if ( getScene() != null ) {
            getScene().destroy();
        }
        setScene( requestedScene );
        getScene().create();

        Game.elapsed = 0f;
        Game.timeScale = 1f;
    }

    protected void update () {
        Game.elapsed = Game.timeScale * step * 0.001f;

        synchronized (motionEvents) {
            Touchscreen.processTouchEvents( motionEvents );
            motionEvents.clear();
        }
        synchronized (keysEvents) {
            Keys.processTouchEvents( keysEvents );
            keysEvents.clear();
        }

        getScene().update();
        Camera.updateAll();
    }

    public Class<? extends Scene> getSceneClass () {
        return sceneClass;
    }

    public void setSceneClass ( Class<? extends Scene> sceneClass ) {
        this.sceneClass = sceneClass;
    }

    public boolean isRequestedReset () {
        return requestedReset;
    }

    public void setRequestedReset ( boolean requestedReset ) {
        this.requestedReset = requestedReset;
    }

    public Scene getScene () {
        return scene;
    }

    public void setScene ( Scene scene ) {
        this.scene = scene;
    }
}
