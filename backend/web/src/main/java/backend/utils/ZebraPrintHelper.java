package backend.utils;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import backend.exception.BusinessException;

public class ZebraPrintHelper {

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
	
}
