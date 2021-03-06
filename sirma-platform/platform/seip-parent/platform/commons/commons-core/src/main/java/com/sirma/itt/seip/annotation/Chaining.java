package com.sirma.itt.seip.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Annotation to indicate that when injecting functions that can have multiple implementations and we want to call all
 * of them.
 *
 * @author BBonev
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
public @interface Chaining {
	// nothing to do
}
