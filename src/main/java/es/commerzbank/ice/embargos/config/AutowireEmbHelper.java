package es.commerzbank.ice.embargos.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Helper class which is able to autowire a specified class. It holds a static reference to the
 * {@link org.springframework.context.ApplicationContext}.
 */
public final class AutowireEmbHelper implements ApplicationContextAware {

    private static final AutowireEmbHelper INSTANCE = new AutowireEmbHelper();
    private static ApplicationContext applicationContext;

    private AutowireEmbHelper() {}

    /**
     * Tries to autowire the specified instance of the class if one of the specified beans which need to be autowired
     * are null.
     *
     * @param classToAutowire the instance of the class which holds @Autowire annotations
     * @param beansToAutowireInClass the beans which have the @Autowire annotation in the specified {#classToAutowire}
     */
    public static void autowire(Object classToAutowire, Object... beansToAutowireInClass) {
        for (Object bean : beansToAutowireInClass) {
            if (bean == null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
                return;
            }
        }
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        AutowireEmbHelper.applicationContext = applicationContext;
    }

    /**
     * @return the singleton instance.
     */
    public static AutowireEmbHelper getInstance() {
        return INSTANCE;
    }

}
