package uk.org.brooklyn.miniops.confer.client.spring;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author ImBrooklyn
 * @since 03/05/2025
 */
public class ConferConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(ConferConfigProcessor.class);
        registry.registerBeanDefinition("conferConfigProcessor", builder.getBeanDefinition());
    }
}
