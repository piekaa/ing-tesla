package pl.piekoszek.ing.json;

import pl.piekoszek.ing.endpoints.transactions.Transaction;

import java.io.IOException;
import java.io.InputStream;

class TransactionCADDeserializer implements Deserializer<Transaction> {
    private final int firstSkip;
    private final int secondSkip;
    private final int thirdSkip;
    private final int goToNextObject;

    TransactionCADDeserializer(Formatting formatting) {
        firstSkip = formatting.bytesBetweenObjectOpenAndFirstNameChar()
                    + "creditAccount".length()
                    + formatting.bytesBetweenLastNameCharAndFirstValueToken()
                    + 1;

        secondSkip = 1
                     + formatting.bytesBetweenLastValueTokenAndComma()
                     + formatting.bytesBetweenLastValueTokenAndFirstNameChar()
                     + "amount".length()
                     + formatting.bytesBetweenLastNameCharAndFirstValueToken();

        thirdSkip = formatting.bytesBetweenLastValueTokenAndComma()
                    + formatting.bytesBetweenLastValueTokenAndFirstNameChar()
                    + "debitAccount".length()
                    + formatting.bytesBetweenLastNameCharAndFirstValueToken();

        goToNextObject = formatting.bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen() + 1; // +1 skip " char
    }

    @Override
    public boolean deserialize(InputStream inputStream, Transaction toFill) throws IOException {
        StreamUtils.skip(inputStream, firstSkip);
        toFill.creditAccount = StreamUtils.readString(inputStream, 26);

        StreamUtils.skip(inputStream, secondSkip);
        toFill.amount.read(inputStream);

        StreamUtils.skip(inputStream, thirdSkip);
        toFill.debitAccount = StreamUtils.readString(inputStream, 26);

        return StreamUtils.skipAndCheckForEndOfStream(inputStream, goToNextObject);
    }
}
