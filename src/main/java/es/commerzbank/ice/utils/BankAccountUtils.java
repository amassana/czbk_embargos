package es.commerzbank.ice.utils;

import java.math.BigInteger;

public class BankAccountUtils {
    private static BigInteger NINETYSEVEN = new BigInteger("97");
    private static BigInteger NINETYEIGHT = new BigInteger("98");

    public static String convertToIBAN(String cc)
    {
        BigInteger funthing = NINETYEIGHT.subtract(new BigInteger(cc + "142800").mod(NINETYSEVEN));

        return "ES" + funthing.toString() + cc;
    }
}
