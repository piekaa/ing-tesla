package pl.piekoszek.ing.mutable;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class Decimal {
    private long value;
    private int decimalSeparatorPosition;
    private int decimalSeparatorPositionRight;

    public void read(InputStream inputStream) throws IOException {
        reset();

        var negative = false;
        var size = 0;
        for (int i = 0; ; i++) {
            var digitOrDotOrSign = inputStream.read();

            if (i == 0 && digitOrDotOrSign == '-') {
                negative = true;
                i--;
                continue;
            }

            if (Character.isWhitespace(digitOrDotOrSign) || digitOrDotOrSign == ',' || digitOrDotOrSign == '}') {
                break;
            }
            if (digitOrDotOrSign == '.') {
                decimalSeparatorPosition = i;
            } else {
                int digit = digitOrDotOrSign - '0';
                value *= 10;
                value += digit;
                size++;
            }
        }
        decimalSeparatorPositionRight = size - decimalSeparatorPosition;
        if (decimalSeparatorPosition == 0) {
            decimalSeparatorPositionRight = 0;
        }
        if (negative) {
            value *= -1;
        }
    }

    public void reset() {
        value = 0;
        decimalSeparatorPosition = 0;
        decimalSeparatorPositionRight = 0;
    }

    public void add(Decimal toAdd) {
        addOrSubtract(toAdd, 1);
    }

    public void subtract(Decimal toSubtract) {
        addOrSubtract(toSubtract, -1);
    }

    public void addOrSubtract(Decimal toAdd, int sign) {
        var separatorDifference = decimalSeparatorPositionRight - toAdd.decimalSeparatorPositionRight;
        if (separatorDifference >= 0) {
            var add = toAdd.value * tenTo(separatorDifference);
            value += add * sign;
        } else {
            value *= tenTo(-separatorDifference);
            decimalSeparatorPositionRight -= separatorDifference;
            markAsFloat();
            value += toAdd.value * sign;
        }
    }

    private void markAsFloat() {
        decimalSeparatorPosition = 1;
    }

    public void print(PrintWriter printWriter) {
        if (value < 0) {
            printWriter.append('-');
        }
        var dotPlace = tenTo(decimalSeparatorPositionRight);
        if (decimalSeparatorPosition == 0) {
            dotPlace = 0;
        }
        for (long place = Math.max(tenTo(numberOfDigits() - 1), dotPlace); place > 0; place /= 10) {
            var digit = Math.abs(value) / place % 10;
            printWriter.append((char) ('0' + digit));
            if (dotPlace == place) {
                printWriter.append('.');
            }
        }
    }

    private int numberOfDigits() {
        var digits = 1;
        var absValue = Math.abs(value);
        for (long powerOf10 = 10; absValue >= powerOf10; powerOf10 *= 10) {
            digits++;
        }
        return digits;
    }

    private long tenTo(int power) {
        long result = 1;
        for (int i = 0; i < power; i++) {
            result *= 10;
        }
        return result;
    }
}
