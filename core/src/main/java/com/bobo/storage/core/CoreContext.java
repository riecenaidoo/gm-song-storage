package com.bobo.storage.core;

import static java.lang.annotation.ElementType.TYPE;

import com.bobo.storage.core.domain.DomainEntity;
import com.bobo.storage.core.resource.EntityRepository;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(CoreConfig.class)
@ComponentScan(basePackageClasses = CoreContext.class)
@EntityScan(basePackageClasses = DomainEntity.class)
@EnableJpaRepositories(basePackageClasses = EntityRepository.class)
@EnableTransactionManagement
public @interface CoreContext {}
