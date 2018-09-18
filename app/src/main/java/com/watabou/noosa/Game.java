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
    private static Game instance;

    // Actual size of the screen
    private static int width;
    private static int height;

    // Density: mdpi=1, hdpi=1.5, xhdpi=2...
    private static float density = 1;

    private static String version;
    private static int versionBuild;
    private static String vanillaVersion = "Vanilla PD v 1.9.2a";
    private static float timeScale = 1f;
    private static float elapsed = 0f;
    // Current scene
    private Scene scene;
    // New scene we are going to switch to
    private Scene requestedScene;
    // true if scene switch is requested
    private boolean requestedReset = true;
    // New scene class
    private Class<? extends Scene> sceneClass;
    // Current time in milliseconds
    private long now;
    // Milliseconds passed since previous update
    private long step;
    private GLSurfaceView view;
    private SurfaceHolder holder;

    // Accumulated touch events
    private final ArrayList<MotionEvent> motionEvents = new ArrayList<>();

    // Accumulated key events
    private final ArrayList<KeyEvent> keysEvents = new ArrayList<>();

    public Game ( Class<? extends Scene> c ) {
        super();
        setSceneClass( c );
    }

    public static void resetScene () {
        switchScene( getInstance().getSceneClass() );
    }

    public static void switchScene ( Class<? extends Scene> c ) {
        getInstance().setSceneClass( c );
        getInstance().setRequestedReset( true );
    }

    public static Scene scene () {
        return getInstance().getScene();
    }

    public static void vibrate ( int milliseconds ) {
        ( (Vibrator) Objects.requireNonNull( getInstance().getSystemService( VIBRATOR_SERVICE ) ) ).vibrate( milliseconds );
    }

    public static Game getInstance () {
        return instance;
    }

    public static void setInstance ( Game instance ) {
        Game.instance = instance;
    }

    public static int getWidth () {
        return width;
    }

    public static void setWidth ( int width ) {
        Game.width = width;
    }

    public static int getHeight () {
        return height;
    }

    public static void setHeight ( int height ) {
        Game.height = height;
    }

    public static float getDensity () {
        return density;
    }

    public static void setDensity ( float density ) {
        Game.density = density;
    }

    public static String getVersion () {
        return version;
    }

    public static void setVersion ( String version ) {
        Game.version = version;
    }

    public static int getVersionBuild () {
        return versionBuild;
    }

    public static void setVersionBuild ( int versionBuild ) {
        Game.versionBuild = versionBuild;
    }

    public static String getVanillaVersion () {
        return vanillaVersion;
    }

    @SuppressWarnings ( "unused" )
    public static void setVanillaVersion ( String vanillaVersion ) {
        Game.vanillaVersion = vanillaVersion;
    }

    public static float getTimeScale () {
        return timeScale;
    }

    @SuppressWarnings ( "unused" )
    public static void setTimeScale ( float timeScale ) {
        Game.timeScale = timeScale;
    }

    public static float getElapsed () {
        return elapsed;
    }

    @SuppressWarnings ( "unused" )
    public static void setElapsed ( float elapsed ) {
        Game.elapsed = elapsed;
    }

    @SuppressLint ( "ClickableViewAccessibility" )
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setInstance( this );
        TextureCache.setContext( getInstance() );
        BitmapCache.setContext( getInstance() );

        DisplayMetrics m = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( m );
        setDensity( m.density );

        try {
            setVersion( getPackageManager().getPackageInfo( getPackageName(), 0 ).versionName );
            setVersionBuild( getPackageManager().getPackageInfo( getPackageName(), 0 ).versionCode );
        } catch ( NameNotFoundException e ) {
            setVersion( "???" );
            setVersionBuild( 0 );
        }

        VersionNewsInfo.setAlreadySeen( false ); // Static variables tend to stick between games

        setVolumeControlStream( AudioManager.STREAM_MUSIC );


        setView( new GLSurfaceView( this ) );
        getView().setEGLContextClientVersion( 2 );
        getView().setEGLConfigChooser( false );
        // Addresses java.lang.IllegalArgumentException: No config chosen
        //noinspection MagicNumber
        getView().setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
        getView().setRenderer( this );
        getView().setOnTouchListener( this );
        setContentView( getView() );
    }

    @SuppressWarnings ( "unused" )
    @Override
    public void onResume () {
        super.onResume();

        setNow( 0 );
        getView().onResume();

        Music.INSTANCE.resume();
        Sample.INSTANCE.resume();
    }

    @SuppressWarnings ( "unused" )
    @Override
    public void onPause () {
        super.onPause();

        if ( getScene() != null ) {
            getScene().pause();
        }

        getView().onPause();
        Script.reset();

        Music.INSTANCE.pause();
        Sample.INSTANCE.pause();
    }

    @SuppressWarnings ( "unused" )
    @Override
    public void onDestroy () {
        super.onDestroy();
        destroyGame();

        Music.INSTANCE.mute();
        Sample.INSTANCE.reset();
    }

    @SuppressWarnings ( { "SameReturnValue", "unused" } )
    @SuppressLint ( { "Recycle", "ClickableViewAccessibility" } )
    public boolean onTouch ( MotionEvent event ) {
        synchronized (getMotionEvents()) {
            getMotionEvents().add( MotionEvent.obtain( event ) );
        }
        return true;
    }

    @SuppressWarnings ( "unused" )
    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event ) {

        if ( keyCode == Keys.VOLUME_DOWN ||
                keyCode == Keys.VOLUME_UP ) {

            return false;
        }

        synchronized (getMotionEvents()) {
            getKeysEvents().add( event );
        }
        return true;
    }

    @SuppressWarnings ( "unused" )
    @Override
    public boolean onKeyUp ( int keyCode, KeyEvent event ) {

        if ( keyCode == Keys.VOLUME_DOWN ||
                keyCode == Keys.VOLUME_UP ) {

            return false;
        }

        synchronized (getMotionEvents()) {
            getKeysEvents().add( event );
        }
        return true;
    }

    @SuppressWarnings ( "unused" )
    @Override
    public void onDrawFrame ( GL10 gl ) {

        if ( getWidth() == 0 || getHeight() == 0 ) {
            return;
        }

        SystemTime.tick();
        long rightNow = SystemTime.now;
        setStep( ( getNow() == 0 ? 0 : rightNow - getNow() ) );
        setNow( rightNow );

        step();

        NoosaScript.get().resetCamera();
        GLES20.glScissor( 0, 0, getWidth(), getHeight() );
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );
        draw();
    }

    @SuppressWarnings ( "unused" )
    @Override
    public void onSurfaceChanged ( GL10 gl, int width, int height ) {

        GLES20.glViewport( 0, 0, width, height );

        Game.setWidth( width );
        Game.setHeight( height );

    }

    @SuppressWarnings ( "unused" )
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

        setInstance( null );
    }

    protected void step () {

        if ( isRequestedReset() ) {
            setRequestedReset( false );
            try {
                setRequestedScene( getSceneClass().newInstance() );
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
        setScene( getRequestedScene() );
        getScene().create();

        Game.setElapsed( 0f );
        Game.setTimeScale( 1f );
    }

    protected void update () {
        final float TIME_SCALE = 0.001f;
        Game.setElapsed( Game.getTimeScale() * getStep() * TIME_SCALE );

        synchronized (getMotionEvents()) {
            Touchscreen.processTouchEvents( getMotionEvents() );
            getMotionEvents().clear();
        }
        synchronized (getKeysEvents()) {
            Keys.processTouchEvents( getKeysEvents() );
            getKeysEvents().clear();
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

    public Scene getRequestedScene () {
        return requestedScene;
    }

    public void setRequestedScene ( Scene requestedScene ) {
        this.requestedScene = requestedScene;
    }

    public long getNow () {
        return now;
    }

    public void setNow ( long now ) {
        this.now = now;
    }

    public long getStep () {
        return step;
    }

    public void setStep ( long step ) {
        this.step = step;
    }

    public GLSurfaceView getView () {
        return view;
    }

    public void setView ( GLSurfaceView view ) {
        this.view = view;
    }

    public SurfaceHolder getHolder () {
        return holder;
    }

    public void setHolder ( SurfaceHolder holder ) {
        this.holder = holder;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public ArrayList<MotionEvent> getMotionEvents () {
        return motionEvents;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    public ArrayList<KeyEvent> getKeysEvents () {
        return keysEvents;
    }
}
