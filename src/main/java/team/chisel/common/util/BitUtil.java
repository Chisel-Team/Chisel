package team.chisel.common.util;

/**
 * Utility for fancy bit stuff
 */
public class BitUtil {


    public static long concat(long first, long second, int secondLen){
        return (first << secondLen) | second;
    }

    public static long chop(long value, int length){
        return value & ((-1) >> (64 - length));
    }

    public static boolean[] read(long value, int length){
        boolean[] values = new boolean[length];
        long sum = 1;
        for (int i = 0 ; i < length ; i++ ){
            if ((value & sum) != 0){
                values[i] = true;
            }
            sum = sum << 1;
        }
        return values;
    }



}
