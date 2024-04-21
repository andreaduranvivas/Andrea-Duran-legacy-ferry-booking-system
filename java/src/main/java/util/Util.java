package util;

import java.util.Objects;

public class Util {
    public static String readFileToString(String path) {
        return (Objects.requireNonNull(Thread.currentThread().getClass().getResourceAsStream(path))).toString();
    }
}
