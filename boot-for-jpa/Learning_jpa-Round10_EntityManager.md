```text
                    Persistence Class
                            |
                            |
       public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
             return createEntityManagerFactory(persistenceUnitName, (Map)null);
      } 
                            |
                            |
               EntityManagerFactory Interface --EJB/HIBERNATE
                            |
                            |
                EntityManager createEntityManager();
                            |
                            |
                   SessionFactoryImpl
                            |
                            |
    public Session createEntityManager() {
        return this.buildEntityManager(SynchronizationType.SYNCHRONIZED, Collections.emptyMap());
    }
                            |
                            |
                      EntityManager
                            |
                            |
            public interface EntityManager {
                void persist(Object var1);
            
                <T> T merge(T var1);
            
                void remove(Object var1);
            
                <T> T find(Class<T> var1, Object var2);
             。。。。。。
             }//执行增删改等操作

```

```text
Persistence 1--->* EntityManagerFactory 1--创建-->* EntityManager *--管理-->1 PersistenceContext 持久化上下文
                           1                                                             *
                           |                                                            /|\
                           1                                                             |
    persistence.xml  PersistenceUnit 1 ----------------创建------------------------------|
```