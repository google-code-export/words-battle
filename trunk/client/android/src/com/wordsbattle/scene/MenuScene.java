package com.wordsbattle.scene;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.text.Text;

import android.graphics.Color;
import android.graphics.Typeface;

import com.wordsbattle.WordsBattleActivity;

public class MenuScene {
    private static Scene scene;
    private static Text text;
    
    // Загрузка сцены и всех необходимых ресурсов
    public static void load() {
        scene = new Scene();
        scene.setBackground(new ColorBackground(255f/255f, 157f/255f, 2f/255f));
        
        BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR);
        Font font = new Font(fontTexture, Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL), 64, true, Color.BLACK);
        SceneManager.loadFont(font);
        SceneManager.loadTexture(fontTexture);
       
        text = new Text(200, 60, font, "Words Battle");
        scene.attachChild(text); 
       
        // Here we register a simple touch listener.
        // This is so that we can switch to the second screen when 
        // the user taps our first screen.
        
        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
           public boolean onSceneTouchEvent(final Scene scene, final TouchEvent touchEvent) {
               SceneManager.setScene(WordsBattleActivity.gameScene);
               return false;
           }
       });
    }
    
    /**
     * Return the scene for when the scene is called to become active.
     */
    public static Scene run() {
        return scene;
    }
    
    /**
     * Unload any assets here - to be called later.
     */
    public static void unload() {
     
    }
}
