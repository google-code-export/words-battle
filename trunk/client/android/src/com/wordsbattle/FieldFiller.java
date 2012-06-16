package com.wordsbattle;

import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.util.modifier.ease.EaseBounceOut;

import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.util.Pair;

import static com.wordsbattle.WordsBattleActivity.SCALE;
import static com.wordsbattle.WordsBattleActivity.SPRITE_SIZE;
import static com.wordsbattle.WordsBattleActivity.texBase;
import static com.wordsbattle.WordsBattleActivity.fieldGrid;

public class FieldFiller {
    public static void fill(Scene scene) {
        for (Pair<Float, Float> point: fieldGrid.getGrid()) {
            if (point.getPointLetter() == null) {
                //char newLetterChar = conn.getNewLetter();
                Letter newLetter = new Letter('g');
                final LetterSprite newLetterSprite = new LetterSprite(newLetter, 
                                                    point.getKey() - SPRITE_SIZE * SCALE / 2, 
                                                    point.getValue() - SPRITE_SIZE * SCALE / 2,
                                                    texBase);
                newLetterSprite.setScale(0);
                newLetterSprite.registerEntityModifier(new ScaleModifier(1.5f, 0, SCALE, EaseBounceOut.getInstance()));
                scene.attachChild(newLetterSprite, 0);
                scene.registerTouchArea(newLetterSprite);
                point.setPointLetter(newLetterSprite);
            }
        }
    }
}
