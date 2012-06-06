package com.wordsbattle.util;

import com.wordsbattle.LetterSprite;

// TODO(acbelter): переделать структуру класса и его название.
public class Pair<K, V> {
    private K key;
    private V value;
    private LetterSprite pointLetter;  // Спрайт буквы с центром в этой точке.
   
    public Pair(K key, V value) {  
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }
   
    public V getValue() {
        return value;
    }
    
    public LetterSprite getPointLetter() {
        return pointLetter;
    }
    
    public void setKey(K key) {
        this.key = key;
    }
   
    public void setValue(V value) {
        this.value = value;
    }
    
    public void setPointLetter(LetterSprite pointLetter) {
        this.pointLetter = pointLetter;
    }
    
}
