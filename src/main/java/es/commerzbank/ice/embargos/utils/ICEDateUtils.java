package es.commerzbank.ice.embargos.utils;

import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

//TODO: Esta clase es provisional y se tendra que eliminar, ya que se tendra que utilizar la clase ICEDateUtils de Comunes,
//Por tanto, los metodos de esta clase se tendran que pasar a la clase ICEDateUtils de Comunes.

public class ICEDateUtils {
	private static final Logger logger = LoggerFactory.getLogger(ICEDateUtils.class);

	public static final String FORMAT_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	
	public static final String FORMAT_yyyyMMdd = "yyyyMMdd";
	
	public static BigDecimal dateToBigDecimal(Date date, String format) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		String dateFormated = simpleDateFormat.format(date);
		
		return new BigDecimal(dateFormated);
		
	}

	public static BigDecimal localdateToBigDecimal(LocalDate date, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		String dateFormated = simpleDateFormat.format(DateUtils.convertToDate(date));

		return new BigDecimal(dateFormated);

	}
	
	public static BigDecimal actualDateToBigDecimal(String format) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		Date date = es.commerzbank.ice.comun.lib.typeutils.DateUtils.convertToDate(LocalDateTime.now());
		
		String dateFormated = simpleDateFormat.format(date);
		
		return new BigDecimal(dateFormated);
		
	}
	
	public static Date bigDecimalToDate(BigDecimal bigDecimalDate, String format) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		Date result = null;
		
		try {
		
			result = simpleDateFormat.parse(bigDecimalDate.toString());
		
		} catch (ParseException pe) {
			logger.error("Can't parse "+ bigDecimalDate.toString() +" to date", pe);
		}
		return result;
		
	}

	public static BigDecimal currentDateTime() {
		return actualDateToBigDecimal(FORMAT_yyyyMMddHHmmss);
	}
	
	
}
