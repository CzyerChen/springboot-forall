> jpa 是符合领域设计理念的产品，隔绝底层实现，让使用者更专注于业务的实现

> jpa 也很常用，从简单配置，走入高级，走向源码

### 配置JDBC连接
```text
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/testdb
    username: root
    password: root
  jpa:
    properties:
      hibernate:
        format_sql : true
        show_sql: true
        dialect: org.hibernate.dialect.MySQL5Dialect
    database: mysql
    show-sql: true
    generate-ddl: true
    hibernate:
      ddl-auto: update   # Hibernate ddl auto (create, create-drop, update)
      #validate               加载hibernate时，验证创建数据库表结构
      #create                  每次加载hibernate，重新创建数据库表结构，这就是导致数据库表数据丢失的原因。
      #create-drop        加载hibernate时创建，退出是删除表结构
      #update                 加载hibernate自动更新数据库结构
    database-platform: org.hibernate.dialect.MySQL5Dialect

```
里面很多参数来自于JpaProperties:
```text
@ConfigurationProperties(prefix = "spring.jpa")
public class JpaProperties {

	/**
	 * Additional native properties to set on the JPA provider.
	 */
	private Map<String, String> properties = new HashMap<>();

	/**
	 * Mapping resources (equivalent to "mapping-file" entries in persistence.xml).
	 */
	private final List<String> mappingResources = new ArrayList<>();

	/**
	 * Name of the target database to operate on, auto-detected by default. Can be
	 * alternatively set using the "Database" enum.
	 */
	private String databasePlatform;

	/**
	 * Target database to operate on, auto-detected by default. Can be alternatively set
	 * using the "databasePlatform" property.
	 */
	private Database database;

	/**
	 * Whether to initialize the schema on startup.
	 */
	private boolean generateDdl = false;

	/**
	 * Whether to enable logging of SQL statements.
	 */
	private boolean showSql = false;

	/**
	 * Register OpenEntityManagerInViewInterceptor. Binds a JPA EntityManager to the
	 * thread for the entire processing of the request.
	 */
	private Boolean openInView;

	private Hibernate hibernate = new Hibernate();

	public Map<String, String> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public List<String> getMappingResources() {
		return this.mappingResources;
	}

	public String getDatabasePlatform() {
		return this.databasePlatform;
	}

	public void setDatabasePlatform(String databasePlatform) {
		this.databasePlatform = databasePlatform;
	}

	public Database getDatabase() {
		return this.database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public boolean isGenerateDdl() {
		return this.generateDdl;
	}

	public void setGenerateDdl(boolean generateDdl) {
		this.generateDdl = generateDdl;
	}

	public boolean isShowSql() {
		return this.showSql;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	public Boolean getOpenInView() {
		return this.openInView;
	}

	public void setOpenInView(Boolean openInView) {
		this.openInView = openInView;
	}

	public Hibernate getHibernate() {
		return this.hibernate;
	}

	public void setHibernate(Hibernate hibernate) {
		this.hibernate = hibernate;
	}

	/**
	 * Get configuration properties for the initialization of the main Hibernate
	 * EntityManagerFactory.
	 * @param settings the settings to apply when determining the configuration properties
	 * @return some Hibernate properties for configuration
	 */
	public Map<String, Object> getHibernateProperties(HibernateSettings settings) {
		return this.hibernate.getAdditionalProperties(this.properties, settings);
	}

	/**
	 * Determine the {@link Database} to use based on this configuration and the primary
	 * {@link DataSource}.
	 * @param dataSource the auto-configured data source
	 * @return {@code Database}
	 */
	public Database determineDatabase(DataSource dataSource) {
		if (this.database != null) {
			return this.database;
		}
		return DatabaseLookup.getDatabase(dataSource);
	}

	public static class Hibernate {

		private static final String USE_NEW_ID_GENERATOR_MAPPINGS = "hibernate.id."
				+ "new_generator_mappings";

		/**
		 * DDL mode. This is actually a shortcut for the "hibernate.hbm2ddl.auto"
		 * property. Defaults to "create-drop" when using an embedded database and no
		 * schema manager was detected. Otherwise, defaults to "none".
		 */
		private String ddlAuto;

		/**
		 * Whether to use Hibernate's newer IdentifierGenerator for AUTO, TABLE and
		 * SEQUENCE. This is actually a shortcut for the
		 * "hibernate.id.new_generator_mappings" property. When not specified will default
		 * to "true".
		 */
		private Boolean useNewIdGeneratorMappings;

		private final Naming naming = new Naming();

		public String getDdlAuto() {
			return this.ddlAuto;
		}

		public void setDdlAuto(String ddlAuto) {
			this.ddlAuto = ddlAuto;
		}

		public Boolean isUseNewIdGeneratorMappings() {
			return this.useNewIdGeneratorMappings;
		}

		public void setUseNewIdGeneratorMappings(Boolean useNewIdGeneratorMappings) {
			this.useNewIdGeneratorMappings = useNewIdGeneratorMappings;
		}

		public Naming getNaming() {
			return this.naming;
		}

		private Map<String, Object> getAdditionalProperties(Map<String, String> existing,
				HibernateSettings settings) {
			Map<String, Object> result = new HashMap<>(existing);
			applyNewIdGeneratorMappings(result);
			getNaming().applyNamingStrategies(result);
			String ddlAuto = determineDdlAuto(existing, settings::getDdlAuto);
			if (StringUtils.hasText(ddlAuto) && !"none".equals(ddlAuto)) {
				result.put("hibernate.hbm2ddl.auto", ddlAuto);
			}
			else {
				result.remove("hibernate.hbm2ddl.auto");
			}
			Collection<HibernatePropertiesCustomizer> customizers = settings
					.getHibernatePropertiesCustomizers();
			if (!ObjectUtils.isEmpty(customizers)) {
				customizers.forEach((customizer) -> customizer.customize(result));
			}
			return result;
		}

		private void applyNewIdGeneratorMappings(Map<String, Object> result) {
			if (this.useNewIdGeneratorMappings != null) {
				result.put(USE_NEW_ID_GENERATOR_MAPPINGS,
						this.useNewIdGeneratorMappings.toString());
			}
			else if (!result.containsKey(USE_NEW_ID_GENERATOR_MAPPINGS)) {
				result.put(USE_NEW_ID_GENERATOR_MAPPINGS, "true");
			}
		}

		private String determineDdlAuto(Map<String, String> existing,
				Supplier<String> defaultDdlAuto) {
			String ddlAuto = existing.get("hibernate.hbm2ddl.auto");
			if (ddlAuto != null) {
				return ddlAuto;
			}
			return (this.ddlAuto != null) ? this.ddlAuto : defaultDdlAuto.get();
		}

	}

	public static class Naming {

		private static final String DEFAULT_PHYSICAL_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy";

		private static final String DEFAULT_IMPLICIT_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy";

		/**
		 * Fully qualified name of the implicit naming strategy.
		 */
		private String implicitStrategy;

		/**
		 * Fully qualified name of the physical naming strategy.
		 */
		private String physicalStrategy;

		public String getImplicitStrategy() {
			return this.implicitStrategy;
		}

		public void setImplicitStrategy(String implicitStrategy) {
			this.implicitStrategy = implicitStrategy;
		}

		public String getPhysicalStrategy() {
			return this.physicalStrategy;
		}

		public void setPhysicalStrategy(String physicalStrategy) {
			this.physicalStrategy = physicalStrategy;
		}

		private void applyNamingStrategies(Map<String, Object> properties) {
			applyNamingStrategy(properties, "hibernate.implicit_naming_strategy",
					this.implicitStrategy, DEFAULT_IMPLICIT_STRATEGY);
			applyNamingStrategy(properties, "hibernate.physical_naming_strategy",
					this.physicalStrategy, DEFAULT_PHYSICAL_STRATEGY);
		}

		private void applyNamingStrategy(Map<String, Object> properties, String key,
				Object strategy, Object defaultStrategy) {
			if (strategy != null) {
				properties.put(key, strategy);
			}
			else if (defaultStrategy != null && !properties.containsKey(key)) {
				properties.put(key, defaultStrategy);
			}
		}

	}

```

### 配置pom
```text
<dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
###  书写Entity
```text
@Entity // 标识实体类
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class User {
    //标识主键，指定主键生成策略
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false)//有很多标识的注解，这边也不一一描述作用，可以看源码的时候再说
    private int id;
    @Column(name = "name")
    private String name;
    private int sex;
    private String phone;
    private String address;
    private String email;
}
```

### 扫描包或者写注解
- 推荐扫描包
```text
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.forjpa.repository"})
public class JpaTestApplication {

    public static void main(String[] args){
        SpringApplication.run(JpaTestApplication.class,args);
    }
}
```

### 最后来一个Test
```text
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JpaTestApplication.class)
@WebAppConfiguration
public class ApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test1(){
        User user = new User(0, "claire", 2, "11111111111", "hz", "xxxx@gmail.com");
        userRepository.save(user);
    }
}

```
- 自动打印format的HQL
```text
Hibernate: 
    insert 
    into
        user
        (address, email, name, phone, sex) 
    values
        (?, ?, ?, ?, ?)
```