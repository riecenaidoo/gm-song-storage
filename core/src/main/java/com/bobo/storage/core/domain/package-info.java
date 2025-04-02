/**
 * Rules:
 * <ol>
 *   <li>The id of a Domain entity must not have a publicly accessible mutator.
 *   You should not be able to mutate an Entity's id and use it to access or mutate the real entity.</li>
 * </ol>
 *
 * Style:
 * <ol>
 *  <li>Domain object fields are ordered by domain importance.</li>
 * </ol>
 */
package com.bobo.storage.core.domain;