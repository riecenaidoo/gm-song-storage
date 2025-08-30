package com.bobo.storage.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @implNote Library modules do not have an actual {@link SpringBootApplication} for Spring to
 *     target when setting up {@link SpringBootTest}/{@code SliceTest(s)}, so we create a "fake"
 *     target {@link SpringBootApplication} for Spring to use when bootstrapping the application
 *     context.
 */
@SpringBootApplication
class CoreTestApplication {}
