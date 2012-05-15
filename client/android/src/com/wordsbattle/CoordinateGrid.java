package com.wordsbattle;

import java.util.ArrayList;
import java.util.List;

// import static com.wordsbattle.WordsBattleActivity.SCALE;
// import static com.wordsbattle.WordsBattleActivity.SPRITE_SIZE;

import static com.wordsbattle.WordsBattleActivity.leftOffset;
import static com.wordsbattle.WordsBattleActivity.rightOffset;
import static com.wordsbattle.WordsBattleActivity.upOffset;
import static com.wordsbattle.WordsBattleActivity.downOffset;

public class CoordinateGrid {
    // �����, ���������� ���������� ������� ����.
    private List<Pair<Float, Float>> grid;
    
    public List<Pair<Float, Float>> getGrid() {
        return grid;
    }
    
    public CoordinateGrid(final int pDisplayWidth, final int pDisplayHeight, final int pLetterSpriteSize, final float pScale) {
        // ������ � ������ ���� ��� ����.
        float fieldWidth = pDisplayWidth - leftOffset - rightOffset;
        float fieldHeight = pDisplayHeight - upOffset - downOffset;
        
        // ���������� ���� �� ����.
        int letterCountX = (int) (fieldWidth / (pLetterSpriteSize * pScale));
        int letterCountY = (int) (fieldHeight / (pLetterSpriteSize * pScale));
        
        grid = new ArrayList<Pair<Float, Float>>(letterCountX * letterCountY);
        
        // ��������� ������ ���������.
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
}
