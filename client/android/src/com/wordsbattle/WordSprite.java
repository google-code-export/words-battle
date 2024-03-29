﻿package com.wordsbattle;

import static com.wordsbattle.WordsBattleActivity.SCALE;
import static com.wordsbattle.WordsBattleActivity.SPRITE_SIZE;
import static com.wordsbattle.WordsBattleActivity.letterIsPressed;
import static com.wordsbattle.WordsBattleActivity.pressedLetter;
import static com.wordsbattle.WordsBattleActivity.pressedLetterX;
import static com.wordsbattle.WordsBattleActivity.pressedLetterY;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;

import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.Word;

public class WordSprite {
    private Word word;
    public List<LetterPlaceSprite> cells;
    
    public String getWord() {
        StringBuilder buffer = new StringBuilder();
        
        for (Letter letter : word) {
            buffer.append(letter.getValue());
        }   
        
        return buffer.toString();
    }
    
    public void setLetter(int index, Letter letter) {
        word.set(index, letter);
    }
    
    public int getFirstEmptyPlaceIndex() {
        for (int i = 0; i < word.size(); i++) {
            if (word.get(i) == null) {
                return i;
            }
        }
        
        return -1;
    }
    
    public WordSprite(final int pLength, float pX, float pY, TextureRegion pTextureRegion, final boolean pAvailable) {
        word = new Word();
        for (int i = 0; i < pLength; i++) {
            word.add(null);
        }
        cells = new ArrayList<LetterPlaceSprite>(pLength);
                
        for (int i = 0; i < pLength; i++) {
            final int position = i;
            final WordSprite thisWord = this;
            
            final LetterPlaceSprite newLetterPlace = new LetterPlaceSprite(i, pX + i * SPRITE_SIZE * SCALE, pY, pTextureRegion, pAvailable) {
                @Override
                public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, 
                                             final float pTouchAreaLocalX, 
                                             final float pTouchAreaLocalY) {
                    // Если это слово игрока и можно вставлять буквы и если есть нажатая буква, находящаяся в этом слове.
                    if (pSceneTouchEvent.isActionUp() && pAvailable && letterIsPressed && pressedLetter.word == thisWord) {
                        final Path path = new Path(2);

                        path.to(pressedLetterX, pressedLetterY);
                        path.to(this.getX() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f, this.getY() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f);
                        
                        pressedLetter.registerEntityModifier(new PathModifier(0.1f, path, null, new IPathModifierListener() {
                            @Override
                            public void onPathStarted(PathModifier pPathModifier, IEntity pEntity) {
                                // TODO Auto-generated method stub
                                
                            }

                            @Override
                            public void onPathWaypointStarted(PathModifier pPathModifier, IEntity pEntity, int pWaypointIndex) {
                                // TODO Auto-generated method stub
                                
                            }

                            @Override
                            public void onPathWaypointFinished(PathModifier pPathModifier, IEntity pEntity, int pWaypointIndex) {
                                // TODO Auto-generated method stub
                                
                            }

                            @Override
                            public void onPathFinished(PathModifier pPathModifier, IEntity pEntity) {
                                // TODO Auto-generated method stub
                                
                            }}, EaseSineInOut.getInstance()));
                        
                        // Перемещаем её на место. 
//                        pressedLetter.setPosition(this.getX() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f,
//                                                  this.getY() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f);
                        //pressedLetter.setAlpha(1);
                        pressedLetter.setColor(255f/255f, 255f/255f, 255f/255f, 0f);
                        word.set(position, pressedLetter.getLetter());
                        letterIsPressed = false;
                        pressedLetter = null;
                    }
                    
                    return true;
                }
            };
            
            cells.add(newLetterPlace);
        }
    }
}