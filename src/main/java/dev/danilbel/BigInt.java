package dev.danilbel;

import java.util.ArrayList;

public class BigInt {

    private static final String REGEX_INTEGER = "-?\\d+";

    private final ArrayList<Integer> digits = new ArrayList<>();
    private boolean isNegative = false;

    private final static BigInt ZERO = new BigInt(0);
    private final static BigInt ONE = new BigInt(1);

    private BigInt() {
    }

    public BigInt(String number) {
        if (number == null || number.isEmpty()) {
            throw new IllegalArgumentException("Number is empty");
        }
        if (!number.matches(REGEX_INTEGER)) {
            throw new IllegalArgumentException("Number is not a valid integer");
        }

        if (number.charAt(0) == '-') {
            isNegative = true;
            number = number.substring(1);
        }
        for (int i = number.length() - 1; i >= 0; i--) {
            digits.add(Character.getNumericValue(number.charAt(i)));
        }
    }

    public BigInt(int number) {
        this(String.valueOf(number));
    }

    private int compareTo(BigInt other) {
        if (this.isNegative && !other.isNegative) return -1;
        if (!this.isNegative && other.isNegative) return 1;

        int thisSize = this.digits.size();
        int otherSize = other.digits.size();
        if (thisSize != otherSize) {
            if (this.isNegative) return otherSize - thisSize;
            return thisSize - otherSize;
        }

        for (int i = thisSize - 1; i >= 0; i--) {
            int thisDigit = this.digits.get(i);
            int otherDigit = other.digits.get(i);
            if (thisDigit != otherDigit) {
                if (this.isNegative) return otherDigit - thisDigit;
                return thisDigit - otherDigit;
            }
        }

        return 0;
    }

    public boolean isLessThan(BigInt other) {
        return this.compareTo(other) < 0;
    }

    public boolean isLessThanOrEquals(BigInt other) {
        return this.compareTo(other) <= 0;
    }

    public boolean isGreaterThan(BigInt other) {
        return this.compareTo(other) > 0;
    }

    public boolean isGreaterThanOrEquals(BigInt other) {
        return this.compareTo(other) >= 0;
    }

    public boolean isEqualsTo(BigInt other) {
        return this.compareTo(other) == 0;
    }

    public BigInt negate() {
        if (this.isEqualsTo(ZERO)) {
            return ZERO;
        }
        BigInt result = new BigInt();
        result.digits.addAll(this.digits);
        result.isNegative = !this.isNegative;
        return result;
    }

    public BigInt abs() {
        BigInt result = new BigInt();
        result.digits.addAll(this.digits);
        result.isNegative = false;
        return result;
    }

    private BigInt addNumbers(BigInt first, BigInt second) {
        if (first.isNegative != second.isNegative && !first.isEqualsTo(ZERO) && !second.isEqualsTo(ZERO)){
            throw new IllegalArgumentException("Numbers should have the same sign");
        }

        BigInt result = new BigInt();
        result.isNegative = first.isNegative;

        int carry = 0;
        int firstSize = first.digits.size();
        int secondSize = second.digits.size();
        int maxSize = Math.max(firstSize, secondSize);
        for (int i = 0; i < maxSize || carry != 0; i++) {
            int sum = carry;
            if (i < firstSize) sum += first.digits.get(i);
            if (i < secondSize) sum += second.digits.get(i);
            result.digits.add(sum % 10);
            carry = sum / 10;
        }

        return result;
    }

    private void removeLeadingZeros() {
        for (int i = digits.size() - 1; i > 0; i--) {
            if (digits.get(i) == 0) {
                digits.remove(i);
            } else {
                break;
            }
        }
    }

    private BigInt subtractNumbers(BigInt first, BigInt second) {
        if (first.isNegative == second.isNegative && !first.isEqualsTo(ZERO) && !second.isEqualsTo(ZERO)) {
            throw new IllegalArgumentException("Numbers should have different signs");
        }

        BigInt result = new BigInt();
        BigInt bigger;
        BigInt smaller;
        BigInt firstAbs = first.abs();
        BigInt secondAbs = second.abs();

        if (firstAbs.isEqualsTo(secondAbs)) {
            return ZERO;
        } else if (firstAbs.isGreaterThan(secondAbs)) {
            bigger = first;
            smaller = second;
            result.isNegative = first.isNegative;
        } else {
            bigger = second;
            smaller = first;
            result.isNegative = !first.isNegative;
        }

        int carry = 0;
        int biggerSize = bigger.digits.size();
        int smallerSize = smaller.digits.size();
        for (int i = 0; i < biggerSize || carry != 0; i++) {
            int difference = carry;
            if (i < biggerSize) difference += bigger.digits.get(i);
            if (i < smallerSize) difference -= smaller.digits.get(i);
            if (difference < 0) {
                difference += 10;
                carry = -1;
            } else {
                carry = 0;
            }
            result.digits.add(difference);
        }

        result.removeLeadingZeros();

        return result;
    }

