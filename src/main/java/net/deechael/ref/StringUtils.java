package net.deechael.ref;

import java.util.Random;

final class StringUtils {

    private StringUtils() {}

    public static String join(String separator, String[] strings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i]);
            if (i < strings.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String random16() {
        StringBuilder builder = new StringBuilder();
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            builder.append(chars[random.nextInt(chars.length)]);
        }
        return builder.toString();
    }

}
