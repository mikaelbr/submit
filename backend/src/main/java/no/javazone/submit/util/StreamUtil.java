package no.javazone.submit.util;

import java.io.InputStream;
import java.util.Scanner;

public class StreamUtil {

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
