package com.wordsbattle;

import static com.wordsbattle.WordsBattleActivity.SCALE;
import static com.wordsbattle.WordsBattleActivity.SPRITE_SIZE;
import static com.wordsbattle.WordsBattleActivity.letterIsPressed;
import static com.wordsbattle.WordsBattleActivity.pressedLetter;
import static com.wordsbattle.WordsBattleActivity.pressedLetterX;
import static com.wordsbattle.WordsBattleActivity.pressedLetterY;
import static com.wordsbattle.WordsBattleActivity.myWord;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;

public class Letter extends Sprite {
    private char letter;
    public Word word;
    public boolean available;
    
    public char getLetter() {
        return letter;
    }
    
    public Letter(char pLetter, float pX, float pY, TexturesBase pTexBase) {
        super(pX - (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f, 
              pY - (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f, 
              pTexBase.getCharTexture(pLetter)); 
        this.letter = pLetter;
        this.available = true;
        this.word = null;
        this.setScale(SCALE);
    }

    // Метод, обрабатывающий нажатия на спрайт буквы.
    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        // Если буква в пуле и доступна для нажатия и если никакая буква ещё не нажата.
        if (this.available && !letterIsPressed && this.word == null) {
            int firstEmptyPlace = myWord.getFirstEmptyPlaceIndex();
                
            if (firstEmptyPlace != -1) {
                this.setPosition(myWord.cells.get(firstEmptyPlace).getX() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f, 
                                 myWord.cells.get(firstEmptyPlace).getY() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f);
                myWord.wordLetters[firstEmptyPlace] = this.letter;
            }
            
            this.word = myWord;
            // TODO: Чёрт знает, почему ,если тут поставить return true, всё перестаёт работать!
            // TODO: Сделать, чтобы спрайты перемещались по самому верхнему слою.
            return false;
        } 
            
        if (this.available && !letterIsPressed && this.word == myWord) {
            letterIsPressed = true;
            pressedLetter = this;
            
            this.setAlpha(0);
                
            pressedLetterX = this.getX() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f;
            pressedLetterY = this.getY() + (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f;
                
            // Удаляем символ из слова.
            for (int i = 0; i < word.cells.size(); i++) {
                // Здесь НУЖНО использовать координаты центров.
                if (word.cells.get(i).containPoint(pressedLetterX + SPRITE_SIZE * SCALE * 0.5f,
                                                   pressedLetterY + SPRITE_SIZE * SCALE * 0.5f)) {
                    word.wordLetters[i] = '#';
                    break;
                }
            }
            return true;
        }
        
//        else if (this.available && this.word == pressedLetter.word && letterIsPressed) {
//            float thisX = this.getX();
//            float thisY = this.getY();
//            
//            this.setPosition(pressedLetter);
//            pressedLetter.setPosition(thisX, thisY);
//            
//            letterIsPressed = false;
//            pressedLetter = null;
//        }

        return true;
    }
    
    @Override
    public void setPosition(float pX, float pY) {
        super.setPosition(pX - (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f, pY - (SPRITE_SIZE - SPRITE_SIZE * SCALE) * 0.5f);
    }
}
