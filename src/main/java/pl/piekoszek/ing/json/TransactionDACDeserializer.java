package pl.piekoszek.ing.json;

import pl.piekoszek.ing.endpoints.transactions.Transaction;

import java.io.IOException;
import java.io.InputStream;

class TransactionDACDeserializer implements Deserializer<Transaction> {
    private final int firstSkip;
    private final int secondSkip;
    private final int thirdSkip;
    private final int goToNextObject;

    TransactionDACDeserializer(Formatting formatting) {
        firstSkip = formatting.bytesBetweenObjectOpenAndFirstNameChar()
                    + "debitAccount".length()
                    + formatting.bytesBetweenLastNameCharAndFirstValueToken()
                    + 1;

        secondSkip = 1
                     + formatting.bytesBetweenLastValueTokenAndComma()
                     + formatting.bytesBetweenLastValueTokenAndFirstNameChar()
                     + "amount".length()
                     + formatting.bytesBetweenLastNameCharAndFirstValueToken();

        thirdSkip = formatting.bytesBetweenLastValueTokenAndComma()
                    + formatting.bytesBetweenLastValueTokenAndFirstNameChar()
                    + "creditAccount".length()
                    + formatting.bytesBetweenLastNameCharAndFirstValueToken();

        goToNextObject = formatting.bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen() + 1; // +1 skip " char
    }

    @Override
    public boolean deserialize(InputStream inputStream, Transaction toFill) throws IOException {
        StreamUtils.skip(inputStream, firstSkip);
        toFill.debitAccount = StreamUtils.readString(inputStream, 26);

        StreamUtils.skip(inputStream, secondSkip);
        toFill.amount.read(inputStream);

        StreamUtils.skip(inputStream, thirdSkip);
        toFill.creditAccount = StreamUtils.readString(inputStream, 26);

        return StreamUtils.skipAndCheckForEndOfStream(inputStream, goToNextObject);
    }
}
