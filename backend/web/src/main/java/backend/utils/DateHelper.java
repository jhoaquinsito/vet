package backend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import backend.exception.BusinessException;

public class DateHelper {
	
	private static final String cDUEDATE_PARSING_EXCEPTION = "Error al intentar obtener una fecha  a partir de la fecha en formato ISO.";
	
	public static Date getDate(Integer pIsoDueDate) throws BusinessException{
			
			// iso due date tiene el formato yyyymmdd
			SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyyMMdd");
	
			Date mDueDate;
	
			try {
				// uso el formatter para parsear la fecha en formato ISO (si hay un error, lanza una excepción)
				mDueDate = mDateFormatter.parse(pIsoDueDate.toString());
			} catch (ParseException bParseException) {
				throw new BusinessException(DateHelper.cDUEDATE_PARSING_EXCEPTION, bParseException);
			}
			
			return mDueDate;
		}
}
