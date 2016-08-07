package backend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	/***
	 * Este método permite obtener una comparación de fechas sin ser tan preciso.
	 * Con que se cumplan los valores: Año, Mes, Dia, las fechas serán consideradas iguales
	 * @return
	 */
	public static Boolean isSameDateWithoutTime(Date pDateA, Date pDateB)
	{
		Calendar calendarA = Calendar.getInstance();
		calendarA.setTime(pDateA);
		int yearA 	= calendarA.get(calendarA.YEAR);
		int monthA 	= calendarA.get(calendarA.MONTH);
		int dayA 	= calendarA.get(calendarA.DAY_OF_MONTH);
		int hourA   = calendarA.get(calendarA.HOUR);
		int minuteA = calendarA.get(calendarA.MINUTE);
		
		Calendar calendarB = Calendar.getInstance();
		calendarB.setTime(pDateB);
		int yearB 	= calendarB.get(calendarB.YEAR);
		int monthB 	= calendarB.get(calendarB.MONTH);
		int dayB 	= calendarB.get(calendarB.DAY_OF_MONTH);
		int hourB   = calendarB.get(calendarB.HOUR);
		int minuteB = calendarB.get(calendarB.MINUTE);
		
		return (yearA == yearB) && (monthA == monthB) && (dayA == dayB) && (hourA == hourB) && (minuteA == minuteB);
	}
}
