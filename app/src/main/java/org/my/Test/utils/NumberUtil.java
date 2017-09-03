package org.my.Test.utils;

public class NumberUtil {
    public static float parseFloat(String value){
        float result = 0.0f;

        try {
            if ((value != null) && !value.isEmpty() && !value.equalsIgnoreCase("none")) {
                result = Float.parseFloat(value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
