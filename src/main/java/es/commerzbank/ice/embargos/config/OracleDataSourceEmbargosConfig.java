package es.commerzbank.ice.embargos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import oracle.jdbc.pool.OracleDataSource;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "es.commerzbank.ice.embargos.repository", 
						entityManagerFactoryRef = "emEmbargos",
						transactionManagerRef = "transactionManager")
@EnableTransactionManagement
@EnableCaching
public class OracleDataSourceEmbargosConfig {
	
	/*@Value("${spring.datasource.jndi-name}")
	private String oracleJNDIName;*/
	
	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.username}")
	private String userName;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;

	@Value("${spring.ds-comun.url}")
	private String c_url;

	@Value("${spring.ds-comun.username}")
	private String c_userName;

	@Value("${spring.ds-comun.password}")
	private String c_password;

	@Value("${spring.ds-comun.driver-class-name}")
	private String c_driverClassName;

	@Bean(name="dsEmbargos")
	public DataSource oracleDataSource() throws SQLException {
		/*JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		return dataSourceLookup.getDataSource(oracleJNDIName);*/
		OracleDataSource dataSource = new OracleDataSource();
		dataSource.setUser(userName);
		dataSource.setURL(url);
		dataSource.setPassword(password);
		dataSource.setImplicitCachingEnabled(true);
        dataSource.setFastConnectionFailoverEnabled(true);
        return dataSource;
		
	}
	
	private JpaVendorAdapter jpaVendorAdapter() {
	    HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
	    hibernateJpaVendorAdapter.setShowSql(true);
	    hibernateJpaVendorAdapter.setDatabase(Database.ORACLE);
	    hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.Oracle10gDialect");
	    return hibernateJpaVendorAdapter;
	}
	
	@Bean(name="transactionManager")
    public PlatformTransactionManager transactionManager() throws SQLException{
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
    }
	
	@Bean(name="emEmbargos")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	    em.setDataSource(oracleDataSource());
	    em.setPackagesToScan("es.commerzbank.ice.embargos.domain.entity");
	    em.setJpaVendorAdapter(jpaVendorAdapter());
	    em.setPersistenceUnitName("dbEmbargosEntityManager");

	    em.afterPropertiesSet();

	    return em;

	}

	public Connection getEmbargosConnection()
		throws ClassNotFoundException, SQLException
	{
		Class.forName(driverClassName);
		Connection conn = DriverManager.getConnection(url, userName, password);
		return conn;
	}

	public Connection getComunesConnection()
			throws ClassNotFoundException, SQLException
	{
		Class.forName(c_driverClassName);
		Connection conn = DriverManager.getConnection(c_url, c_userName, c_password);
		return conn;
	}
}
