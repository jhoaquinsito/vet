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
import backend.product.BatchCodeGenerator;
import backend.product.ProductDTO;
import backend.product.batch.BatchPrintDTO;
import backend.product.measure_unit.MeasureUnitDTO;
import backend.sale.SaleDTO;
import backend.saleline.SaleLineDTO;

public class ZebraPrintHelper {

	public static final String PRINT_ERROR = "Error en la impresión: ";
	
	/* PRINT BATCH */
	public static final String BATCH_PRINT_ERROR_NULL_PRODUCT_ID = 
			PRINT_ERROR + "El ID del producto asociado al Batch a ser impreso no puede ser nulo.";
	public static final String BATCH_PRINT_ERROR_QUANTITY 		 = 
			PRINT_ERROR + "La cantidad de etiquetas a imprimir debe ser mayor que cero (0).";
	public static final String BATCH_PRINT_ERROR_ISODUEDATE		 = 
			PRINT_ERROR + "Se ha especificado un Iso Due Date inválido.";

	/* PRINT SALE */
	public static final String SALE_PRINT_ERROR_NULL_SALE = 
			PRINT_ERROR + "El Sale a ser impreso no puede ser nulo.";
	public static final String SALE_PRINT_ERROR_NULL_SALE_ID 		 = 
			PRINT_ERROR + "El ID del Sale a ser impreso no puede ser nulo.";
	public static final String SALE_PRINT_MISSING_PROPERTIES_FILE	 = 
			PRINT_ERROR + "El archivo de propiedades no puede ser nulo.";
	public static final String SALE_PRINT_MISSING_ASSOCIETED_PERSON	 = 
			PRINT_ERROR + "La venta tiene que estar asociada a una persona.";
	public static final String SALE_PRINT_NO_PRODUCTS_DETECTED		 = 
			PRINT_ERROR + "La venta debe tener al menos un producto como item vendido.";
	
	
	
	/* GENERIC ERRORS */
	public static final String PRINT_ERROR_INVALID_ZPL_CODE		 = 
		PRINT_ERROR + "No se ha establecido un Código ZPL válido.";
	
	
	
	
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

			/******VALIDACIONES******/
			if(pBatchDto.getProductId() == null)
				throw new BusinessException(BATCH_PRINT_ERROR_NULL_PRODUCT_ID);
			
			if(pBatchDto.getQuantity() <= 0)
				throw new BusinessException(BATCH_PRINT_ERROR_QUANTITY);
			
			if(pBatchDto.getIsoDueDate() <= 0)
				throw new BusinessException(BATCH_PRINT_ERROR_ISODUEDATE);
			
			/************************/
			
			String code = "";
			
			// Obtenemos el código del lote, a partir de su product.
			code = BatchCodeGenerator.code(pBatchDto.getProductId(), pBatchDto.getIsoDueDate());

