package pl.piekoszek.ing.json;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
    public static void skip(InputStream inputStream, int toSkip) throws IOException {

        long allRead = 0;
        do {
            allRead += inputStream.skip(toSkip - allRead);
        } while (allRead < toSkip);
    }

    public static boolean skipAndCheckForEndOfStream(InputStream inputStream, int toSkip) throws IOException {
        long allRead = 0;
        do {
            var read = inputStream.skip(toSkip - allRead);
            if (read == 0) {
                if (inputStream.read() == -1) {
                    return true;
                } else {
                    read++;
                }
            }
            allRead += read;
        } while (allRead < toSkip);
        return false;
    }

    public static String readString(InputStream inputStream, int length) throws IOException {
        return new String(readBytes(inputStream, length));
    }

    private static byte[] readBytes(InputStream inputStream, int length) throws IOException {
        byte[] bytes = new byte[length];

        int bytesRead = 0;
        while (bytesRead < length) {
            int result = inputStream.read(bytes, bytesRead, length - bytesRead);
            if (result == -1) {
                break;
            }
            bytesRead += result;
        }
        return bytes;
    }
}
