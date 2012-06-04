package com.wordsbattle.common.domain;

import java.util.HashMap;
import java.util.Map;

public class LetterWeight {
	private static Map<Character, Integer> weights = new HashMap<Character, Integer>();
	
	public LetterWeight() {
		// As in Clockwords Prelude
		weights.put('a', 1);
		weights.put('b', 3);
		weights.put('c', 3);
		weights.put('d', 2);
		weights.put('e', 1);
		weights.put('f', 4);
		weights.put('g', 2);
		weights.put('h', 3);
		weights.put('i', 1);
		weights.put('j', 5);
		weights.put('k', 4);
		weights.put('l', 2);
		weights.put('m', 3);
		weights.put('n', 2);
		weights.put('o', 2);
		weights.put('p', 3);
		weights.put('q', 5);
		weights.put('r', 1);
		weights.put('s', 1);
		weights.put('t', 2);
		weights.put('u', 3);
		weights.put('v', 4);
		weights.put('w', 4);
		weights.put('x', 5);
		weights.put('y', 4);
		weights.put('z', 5);
	}
	
	public static int getWeight(char c) {
		return weights.get(c);
	}
}
