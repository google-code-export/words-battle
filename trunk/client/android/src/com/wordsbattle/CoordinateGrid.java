package com.wordsbattle;

import java.util.ArrayList;
import java.util.List;

import com.wordsbattle.util.Pair;

import static com.wordsbattle.WordsBattleActivity.leftOffset;
import static com.wordsbattle.WordsBattleActivity.rightOffset;
import static com.wordsbattle.WordsBattleActivity.upOffset;
import static com.wordsbattle.WordsBattleActivity.downOffset;

public class CoordinateGrid {
    // TODO(acbelter): Что делать, если экраны вмещают разлиное количество букв?
    // Сетка, содержащая координаты центров букв.
    private List<Pair<Float, Float>> grid;
    
    public List<Pair<Float, Float>> getGrid() {
        return grid;
    }
    
    public CoordinateGrid(final int pDisplayWidth, final int pDisplayHeight, final int pLetterSpriteSize, final float pScale) {
        // Высота и ширина поля для букв.
        float fieldWidth = pDisplayWidth - leftOffset - rightOffset;
        float fieldHeight = pDisplayHeight - upOffset - downOffset;
        
        // Количество букв по осям.
        int letterCountX = (int) (fieldWidth / (pLetterSpriteSize * pScale));
        int letterCountY = (int) (fieldHeight / (pLetterSpriteSize * pScale));
        
        grid = new ArrayList<Pair<Float, Float>>(letterCountX * letterCountY);
        
        // Смещенное начало координат.
        float newOriginX = leftOffset + (fieldWidth - letterCountX * pLetterSpriteSize * pScale) / 2;
        float newOriginY = upOffset + (fieldHeight - letterCountY * pLetterSpriteSize * pScale) / 2;
        
        for (float y = newOriginY + letterCountY * pLetterSpriteSize * pScale - (pLetterSpriteSize * pScale / 2); 
                   y > newOriginY; 
                   y -= pLetterSpriteSize * pScale) {
            for (float x = newOriginX + (pLetterSpriteSize * pScale / 2); 
                       x < newOriginX + letterCountX * pLetterSpriteSize * pScale; 
                       x += pLetterSpriteSize * pScale) {
                grid.add(new Pair<Float, Float>(x, y));
            }
        }
    }
    
    public void deleteLetter(Letter deletedLetter) {
        for (Pair<Float, Float> point : grid) {
            if (point.getPointLetter() != null && point.getPointLetter().equals(deletedLetter)) {
                point.setPointLetter(null);
                break;
            }
        }
    }
}