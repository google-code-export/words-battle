package com.wordsbattle;

import static com.wordsbattle.WordsBattleActivity.SCALE;
import static com.wordsbattle.WordsBattleActivity.SPRITE_SIZE;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class LetterPlace extends Sprite {
    public boolean available;

    // Метод определяет, попадает ли точка в область LetterPlace.
    public boolean containPoint(float pX, float pY) {
        return (pX >= this.getX() && pX <= (this.getX() + SPRITE_SIZE * SCALE) &&
                pY >= this.getY() && pY <= (this.getY() + SPRITE_SIZE * SCALE));
    }
    
    public LetterPlace(int pIndex, float pX, float pY, TextureRegion pLetterPlaceTextureRegion, boolean pAvailable) {
        super(pX - (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f, pY - (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f, pLetterPlaceTextureRegion); 
        this.available = pAvailable;
        this.setScale(SCALE);
    }
}
