/**
 * Why?
 * <p>
 * It is very unlikely I will have more than a dozen entities.
 * I do not want to put the responsibility of controlling serialization on the
 * {@code Core} module, so I will just make simple DTOs and leverage {@link java.lang.Record}(s).
 * <p>
 * Also, I think it's beneficial to standardise my API responses.
 * If I make changes to {@code Core} that would alter my responses,
 * I need to be aware of that so as not to break my frontend client.
 * <p>
 * I have burnt my hand like that once before, unfortunately it was not an obvious break.
 * It did not break the build for the frontend, just the UI in several places.
 */
package com.bobo.storage.web.api.response;