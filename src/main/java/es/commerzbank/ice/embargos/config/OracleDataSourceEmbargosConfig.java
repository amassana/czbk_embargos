package es.commerzbank.ice.embargos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import es.commerzbank.ice.comun.lib.config.OracleDataSourceConfig;
import oracle.jdbc.pool.OracleDataSource;

@Configuration

@EnableScheduling
@EnableJpaRepositories(basePackages = "es.commerzbank.ice.embargos.repository", 
						entityManagerFactoryRef = "emEmbargos",
						transactionManagerRef = "transactionManager")
@EnableTransactionManagement
@EnableCaching
public class OracleDataSourceEmbargosConfig {
	private static final Logger logger = LoggerFactory.getLogger(OracleDataSourceEmbargosConfig.class);

	/*@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String userName;

	@Value("${spring.datasource.password}")
	private String password;*/

	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;

	/*@Value("${spring.ds-comun.url}")
	private String c_url;

	@Value("${spring.ds-comun.username}")
	private String c_userName;

	@Value("${spring.ds-comun.password}")
	private String c_password;*/

	@Value("${spring.ds-comun.driver-class-name}")
	private String c_driverClassName;
	
	@Autowired
	private OracleDataSourceConfig oracleDataSourceComunes;

	@Bean(name = "dsEmbargos")
	public DataSource oracleDataSource() throws SQLException, IllegalArgumentException, NamingException {
		/*OracleDataSource dataSource = new OracleDataSource();
		dataSource.setUser(userName);
		dataSource.setURL(url);
		dataSource.setPassword(password);
		dataSource.setImplicitCachingEnabled(true);
		dataSource.setFastConnectionFailoverEnabled(true);*/
		
		DataSource ds = null;
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();

        bean.setJndiName("java:comp/env/jdbc/embargosDB");
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        
		ds = (DataSource) bean.getObject();
		
		return ds;
	}

	private JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setDatabase(Database.ORACLE);
		hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.Oracle10gDialect");
		return hibernateJpaVendorAdapter;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() throws SQLException, IllegalArgumentException, NamingException {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean(name = "emEmbargos")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException, IllegalArgumentException, NamingException {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(oracleDataSource());
		em.setPackagesToScan("es.commerzbank.ice.embargos.domain.entity");
		em.setJpaVendorAdapter(jpaVendorAdapter());
		em.setPersistenceUnitName("dbEmbargosEntityManager");

		em.afterPropertiesSet();

		return em;

	}

	public Connection getEmbargosConnection() throws ClassNotFoundException, SQLException, IllegalArgumentException, NamingException {
		Class.forName(driverClassName);
		Connection conn = oracleDataSource().getConnection();
		return conn;
	}

	public Connection getComunesConnection() throws ClassNotFoundException, SQLException, IllegalArgumentException, NamingException {
		Class.forName(c_driverClassName);
		Connection conn = oracleDataSourceComunes.oracleDataSource().getConnection();
		return conn;
	}
}
