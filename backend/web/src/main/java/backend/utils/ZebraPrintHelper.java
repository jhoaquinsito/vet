package backend.utils;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import backend.exception.BusinessException;
import backend.product.batch.BatchDTO;

public class ZebraPrintHelper {

	/**
	 * Este método permite invocar a la impresión de una 
	 * etiqueta de prueba, con un valor puesto a la fuerza.
	 * @throws BusinessException
	 */
	public static void PrintTest() throws BusinessException{
		try {
		// aca obtenemos la printer default  
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
		  
		String zplCommand = "^XA\n"+
		"^FO30,30^BY1"+
		"^B3N,N,100,Y,N\n"+
		"^FD10566050116^FS\n"+
		"^XZ";
		  
		// convertimos el comando a bytes  
		byte[] by = zplCommand.getBytes();  
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;  
		Doc doc = new SimpleDoc(by, flavor, null);  
		  
		// creamos el printjob  
		DocPrintJob job = printService.createPrintJob();  
		  
		// imprimimos  
		
			job.print(doc, null);
		} catch (PrintException e) {
			throw new BusinessException("Error en la impresión: "+ e.getMessage(),e);
		} 
	}

	/**
	 * Este método permite imprimir un <strong> Codigo </strong> arbitrario.
	 * @param code
	 * 				Codigo a ser impreso mediante código de barras.
	 * @throws BusinessException
	 */
	public static void PrintCode(String code)throws BusinessException{
		try {
			// aca obtenemos la printer default  
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
			  
			String zplCommand = "^XA\n"+
			"^FO30,30^BY1"+
			"^B3N,N,100,Y,N\n"+
			"^FD"+code+"^FS\n"+
			"^XZ";
			  
			// convertimos el comando a bytes  
			byte[] by = zplCommand.getBytes();  
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;  
			Doc doc = new SimpleDoc(by, flavor, null);  
			  
			// creamos el printjob  
			DocPrintJob job = printService.createPrintJob();  
			  
			// imprimimos  
			
				job.print(doc, null);
			} catch (PrintException e) {
				throw new BusinessException("Error en la impresión: "+ e.getMessage(),e);
			} 
	}

	/**
	 * Este método permite imprimir un <strong> Lote </strong> (Batch)
	 * a partir de sus datos internos.
	 * Ej: "1001001052015"
	 * Se forma con el ( <code>Id </code> + 10000)  concatenado al  
	 * <code>IsoDueDate </code> del  <strong> Lote </strong>
	 * @param batch
	 * 				Lote a ser impreso mediante código de barras.
	 * @throws BusinessException
	 */
	public static void PrintBatch(BatchDTO batch)throws BusinessException{
		try {
			
			if(batch == null)
				throw new BusinessException("El Batch a ser impreso no puede ser nulo.");
			if(batch.getId() == null)
				throw new BusinessException("El ID del Batch a ser impreso no puede ser nulo");
			if(batch.getIsoDueDate() == null)
				throw new BusinessException("El IsoDueDate (fecha de vencimiento del lote codificada) a ser impreso no puede ser nulo.");
			
			
			// aca obtenemos la printer default  
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
			String code = String.valueOf(batch.getId()+ 10000)  + String.valueOf(batch.getIsoDueDate());
			String zplCommand = "^XA\n"+
			"^FO30,30^BY1"+
			"^B3N,N,100,Y,N\n"+
			"^FD"+code+"^FS\n"+
			"^XZ";
			  
			// convertimos el comando a bytes  
			byte[] by = zplCommand.getBytes();  
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;  
			Doc doc = new SimpleDoc(by, flavor, null);  
			  
			// creamos el printjob  
			DocPrintJob job = printService.createPrintJob();  
			  
			// imprimimos  
			
				job.print(doc, null);
			} catch (PrintException e) {
				throw new BusinessException("Error en la impresión: "+ e.getMessage(),e);
			} 
	}
}
