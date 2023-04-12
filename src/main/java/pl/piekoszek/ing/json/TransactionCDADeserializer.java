package pl.piekoszek.ing.json;

import pl.piekoszek.ing.endpoints.transactions.Transaction;

import java.io.IOException;
import java.io.InputStream;

class TransactionCDADeserializer implements Deserializer<Transaction> {
    private final int firstSkip;
    private final int secondSkip;
    private final int thirdSkip;
    private final int goToNextObject;

    TransactionCDADeserializer(Formatting formatting) {
        firstSkip = formatting.bytesBetweenObjectOpenAndFirstNameChar()
                    + "creditAccount".length()
                    + formatting.bytesBetweenLastNameCharAndFirstValueToken()
                    + 1;

        secondSkip = 1
                     + formatting.bytesBetweenLastValueTokenAndComma()
                     + formatting.bytesBetweenLastValueTokenAndFirstNameChar()
                     + "debitAccount".length()
                     + formatting.bytesBetweenLastNameCharAndFirstValueToken()
                     + 1;

        thirdSkip = 1
                    + formatting.bytesBetweenLastValueTokenAndComma()
                    + formatting.bytesBetweenLastValueTokenAndFirstNameChar()
                    + "amount".length()
                    + formatting.bytesBetweenLastNameCharAndFirstValueToken();

        goToNextObject = formatting.bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen() - 1;
    }

    @Override
    public boolean deserialize(InputStream inputStream, Transaction toFill) throws IOException {
        StreamUtils.skip(inputStream, firstSkip);
        toFill.creditAccount = StreamUtils.readString(inputStream, 26);

        StreamUtils.skip(inputStream, secondSkip);
        toFill.debitAccount = StreamUtils.readString(inputStream, 26);

        StreamUtils.skip(inputStream, thirdSkip);
        toFill.amount.read(inputStream);

        return StreamUtils.skipAndCheckForEndOfStream(inputStream, goToNextObject);
    }
}
