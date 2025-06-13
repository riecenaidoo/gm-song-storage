package com.bobo.storage.core;

import com.bobo.storage.core.domain.DomainEntity;
import com.bobo.storage.core.resource._ResourceMarker;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EntityScan(basePackageClasses = DomainEntity.class)
@EnableJpaRepositories(basePackageClasses = _ResourceMarker.class)
@EnableTransactionManagement
public @interface CoreContext {

}
