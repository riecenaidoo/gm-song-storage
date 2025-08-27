/**
 * The service layer of the application.
 *
 * <p>This package contains service contracts that represent units of work the system can perform,
 * including queries and operations that enforce business rules.
 *
 * <p>Rules:
 *
 * <ol>
 *   <li>The service layer communicates exclusively using the common domain language. It must not
 *       service requests expressed in layer-specific dialects such as DTOs or Commands.
 *   <li>All service methods must represent meaningful operations in the domain, including both
 *       business rule–enforcing actions and query-based use cases.
 * </ol>
 *
 * <p>Style:
 *
 * <ol>
 *   <li>Service contracts must respect the ordering of the serviced domain object's fields,
 *       particularly in method and parameter declaration order.
 *   <li>Service methods should be grouped by CRUD operations. Within each group, singular
 *       operations should be defined before batch operations.
 * </ol>
 *
 * <p>These classes may be aware of other layers or technical concerns, but must not depend on them.
 */
package com.bobo.storage.core.service;
