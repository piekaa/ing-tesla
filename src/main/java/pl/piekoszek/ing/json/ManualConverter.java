package pl.piekoszek.ing.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import pl.piekoszek.ing.endpoints.transactions.Transaction;
import pl.piekoszek.ing.mutable.collections.SortedNoAllocationsHashMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Component
class ManualConverter<T> implements HttpMessageConverter<T> {
    private final TransactionsJsonReader transactionsJsonReader;
    private final SortedNoAllocationsHashMapJsonWriter sortedNoAllocationsHashMapJsonWriter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    ManualConverter(TransactionsJsonReader transactionsJsonReader, SortedNoAllocationsHashMapJsonWriter sortedNoAllocationsHashMapJsonWriter) {
        this.transactionsJsonReader = transactionsJsonReader;
        this.sortedNoAllocationsHashMapJsonWriter = sortedNoAllocationsHashMapJsonWriter;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return List.of(MediaType.APPLICATION_JSON);
    }

    @Override
    public void write(Object toWrite, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (toWrite instanceof SortedNoAllocationsHashMap<?> map) {
            sortedNoAllocationsHashMapJsonWriter.write(outputMessage.getBody(), map);
            return;
        }
        objectMapper.writeValue(outputMessage.getBody(), toWrite);
    }

    @Override
    public T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException, IOException {
        var inputStream = inputMessage.getBody();
        if (clazz.isArray() && clazz.componentType() == Transaction.class) {
            try {
                return (T) transactionsJsonReader.read(inputStream);
            } catch (SingleItemException e) {
                var transactions = new Transaction[2];
                var transactionsForJackson = objectMapper.readValue("[{" + e.json, TransactionForJackson[].class);

                transactions[0] = new Transaction();
                transactions[0].amount.read(new ByteArrayInputStream((transactionsForJackson[0].amount + ",").getBytes()));
                transactions[0].creditAccount = transactionsForJackson[0].creditAccount;
                transactions[0].debitAccount = transactionsForJackson[0].debitAccount;

                return (T) transactions;
            } catch (NoItemsException e) {
                return (T) new Transaction[1];
            }
        }
        return objectMapper.readValue(inputStream, clazz);
    }
}