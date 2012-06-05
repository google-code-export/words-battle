package com.wordsbattle.scene;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

import com.wordsbattle.WordsBattleActivity;

public class SceneManager {
    // Базовая activity.
    private static WordsBattleActivity core;
 
    public static void init(WordsBattleActivity base) {
        core = base;
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    }
 
    // C помощью setScene переключаемся между экранами.
    public static void setScene(Scene scene) {
        core.getEngine().getScene().setOnSceneTouchListenerBindingEnabled(false);
        scene.setOnSceneTouchListenerBindingEnabled(true);
        core.getEngine().setScene(scene);
    }
 
    public static void loadTexture(Texture texture) {
        core.getEngine().getTextureManager().loadTexture(texture);
    }
 
    public static void loadFont(Font font) {
        core.getEngine().getFontManager().loadFont(font);
    }
}