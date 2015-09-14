package backend.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * La clase ApplicationConfiguration contiene toda la configuración del contexto
 * de la aplicación. Esto incluye: JPA y Hibernate, pero podría incluir otras
 * configuraciones en un futuro.
 * 
 * NOTA: Tener en cuenta que en ninguna parte se especifica el paquete que está
 * configurando esta clase, dado que se utiliza el paquete en donde está la
 * clase. Para personalizar los paquetes a escanear hay que hacer uso
 * de @EnableJpaRepositories o @EnableJpa y setear el atributo: "basePackage".
 * 
 * @author tomas
 * asdasdasd
 */
@Configuration
@EnableJpaRepositories("backend")
class ApplicationConfiguration {

	// Configuración de la base de datos:
	private final String cDATABASE_DRIVER_CLASS_NAME = "org.postgresql.Driver";
	private final String cDATABASE_URL = "jdbc:postgresql://localhost:5432/vet";
	private final String cDATABASE_USER = "vet";
	private final String cDATABASE_PASSWORD = "vet";

	// Paquete que se va a escanear para buscar clases con la annotation @Entity
	private final String cPACKAGES_TO_SCAN_ENTITIES = "backend";

	// TODO investigar que es la persistent unit
	private final String cPERSISTENCE_UNIT_NAME = "jpaData";

	// Properties de Hibernate
	// NOTA: para agregar o quitar, solo hay que agregar o quitar del array
	// definido abajo:
	private final List<String[]> cHIBERNATE_PROPERTIES = Arrays
			.asList(new String[][] { { "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect" },
					// TODO ver que hace esta property:
					{ "hibernate.show_sql", "false" },
					// TODO ver que hace esta property:
					{ "hibernate.format_sql", "false" },
					// TODO ver que hace esta property:
					{ "hibernate.hbm2ddl.auto", "create" } });

	/**
	 * El método dataSource() tiene como responsabilidad devolver la fuente de
	 * datos que la aplicación va a usar, ya configurada.
	 * 
	 * @return fuente de datos configurada
	 */
	@Bean // declaro que el objeto debe ser un bean del contexto
	public DataSource dataSource() {
		DriverManagerDataSource mDataSource = new DriverManagerDataSource();
		mDataSource.setDriverClassName(cDATABASE_DRIVER_CLASS_NAME);
		mDataSource.setUrl(cDATABASE_URL);
		mDataSource.setUsername(cDATABASE_USER);
		mDataSource.setPassword(cDATABASE_PASSWORD);
		return mDataSource;
	}

	/**
	 * El método entityManagerFactory() tiene como responsabilidad crear una factoria de administradores de
	 * entidades de JPA.
     * El entityManager está a cargo de las entidades (escucha, conoce las entidades, sus relaciones,
     * persiste el ciclo de vida de ellas y su interfaz define los metodos que serán usados para interactuar
     * con el persistence context). Está asociado a un persistence context específico donde las entidades viven.
	 * 
	 * @return factoria de administradores de entidades JPA
	 */
	@Bean // declaro que el objeto debe ser un bean del contexto
	public EntityManagerFactory entityManagerFactory() {

		HibernateJpaVendorAdapter mVendorAdapter = new HibernateJpaVendorAdapter();
		// TODO investigar que es el DDL y para que sirve
		mVendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean mEntityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		mEntityManagerFactory.setJpaVendorAdapter(mVendorAdapter);
		mEntityManagerFactory.setDataSource(this.dataSource());
		// packages to scan for @Entity classes can be specified:
		mEntityManagerFactory.setPackagesToScan(cPACKAGES_TO_SCAN_ENTITIES);
		// TODO la siguiente linea quiza haya que borrarla:
		mEntityManagerFactory.setPersistenceUnitName(cPERSISTENCE_UNIT_NAME);

		// configuro que las properties se van a cargar después
		mEntityManagerFactory.afterPropertiesSet();

		return mEntityManagerFactory.getObject();

	}

	/**
	 * El método transactionManager() tiene como responsabilidad crear el administrador de transacciones.
     * El administrador de transacciones es responsable del acceso transaccional a los datos, dando soporta a todas
     * las transacciones que ocurren dentro de la aplicación.
	 * @return administrador de transacciones JPA
	 */
	@Bean // declaro que el objeto debe ser un bean del contexto
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager mJpaTransactionManager = new JpaTransactionManager();
		mJpaTransactionManager.setEntityManagerFactory(this.entityManagerFactory());
		return mJpaTransactionManager;
	}
    
	/**
	 * El método sessionFactory() se encarga de crear la factoría de sessiones de Hibernate.
	 * @return factoría de sessiones de Hibernate
	 */
	@Bean // declaro que el objeto debe ser un bean del contexto
	public HibernateJpaSessionFactoryBean sessionFactory() {
		// creo una factory de sessiones de hibernate
		HibernateJpaSessionFactoryBean mSessionFactory = new HibernateJpaSessionFactoryBean();

		// defino las propiedades de hibernate
		Properties mJpaProperties = new Properties();
		for (String[] bHibernateProperty : cHIBERNATE_PROPERTIES) {
			mJpaProperties.setProperty(bHibernateProperty[0], bHibernateProperty[1]);
		}

		// cargo las properties en la session factory
		mSessionFactory.setJpaProperties(mJpaProperties);

		return mSessionFactory;
	}

}