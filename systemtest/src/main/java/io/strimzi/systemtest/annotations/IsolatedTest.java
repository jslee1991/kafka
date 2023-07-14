/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.systemtest.annotations;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static io.strimzi.systemtest.Constants.ISOLATED_TEST;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/***
 * Annotation for running isolated tests in strimzi test suite
 * please be sure that you know laws of parallel execution and concurrent programming
 * be sure that you do not use shared resources, and if you use shared resources please work with synchronization
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
@Inherited
@ResourceLock(mode = ResourceAccessMode.READ_WRITE, value = "global")
@Tag(ISOLATED_TEST)
@Test
public @interface IsolatedTest {
    String value() default "";
}