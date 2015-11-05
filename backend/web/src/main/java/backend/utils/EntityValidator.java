package backend.utils;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import backend.exception.BusinessException;

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
			throw new BusinessException(mViolation.getMessage());
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

}