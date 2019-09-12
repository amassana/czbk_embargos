package es.commerzbank.ice.embargos.formats.cuaderno63;

import java.math.BigDecimal;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class BigDecimalCustomTypeHandler implements TypeHandler{
	
	private int length;
	private BigDecimal pontenciaDeDiez;
	
	/**
	 * Propiedad 'length' definida para el BigDecimalCustomTypeHandler de la config de beanio, 
	 * para determinar el tamanyo del bigdecimal en la escritura en caso de nulos.
	 * 	
	 * @param length
	 */
	public void setLength(String length) {
		this.length = Integer.parseInt(length);
	}
	
	/**
	 * Propiedad 'numDecimalPositions' definida para el BigDecimalCustomTypeHandler de la config de beanio,
	 * para determinar el numero de decimales del bigdecimal.
	 * 
	 * @param numDecimalPositions
	 */
	public void setNumDecimalPositions(String numDecimalPositions) {
		this.pontenciaDeDiez =  BigDecimal.valueOf(Math.pow(10, Integer.parseInt(numDecimalPositions)));
	}


	/**
	 * Metodo para la carga/lectura del archivo
	 */
	@Override
	public Object parse(String text) throws TypeConversionException {
		return new BigDecimal(text).divide(pontenciaDeDiez);
	}


	/**
	 * Metodo para la escritura del archivo
	 */
	@Override
	public String format(Object value)
	{
		String formato = "%0"+ length +"d";
		
		if (value == null) {
			return String.format(formato, 0);
		}
		
		BigDecimal bd = (BigDecimal) value;
		
		return String.format(formato, bd.multiply(pontenciaDeDiez).toBigInteger());
	}
	
	@Override
	public Class<?> getType() {
		return BigDecimal.class;
	}

}
