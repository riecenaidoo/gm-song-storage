package com.bobo.storage.core.semantic;

/**
 * Represents the API for a collection of entities.
 *
 * <p>While a {@link DomainEntity} maintains its invariants at an object level, an {@link
 * EntityService} maintains invariants at a system level, e.g a uniqueness constraint.
 *
 * <p>This interface serves to mark, and define the basic contract of an {@link EntityService}. It
 * should be composed with {@link Create}, {@link Read}, as needed. An {@link EntityService} does
 * not have to support all CRUD operations e.g.
 *
 * <pre>{@code
 * @CoreService
 * public class AppleService implements EntityService<Apple>, Read<Apple>{}
 * }</pre>
 *
 * @apiNote Instances of this service should be treated as collective nouns representing their
 *     entity type. e.g.
 *     <pre>
 *   {@code private final AppleService apples;}
 * </pre>
 *
 * @implSpec
 *     <ul>
 *       <li>Is a part of the service layer of {@link com.bobo.storage.core}, and must be annotated
 *           with {@link CoreService}.
 *       <li>An {@link EntityService} is expected to be injected, and never constructed by
 *           application code. It may only have a single, all-args constructor. The constructor must
 *           have a default access modifier (package-private), to control construction. It cannot be
 *           {@code private}, because the constructor must be visible for Spring to create the bean
 *           for DI.
 *       <li>Method definitions should be grouped by CRUD operation. Within each group, singular
 *           operations should be defined before batch operations. Method order and parameter lists
 *           should reflect the field order of the associated entity (see {@link DomainEntity}).
 *     </ul>
 *
 * @param <T> the {@link DomainEntity} this service is representing as a collection. We capture
 *     {@code T} to denote that an {@link EntityService} represents the API for a single {@link
 *     DomainEntity} collection.
 */
@SuppressWarnings("unused") // T is used in documentation to describe the collection API
public interface EntityService<T extends DomainEntity> {}
