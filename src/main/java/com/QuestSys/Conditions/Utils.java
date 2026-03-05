package com.QuestSys.Conditions;

import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Utils {
    private Utils(){}
    private static final Random RANDOM = new Random();
    
    public static double ParseTargetCount(Object valuesObj) {
		if (valuesObj instanceof Map<?, ?> values) {
			if (values.containsKey("target_count")) {
				return ((Number) values.get("target_count")).doubleValue();
			}
			if (values.get("range") instanceof List<?> range) {
				int min = ((Number) range.get(0)).intValue();
				int max = ((Number) range.get(1)).intValue();
				return RANDOM.nextDouble() * (max - min + 1) + min;
			}
		}
		return 1;
	}
}
