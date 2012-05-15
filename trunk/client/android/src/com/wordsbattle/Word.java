package com.wordsbattle;

import java.util.ArrayList;
import java.util.List;

import static com.wordsbattle.WordsBattleActivity.SCALE;
import static com.wordsbattle.WordsBattleActivity.SPRITE_SIZE;
import static com.wordsbattle.WordsBattleActivity.letterIsPressed;
import static com.wordsbattle.WordsBattleActivity.pressedLetter;
import static com.wordsbattle.WordsBattleActivity.pressedLetterX;
import static com.wordsbattle.WordsBattleActivity.pressedLetterY;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;

public class Word {
    public char[] wordLetters;
    public List<LetterPlace> cells;
    
    public String getWord() {
        StringBuilder buffer = new StringBuilder();
        
        for (char letter : wordLetters) {
            buffer.append(letter);
        }   
        
        return buffer.toString();
    }
    
    public int getFirstEmptyPlaceIndex() {
        for (int i = 0; i < wordLetters.length; i++) {
            if (wordLetters[i] == '#') {
                return i;
            }
        }
        
        return -1;
    }
    
    public Word(final int pLength, float pX, float pY, TextureRegion pTextureRegion, final boolean pAvailable) {
        wordLetters = new char[pLength];
        cells = new ArrayList<LetterPlace>(pLength);
        
        for (int i = 0; i < wordLetters.length; i++) {
            wordLetters[i] = '#';
        }    
        
        
        
        for (int i = 0; i < pLength; i++) {
            final int position = i;
            final Word thisWord = this;
            
            final LetterPlace newLetterPlace = new LetterPlace(i, pX + i * SPRITE_SIZE * SCALE, pY, pTextureRegion, pAvailable) {
                @Override
                public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, 
                                             final float pTouchAreaLocalX, 
                                             final float pTouchAreaLocalY) {
                    // ���� ��� ����� ������ � ����� ��������� ����� � ���� ���� ������� �����, ����������� � ���� �����.
                    if (pAvailable && letterIsPressed && pressedLetter.word == thisWord) {
                        final Path path = new Path(2);
                        path.to(pressedLetterX, pressedLetterY);
                        path.to(this.getX() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f, this.getY() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f);
                        
                        pressedLetter.registerEntityModifier(new PathModifier(2, path, null, new IPathModifierListener() {
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
                        
                        // ���������� � �� �����. 
//                        pressedLetter.setPosition(this.getX() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f,
//                                                  this.getY() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f);
                        pressedLetter.setAlpha(1);
                        wordLetters[position] = pressedLetter.getLetter();
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