    public BigInt add(BigInt other) {
        if (this.isNegative == other.isNegative) {
            return addNumbers(this, other);
        } else {
            return subtractNumbers(this, other);
        }
    }

    public BigInt subtract(BigInt other) {
        if (this.isNegative == other.isNegative) {
            return subtractNumbers(this, other.negate());
        } else {
            return addNumbers(this, other.negate());
        }
    }

    public BigInt multiply(BigInt other) {
        if (this.isEqualsTo(ZERO) || other.isEqualsTo(ZERO)) {
            return ZERO;
        }

        BigInt result = new BigInt();
        for (int i = 0; i < this.digits.size(); i++) {
            for (int j = 0; j < other.digits.size(); j++) {
                int product = this.digits.get(i) * other.digits.get(j);
                int position = i + j;
                while (position >= result.digits.size()) {
                    result.digits.add(0);
                }
                result.digits.set(position, result.digits.get(position) + product);
            }
        }
        int carry = 0;
        for (int i = 0; i < result.digits.size(); i++) {
            int sum = result.digits.get(i) + carry;
            result.digits.set(i, sum % 10);
            carry = sum / 10;
        }
        while (carry > 0) {
            result.digits.add(carry % 10);
            carry /= 10;
        }
        result.isNegative = this.isNegative != other.isNegative;
        return result;
    }

    public BigInt divide(BigInt other) {
        if (other.isEqualsTo(ZERO)) {
            throw new ArithmeticException("Division by zero");
        }
        if (this.isEqualsTo(ZERO) || this.abs().isLessThan(other.abs())) {
            return ZERO;
        }

        BigInt result = new BigInt();
        BigInt dividend = this.abs();
        BigInt divisor = other.abs();
        while (dividend.isGreaterThanOrEquals(divisor)) {
            BigInt temp = divisor;
            BigInt multiple = ONE;
            while (dividend.isGreaterThanOrEquals(temp.add(temp))) {
                temp = temp.add(temp);
                multiple = multiple.add(multiple);
            }
            dividend = dividend.subtract(temp);
            result = result.add(multiple);
        }
        result.isNegative = this.isNegative != other.isNegative;
        return result;
    }

    public BigInt remainder(BigInt other) {
        if (other.isEqualsTo(ZERO)) {
            throw new ArithmeticException("Division by zero");
        }
        if (this.isEqualsTo(ZERO) || this.abs().isLessThan(other.abs())) {
            return this;
        }

        BigInt dividend = this.abs();
        BigInt divisor = other.abs();
        while (dividend.isGreaterThanOrEquals(divisor)) {
            BigInt temp = divisor;
            while (dividend.isGreaterThanOrEquals(temp.add(temp))) {
                temp = temp.add(temp);
            }
            dividend = dividend.subtract(temp);
        }

        if (this.isNegative) {
            dividend = dividend.negate();
        }

        return dividend;
    }

    public BigInt pow(int exponent) {
        if (exponent < 0) {
            throw new ArithmeticException("Negative exponent");
        }
        if (exponent == 0) {
            return ONE;
        }
        BigInt result = this;
        for (int i = 1; i < exponent; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    public int toInt() {
        if (this.isGreaterThanOrEquals(new BigInt(Integer.MAX_VALUE))) {
            return Integer.MAX_VALUE;
        }
        if (this.isLessThanOrEquals(new BigInt(Integer.MIN_VALUE))) {
            return Integer.MIN_VALUE;
        }
        return Integer.parseInt(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (isNegative) {
            result.append("-");
        }
        for (int i = digits.size() - 1; i >= 0; i--) {
            result.append(digits.get(i));
        }
        return result.toString();
    }
}
