/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.elastic.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface Field {

	@AliasFor("name")
	String value() default "";

	@AliasFor("value")
	String name() default "";

	FieldType type() default FieldType.Auto;

	boolean index() default true;

	DateFormat format() default DateFormat.none;

	String pattern() default "";

	boolean store() default false;

	boolean fielddata() default false;

	String searchAnalyzer() default "";

	String analyzer() default "";

	String normalizer() default "";

	String[] ignoreFields() default {};

	boolean includeInParent() default false;

	String[] copyTo() default {};

	int ignoreAbove() default -1;

	boolean coerce() default true;

	boolean docValues() default true;

	boolean ignoreMalformed() default false;

	boolean indexPhrases() default false;


	boolean norms() default true;


	String nullValue() default "";


	double scalingFactor() default 1;
}
