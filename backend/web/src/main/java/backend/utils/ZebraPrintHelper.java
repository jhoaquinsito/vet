package backend.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import backend.core.CommandAndQueries;
import backend.exception.BusinessException;
import backend.person.PersonDTO;
import backend.product.ProductDTO;
import backend.product.batch.Batch;
import backend.product.batch.BatchDTO;
import backend.product.batch.BatchPrintDTO;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.sale.SaleDTO;
import backend.saleline.SaleLineDTO;

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
	 * @param quantity
	 * 				Cantidad de etiquetas a imprimir de ese batch.
	 * @throws BusinessException
	 */
	public static void PrintBatch(BatchPrintDTO pBatchDto)throws BusinessException{
		try {
			boolean mBatchNoHasIsoDueDate = false;
			
			CommandAndQueries mCNQ = new CommandAndQueries();
			Batch batch 	 	   = mCNQ.getBatch(pBatchDto.getId());
			
			if(batch == null)
				throw new BusinessException("El Batch a ser impreso no puede ser nulo.");
			if(batch.getId() == null)
				throw new BusinessException("El ID del Batch a ser impreso no puede ser nulo");
			if(batch.getIsoDueDate() == null)
				mBatchNoHasIsoDueDate = false;
			else
				mBatchNoHasIsoDueDate = true;
			//	throw new BusinessException("El IsoDueDate (fecha de vencimiento del lote codificada) a ser impreso no puede ser nulo.");
			String code = "";
			
			// aca obtenemos la printer default  
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
			if(mBatchNoHasIsoDueDate)
				code = String.valueOf(batch.getId()+ 10000)  + String.valueOf(batch.getIsoDueDate());
			else
				code = String.valueOf(batch.getId()+ 10000)  + "00000000";
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
			for(int i=0; i<pBatchDto.getQuantity(); i++){
				job.print(doc, null);
			 } 
				
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
			boolean mBatchNoHasIsoDueDate = false;
			
			if(batch == null)
				throw new BusinessException("El Batch a ser impreso no puede ser nulo.");
			if(batch.getId() == null)
				throw new BusinessException("El ID del Batch a ser impreso no puede ser nulo");
			if(batch.getIsoDueDate() == null)
				mBatchNoHasIsoDueDate = false;
			else
				mBatchNoHasIsoDueDate = true;
			//	throw new BusinessException("El IsoDueDate (fecha de vencimiento del lote codificada) a ser impreso no puede ser nulo.");
			String code = "";
			
			// aca obtenemos la printer default  
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
			if(mBatchNoHasIsoDueDate)
				code = String.valueOf(batch.getId()+ 10000)  + String.valueOf(batch.getIsoDueDate());
			else
				code = String.valueOf(batch.getId()+ 10000)  + "00000000";
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
	 * Este método permite imprimir un <strong> sALE </strong> (Batch)
	 * a partir de sus datos internos.
	 * @param pSale
	 * 				Venta que se quiere imprimir el TICKET.
	 * @throws BusinessException
	 */
	public static void PrintSale(SaleDTO pSale)throws BusinessException{
		try {
			
			if(pSale == null)
				throw new BusinessException("El Sale a ser impreso no puede ser nulo.");
			if(pSale.getId() == null)
				throw new BusinessException("El ID del Sale a ser impreso no puede ser nulo");
			
			SalePrintProperties mProperties = SalePrintProperties.getInstance();
			if(mProperties == null)
				throw new BusinessException("El archivo de propiedades no puede ser nulo.");
			
			PersonDTO 		 mPerson = pSale.getPerson();
			if(mPerson == null)
				throw new BusinessException("La venta tiene que estar asociada a una persona.");
			
		    Set<SaleLineDTO> mSaleLineList = pSale.getSaleLines(); 
		    if(mSaleLineList == null || mSaleLineList.isEmpty())
				throw new BusinessException("La venta debe tener al menos un producto como item vendido.");
			
			// Otenemos la printer default  
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
			
			DateFormat formatter = new SimpleDateFormat(mProperties.DATE_NOW_PATTERN);
			
			String nl 			= "\n";
			String clientName 	= mProperties.COMPANY_HEADER_VALUE;
			String date 		= formatter.format(new Date());
			
			String  xCompanyName, xLeft, xHeaderValue, xItemPrice, xItemDiscPrice,xItemTotalPrice ;
			int y, itemNameMaxCharacter  = 0;
			
			
			//x,y inicial 		= 50,115
			xCompanyName 		= mProperties.X_ALIGN_COMPANY_NAME;
			xLeft 				= mProperties.X_ALIGN_LEFT;
			xHeaderValue 		= mProperties.X_ALIGN_HEADER_VALUE;
			xItemPrice 			= mProperties.X_ALIGN_ITEM_PRICE;
			xItemDiscPrice 	    = mProperties.X_ALIGN_ITEM_DISC_PRICE;
			xItemTotalPrice 	= mProperties.X_ALIGN_TOTAL_PRICE;
			y 					= Integer.valueOf( mProperties.Y_START_VALUE);
			itemNameMaxCharacter =  mProperties.ITEM_NAME_MAX_CHARACTER;
			
			String  zplCommand = "^XA" + nl;
					/* NOMBRE DEL CLIENTE */
					if(mProperties.HAS_COMPANY_HEADER_FIELD){
					zplCommand+="^FO"				+xCompanyName	+","+String.valueOf(y)+"^A0N,54,30^FD"+clientName+"^FS"+nl;
					}
					/* LINEA SEPARADORA */
					if(mProperties.HAS_BREAKLINE_HEADER){
					y+=80; //115
					zplCommand+="^FO"+xLeft			+","+String.valueOf(y)+"^GB590,0,2^FS"+nl;
					}
					/* FECHA */
					if(mProperties.HAS_DATE_NOW_FIELD){
					y+=5; //120
					zplCommand+="^FO"+xLeft			+","+String.valueOf(y)+"^A0N,36,20^FDFecha:^FS"+nl;
					zplCommand+="^FO"+xHeaderValue	+","+String.valueOf(y)+"^A0N,36,20^FD"+date+"^FS"+nl;
					}
					/* CLIENTE */
					if(mProperties.HAS_CLIENT_NAME_FIELD){
					y+=50; //170
					zplCommand+="^FO"+xLeft			+","+String.valueOf(y)+"^A0N,36,20^FDCliente:^FS"+nl;
					zplCommand+="^FO"+xHeaderValue	+","+String.valueOf(y)+"^A0N,36,20^FD"+mPerson.getName()+"^FS"+nl;
					}
					/* FORMA DE PAGO */
					if(mProperties.HAS_PAYMENT_METHOD_FIELD){
					y+=50; //220
					zplCommand+="^FO"+xLeft			+","+String.valueOf(y)+"^A0N,36,20^FDF. Pago: ^FS"+nl;
					zplCommand+="^FO"+xHeaderValue	+","+String.valueOf(y)+"^A0N,36,20^FDContado.^FS"+nl;
					}
					/* LINEA SEPARADORA */
					if(mProperties.HAS_BREAKLINE_PRODUCTS){
					y+=60; //280
					zplCommand+="^FO"+xLeft			+","+String.valueOf(y)+"^GB590,0,2^FS"+nl;
					}
					y+=20;//300
					/* LISTADO DE PRODUCTOS */
					for(SaleLineDTO mSaleLine : mSaleLineList){
				    	
				    	ProductDTO 		mProduct 		= mSaleLine.getProduct();
				    	MeasureUnitDTO 	mMeasureUnit 	= mProduct.getMeasureUnit();
				    	
				    	/* - Nombre del producto - */
						String mItemName  		= mProduct.getName() + " " + "( x " + mSaleLine.getQuantity() + " " + mMeasureUnit.getName() + " )";
						if(mItemName.length() <= 50)
						{
							zplCommand+="^FO"+xLeft+" ,"+String.valueOf(y)+"^A0N,26,10^FD" +mItemName +"^FS"+nl;
							y+=25;//325
						}
						else
						{
							List<String> mItemNameSplit = new ArrayList<String>();
							int index 					= 0;
							while (index < mItemName.length()) {
								mItemNameSplit.add(mItemName.substring(index, Math.min(index + itemNameMaxCharacter,mItemName.length())));
							    index += itemNameMaxCharacter;
							}
							for(String mItemNameSplitString : mItemNameSplit){
								zplCommand+="^FO"+xLeft+" ,"+String.valueOf(y)+"^A0N,26,10^FD" +mItemNameSplitString +"^FS"+nl;
								y+=25;
							}
						}
						
						/* - Precio del producto - */
						float mItemTotalValue 	= mSaleLine.getUnit_Price() * mSaleLine.getQuantity() - (mSaleLine.getUnit_Price() * mSaleLine.getQuantity() * mSaleLine.getDiscount() / 100);
						String mItemTotal       = "$ " + String.format("%.2f", mItemTotalValue);					

						if( mSaleLine.getDiscount() == 0){
							zplCommand+="^FO"     +xItemPrice+","+String.valueOf(y)+"^A0N,26,10^FD" + mItemTotal+"^FS"+nl;
						}
						else{
							String mItemDesc = "Desc " + String.format("%.2f", mSaleLine.getDiscount())  + " % - ";
							zplCommand+="^FO"	  +xItemDiscPrice+","+String.valueOf(y)+"^A0N,26,10^FD" +mItemDesc + mItemTotal+"^FS"+nl;
						}
							
						y+=25;//350
				    }
					if(mProperties.HAS_BREAKLINE_PRODUCTS){
					/* LINEA SEPARADORA */
					y+=25; //375
					zplCommand+="^FO"+xLeft		 +","+String.valueOf(y)+"^GB590,0,2^FS"+nl;
					}
					/* TOTAL */
					if(mProperties.HAS_BREAKLINE_TOTAL){
					y+=25; //400
					zplCommand+="^FO"+xLeft		      +","+String.valueOf(y)+"^A0N,36,20^FD TOTAL:  ^FS"+nl;
					zplCommand+="^FO"+xItemTotalPrice +","+String.valueOf(y)+"^A0N,36,20^FD$ 2000,35^FS"+nl;
					}
					//y+=80;
					//zplCommand+=""+nl;
					
					zplCommand+="^XZ";
			  
			// convertimos el comando a bytes  
			byte[] by 			= zplCommand.getBytes();  
			DocFlavor flavor 	= DocFlavor.BYTE_ARRAY.AUTOSENSE;  
			Doc doc 			= new SimpleDoc(by, flavor, null);  
			  
			// Registramos en la consola el comando zpl.
			System.out.println("\n \n \nComandoZPL para Venta: " + String.valueOf(pSale.getId()) + "\n \n" + zplCommand + "\n \n ");
			
			// creamos el printjob  
			//TODO - DESCOMENTAR ESTA LINEA PARA PODER ENVIAR EL TRABAJO A LA IMPRESORA. COMENTARLO SI NO SE CUENTA CON LA IMPRESORA / ROLLOS>
			//DocPrintJob job 	= printService.createPrintJob();  
			  
			// imprimimos  
			//TODO - DESCOMENTAR ESTA LINEA PARA PODER EJECUTAR LA IMPRESION. COMENTARLO SI NO SE CUENTA CON LA IMPRESORA / ROLLOS>
			//job.print(doc, null);
			
			} catch (Exception e) {//PrintException
				throw new BusinessException("Error en la impresión: "+ e.getMessage(),e);
			} 
	}
}
