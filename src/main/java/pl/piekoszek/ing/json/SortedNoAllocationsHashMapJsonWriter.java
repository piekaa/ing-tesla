package pl.piekoszek.ing.json;

import org.springframework.stereotype.Component;
import pl.piekoszek.ing.mutable.collections.SortedNoAllocationsHashMap;

import java.io.OutputStream;
import java.io.PrintWriter;

@Component
class SortedNoAllocationsHashMapJsonWriter {

    void write(OutputStream outputStream, SortedNoAllocationsHashMap<?> map) {
        var printWriter = new PrintWriter(outputStream);
        printWriter.print('[');
        var first = true;
        for (var item : map) {
            if (!first) {
                printWriter.print(',');
            }
            item.printJSON(printWriter);
            first = false;
        }
        printWriter.print(']');
        printWriter.flush();
    }
}
