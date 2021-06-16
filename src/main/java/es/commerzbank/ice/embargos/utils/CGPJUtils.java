package es.commerzbank.ice.embargos.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public class CGPJUtils {

    private static DecimalFormat decimalFormat = null;

    public static String format(BigDecimal bigDecimal) {
        initDecimalFormat();
        return decimalFormat.format(bigDecimal);
    }

    public static BigDecimal parse(String s) throws ParseException {
        initDecimalFormat();
        return (BigDecimal) decimalFormat.parse(s);
    }

    private static void initDecimalFormat() {
        if (decimalFormat == null) {
            Locale locale = Locale.forLanguageTag("es");
            decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
            decimalFormat.applyPattern("#,##0.00");
            decimalFormat.setParseBigDecimal(true);
        }
    }
}
