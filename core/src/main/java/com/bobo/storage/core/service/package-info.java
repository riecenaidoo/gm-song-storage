/**
 * A service is a unit of work the system can perform.
 * Contracts within the service package outline the services available for use.
 * The service layer enforces the business rules of those services.
 * <p>
 * Rules:
 * <ol>
 *   <li>The service layer only communicates via the common domain language.
 *   It may not service requests in layer specific dialects, such as DTOs and Commands.</li>
 * </ol>
 * Style:
 * <ol>
 *   <li>Service contracts must respect the ordering of the serviced domain object's fields
 *   - namely in method and parameter declaration ordering.
 *   </li>
 *   <li>A service method should reflect a unit of work that has a business rule associated with it.
 *   If there is no business rule, e.g. simple queries, it need not be defined as part of the service
 *   contract.</li>
 * </ol>
 */
package com.bobo.storage.core.service;