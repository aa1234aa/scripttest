package com.bitnei.cloud.common.runner;

import com.bitnei.cloud.common.util.ApplicationContextProvider;
import com.bitnei.cloud.sys.util.translate.ITranslate;
import com.bitnei.cloud.sys.util.translate.Translate;
import com.bitnei.cloud.sys.util.translate.TranslateFactory;
import jodd.util.RandomString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
@Slf4j
public class TranslateRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        String resourcePattern = "/**/*.class";

        try {

            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath("com.bitnei.cloud") + resourcePattern;

            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(pattern);

            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource.isReadable()) {

                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(className);
                    if(clazz.isAnnotationPresent(Translate.class)){
                        Translate t = clazz.getAnnotation(Translate.class);
                        ITranslate translate = (ITranslate) createBean(clazz);
                        TranslateFactory.getInstance().putTranslate(t, translate);
                    }
                }
            }

        }
        catch (Exception e){
            log.error("error", e);
        }
    }


    /**
     * 创建Bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createBean(Class<T> clazz){


        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();

        if ( ctx != null){
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
            BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(clazz);
            bdb.setScope("prototype");
            String beanName = "translate"+ System.currentTimeMillis() + RandomString.getInstance().randomBase64(6);
            beanFactory.registerBeanDefinition(beanName, bdb.getBeanDefinition());
            return (T)ctx.getBean(beanName);
        }
        return null;
    }
}
