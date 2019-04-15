package net.sppan.base.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

public final class UuidUtils {
    private static long counter = 0L;

    private UuidUtils() {
    }

    public static String generateUuid() {
        String time = String.valueOf(System.nanoTime());
        String count = String.valueOf(getCount());
        return time + count + RandomStringUtils.randomNumeric(32 - time.length() - count.length());
    }

    private static long getCount() {
        Class var0 = UuidUtils.class;
        synchronized(UuidUtils.class) {
            if (counter < 0L) {
                counter = 0L;
            }

            return (long)(counter++);
        }
    }
}