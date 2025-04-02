/**
 * After some frustrating debugging, I realised that the <code>RequestBody</code> annotation
 * must map to a single object, which does make sense.
 * To parse two or more objects, you can accept a <code>Map</code> in the controller,
 * or create an object to map the request body to i.e. a request object.
 * <p>
 * Style:
 * <ol>
 *   <li>Request must begin with the name of the resource it is associated with.</li>
 *   <li>Request objects are plain data objects.
 *   Controlling access and mutation is not necessary, their fields may be public.
 *   This would mean the default constructor is sufficient.</li>
 * </ol>
 *
 * @see org.springframework.web.bind.annotation.RequestBody
 */
package com.bobo.storage.web.api.request;