			PrintCode(code,pBatchDto.getQuantity());
				
			} catch (Exception e) {
				throw new BusinessException(PRINT_ERROR + e.getMessage(),e);
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
			/******VALIDACIONES******/
			if(pSale == null)
				throw new BusinessException(SALE_PRINT_ERROR_NULL_SALE);
			if(pSale.getId() == null)
				throw new BusinessException(SALE_PRINT_ERROR_NULL_SALE_ID);
			
			SalePrintProperties mProperties = SalePrintProperties.getInstance();
			if(mProperties == null)
				throw new BusinessException(SALE_PRINT_MISSING_PROPERTIES_FILE);
			
			PersonDTO mPerson = pSale.getPerson();
			if(mPerson == null)
				throw new BusinessException(SALE_PRINT_MISSING_ASSOCIETED_PERSON);
			
		    Set<SaleLineDTO> mSaleLineList = pSale.getSaleLines(); 
		    if(mSaleLineList == null || mSaleLineList.isEmpty())
				throw new BusinessException(SALE_PRINT_NO_PRODUCTS_DETECTED);
		    /************************/
		    
		    PrintSale(pSale,mPerson,mSaleLineList);

			} catch (Exception e) {//PrintException
				throw new BusinessException(PRINT_ERROR + e.getMessage(),e);
			} 
	}
	
	//=======================================================================================
	
	// SECCION GENÉRICA
	
	
	
	/**
	 * Este método permite imprimir un código de barras.
	 * @param pCodeToPrint el código a ser impreso.
	 * @throws BusinessException
	 */
	private static void print(Doc pZplCodeToPrint)throws BusinessException{
		try {
			if(pZplCodeToPrint == null)
				throw new BusinessException(PRINT_ERROR_INVALID_ZPL_CODE );
			
			// aca obtenemos la printer default  
			PrintService mPrintService = PrintServiceLookup.lookupDefaultPrintService();   
			  
			// creamos el printjob  
			DocPrintJob mJob = mPrintService.createPrintJob();  
			  
			// imprimimos  
			mJob.print(pZplCodeToPrint, null);
			
			} 
		catch (PrintException e) {
			throw new BusinessException(PRINT_ERROR + e.getMessage(),e);
		} 
	}
	
	/**
	 * Este método devuelve el código en un formato listo para ser impreso como un
	 * código de barras.
	 * @param pCode el código que debe ser preparado para ser impreso. 
	 * @return mCodeToPrint el código listo para ser impreso.
	 * @throws BusinessException
	 */
	private static Doc getZplCodeToPrintBatch(String pCode)throws BusinessException{
			  
			String mZPLCommand = "^XA\n"+
			"^FO30,30^BY1"+
			"^B3N,N,100,Y,N\n"+
			"^FD"+pCode+"^FS\n"+
			"^XZ";
			  
			// convertimos el comando a bytes  
			byte[] mBytes 		= mZPLCommand.getBytes();  
			DocFlavor mFlavor 	= DocFlavor.BYTE_ARRAY.AUTOSENSE;  
			Doc mCodeToPrint 	= new SimpleDoc(mBytes, mFlavor, null);  
		
			return mCodeToPrint;
	}
	
	/**
	 * Este método permite imprimir un <strong> Codigo </strong> una cierta cantidad de
	 * veces.
	 * @param pCode el código a imprimir.
	 * @param pQuantity la cantidad de veces que debe imprimirse el código.
	 * @throws BusinessException
	 */
	public static void PrintCode(String pCode, int pQuantity)throws BusinessException{
		
		Doc mCodeToPrint = ZebraPrintHelper.getZplCodeToPrintBatch(pCode);
		
		for(int i=1; i<=pQuantity; i++){
			ZebraPrintHelper.print(mCodeToPrint);
		 }
	}
	
	
	//TODO - Make comentarios
	private static Doc getZplCodeToPrintSale(SaleDTO pSale, PersonDTO pPerson, Set<SaleLineDTO> pSaleLineList)throws BusinessException{
			  
		SalePrintProperties mProperties = SalePrintProperties.getInstance();
		if(mProperties == null)
			throw new BusinessException(SALE_PRINT_MISSING_PROPERTIES_FILE);
		
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
				zplCommand+="^FO"+xHeaderValue	+","+String.valueOf(y)+"^A0N,36,20^FD"+pPerson.getName()+"^FS"+nl;
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
				for(SaleLineDTO mSaleLine : pSaleLineList){
			    	
			    	ProductDTO 		mProduct 		= mSaleLine.getBatch().getProduct();
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
		
		return doc;
	}

	//TODO - Make comentarios
	public static void PrintSale(SaleDTO pSale,PersonDTO pPerson, Set<SaleLineDTO> pSaleLineList)throws BusinessException{
		
		Doc mCodeToPrint = ZebraPrintHelper.getZplCodeToPrintSale(pSale,pPerson,pSaleLineList);
		
		ZebraPrintHelper.print(mCodeToPrint);
		
	}
	
	// FIN SECCION GENÉRICA
	
	//=======================================================================================
}
