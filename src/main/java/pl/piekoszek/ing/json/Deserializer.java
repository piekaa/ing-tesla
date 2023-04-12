package pl.piekoszek.ing.json;

import java.io.IOException;
import java.io.InputStream;

interface Deserializer<T> {

    /**
     * @param inputStream
     * @param toFill
     * @return true if end of stream
     * @throws IOException
     */
    boolean deserialize(InputStream inputStream, T toFill) throws IOException;
}
