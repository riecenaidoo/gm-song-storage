package com.bobo.storage.core.semantic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Service;

/**
 * A contract for a unit of work the system can perform, including queries and operations that
 * enforce business rules.
 *
 * <p>Marks the service layer of {@link com.bobo.storage.core}.
 *
 * @implSpec
 *     <p>Method definitions should be grouped by CRUD operation. Within each group, singular
 *     operations should be defined before batch operations. Method order and parameter lists should
 *     reflect the field order of the associated entity (see {@link DomainEntity}).
 *     <p>These classes may be aware of other layers or technical concerns, but must not depend on
 *     them.
 */
@Service
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CoreService {}
