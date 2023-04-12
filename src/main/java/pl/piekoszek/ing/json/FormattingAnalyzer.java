package pl.piekoszek.ing.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FormattingAnalyzer {
    Formatting analyze(InputStream inputStream) throws IOException, SingleItemException, NoItemsException {
        for (; ; ) {
            var c = inputStream.read();
            if (c == -1) {
                throw new NoItemsException();
            }
            if (c == '{') {
                break;
            }
        }

        var objects = new StringBuilder();

        for (; ; ) {
            var c = (char) inputStream.read();
            objects.append(c);
            if (c == '}') {
                break;
            }
        }

        for (; ; ) {
            var c = inputStream.read();
            if (c == -1) {
                throw new SingleItemException(objects.toString());
            }
            objects.append((char) c);
            if (c == '{') {
                break;
            }
        }

        for (; ; ) {
            var c = (char) inputStream.read();
            objects.append(c);
            if (c == '}') {
                break;
            }
        }

        var twoObjects = objects.toString();
        int currentIndex = 0;

        var names = new ArrayList<String>();

        int bytesBetweenObjectOpenAndFirstNameChar = 0;
        int bytesBetweenLastNameCharAndFirstValueToken = 0;
        int bytesBetweenLastValueTokenAndComma = 0;
        int bytesBetweenLastValueTokenAndFirstNameChar = 0;
        int bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen = 0;

        for (int i = 0; ; i++) {
            var nameInfo = readName(twoObjects, currentIndex);
            if (i == 0) {
                bytesBetweenObjectOpenAndFirstNameChar = nameInfo.bytesBefore();
            }
            if (i == 1) {
                bytesBetweenLastValueTokenAndFirstNameChar = nameInfo.bytesBefore();
            }
            names.add(nameInfo.name());

            currentIndex += nameInfo.bytesBefore() + nameInfo.name().length(); // nameLastToken

            var valueInfo = readValue(twoObjects, currentIndex);

            bytesBetweenLastNameCharAndFirstValueToken = valueInfo.bytesBefore();

            // go to next field or object end

            var afterValue = goToCommaOrObjectEnd(twoObjects, valueInfo.lastTokenIndex());

            if (afterValue.objectEnd()) {
                currentIndex = valueInfo.lastTokenIndex() + 1; // after last value token
                break;
            }

            currentIndex = valueInfo.lastTokenIndex() + afterValue.bytesBefore() + 1; // comma

            bytesBetweenLastValueTokenAndComma = afterValue.bytesBefore();

        }

        for (; ; ) {
            bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen++;
            if (twoObjects.charAt(currentIndex++) == '{') {
                break;
            }
        }

        return new Formatting(
                names,
                bytesBetweenObjectOpenAndFirstNameChar,
                bytesBetweenLastNameCharAndFirstValueToken,
                bytesBetweenLastValueTokenAndComma,
                bytesBetweenLastValueTokenAndFirstNameChar,
                bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen,
                new ByteArrayInputStream(("{" + twoObjects).getBytes()));
    }

    private NameRead readName(String twoObjects, int startIndex) {
        var bytesToNameStart = twoObjects.indexOf("\"", startIndex) + 1;
        var bytesToNameEnd = twoObjects.indexOf("\"", bytesToNameStart);

        var name = twoObjects.substring(bytesToNameStart, bytesToNameEnd);

        return new NameRead(bytesToNameStart - startIndex, name);
    }

    private ValueRead readValue(String twoObjects, int nameLastTokenIndex) {
        var i = nameLastTokenIndex + 1;
        for (; twoObjects.charAt(i) == ':' || Character.isWhitespace(twoObjects.charAt(i)); i++) ;
        var bytesBetweenNameAndValue = i - nameLastTokenIndex;

        var firstValueToken = twoObjects.charAt(i);

        if (firstValueToken == '"') {
            for (i++; twoObjects.charAt(i) != '"'; i++) ;
        } else {
            var number = "";
            for (; ; ) {
                number += twoObjects.charAt(i);
                if (!isNumber(number)) {
                    break;
                }
                i++;
            }
            i--;
        }
        return new ValueRead(bytesBetweenNameAndValue, i);
    }

    private AfterValue goToCommaOrObjectEnd(String twoObjects, int lastValueTokenIndex) {
        var startIndex = ++lastValueTokenIndex;
        for (; twoObjects.charAt(lastValueTokenIndex) != ',' && twoObjects.charAt(lastValueTokenIndex) != '}'; lastValueTokenIndex++) {
        }
        return new AfterValue(lastValueTokenIndex - startIndex, twoObjects.charAt(lastValueTokenIndex) == '}');
    }

    private boolean isNumber(String str) {
        try {

            if (!str.trim().equals(str)) {
                return false;
            }

            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


record NameRead(int bytesBefore, String name) {
}

record ValueRead(int bytesBefore, int lastTokenIndex) {
}

record AfterValue(int bytesBefore, boolean objectEnd) {
}

class NoItemsException extends Exception {
}

class SingleItemException extends Exception {
    public final String json;

    SingleItemException(String json) {
        this.json = json;
    }
}