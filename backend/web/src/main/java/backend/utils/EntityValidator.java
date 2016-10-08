package backend.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import backend.exception.BusinessException;
import backend.sale.SaleCons;
import backend.sale.SaleLiteDTO;

/**
 * Un <code>EntityValidator</code> representa un validador de entidades. Su
 * responsabilidad es validar si una entidad cumple con las restricciones
 * anotadas en la definición de la entidad (Hibernate annotations de
 * hibernate-validation).
 * 
 * @author tomas
 *
 */
public class EntityValidator {

	/**
	 * Método que valida las restricciones de una entidad dada.
	 * 
	 * @param pEntityToValidate
	 *            Entidad a validar
	 * @throws BusinessException
	 *             si hubo un error, devuelvo el mensaje de la
	 *             <strong>primer</strong> violación a las restricciones.
	 */
	public <T> void validate(T pEntityToValidate) throws BusinessException {
		// obtengo el validador por default
		Validator validator = this.getDefaultValidator();

		// valido la entidad y obtengo el junto de violaciones de restricción
		Set<ConstraintViolation<T>> mConstraintViolations = validator.validate(pEntityToValidate);

		// obtengo la primer violacion de restricciones (si existe)
		ConstraintViolation<T> mViolation = this.getFirstConstraintViolation(mConstraintViolations);

		// si existe una violacion, lanzo una excepción con el mensaje
		// correspondiente
		if (mViolation != null) {
			String mFieldName = mViolation.getPropertyPath().toString();
			
			if(mFieldName != null  && mFieldName.length() > 0)
				throw new BusinessException("Campo: [" + mFieldName + "] " + mViolation.getMessage());
			else
				throw new BusinessException(mViolation.getMessage());
		}

	}
	
	/**
	 * Método que valida las restricciones de un conjunto de entidades.
	 * 
	 * @param pEntitiesToValidate
	 *            Entidades a validar
	 * @throws BusinessException
	 *             si hubo un error, devuelvo el mensaje de la
	 *             <strong>primer</strong> violación a las restricciones.
	 */
	public <T> void validate(List<T> pEntitiesToValidate) throws BusinessException {
		for (T entity : pEntitiesToValidate) {
			this.validate(entity);
		}
	}

	/**
	 * Método que devuelve el validador por default de Hibernate.
	 * 
	 * @return validador default
	 */
	private Validator getDefaultValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		return factory.getValidator();
	}

	/**
	 * Método que devuelve la primera violacion de restricciones a partir de un
	 * conjunto de violaciones de restricciones.
	 * 
	 * @param pConstraintViolations
	 *            conjunto de violaciones de restricciones
	 * @return primera violacion de restricción, si existe; si no hay
	 *         violaciones de restricciones entonces devuelvo <code>null</code>
	 */
	private <T> ConstraintViolation<T> getFirstConstraintViolation(Set<ConstraintViolation<T>> pConstraintViolations) {
		ConstraintViolation<T> mViolation = null;

		if (!pConstraintViolations.isEmpty()) {
			// obtengo el iterador del conjunto
			Iterator<ConstraintViolation<T>> mConstraintViolationsIterator = pConstraintViolations.iterator();
			// verifico si el iterador tiene un elemento siguiente
			if (mConstraintViolationsIterator.hasNext()) {
				// si tiene, entonces leo la primera de las violaciones de
				// restricciones
				mViolation = mConstraintViolationsIterator.next();
			}
		}

		return mViolation;
	}
	
	public static final boolean isDTOValid(Object pEntity) throws BusinessException {
		boolean mEntityIsValid = false;
		
		if (pEntity.getClass() == SaleLiteDTO.class){
			SaleLiteDTO mSaleLiteDTO = (SaleLiteDTO) pEntity;
			if (mSaleLiteDTO.getSettlement() == null)
				throw new BusinessException(SaleCons.cSALE_SETTLEMENT_NULL_EXCEPTION_MESSAGE);
			if (mSaleLiteDTO.getPerson() != null) {
			} else {
				throw new NullPointerException("La venta debe estar asociada a un cliente.");
			}
			
			mEntityIsValid = mSaleLiteDTO.getSettlement() != null && mSaleLiteDTO.getPerson() != null;
		}
		
		return mEntityIsValid;
		
	}

}
