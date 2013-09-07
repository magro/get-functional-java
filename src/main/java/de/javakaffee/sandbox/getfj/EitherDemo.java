/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.javakaffee.sandbox.getfj;

import static fj.data.Either.left;
import static fj.data.Either.right;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fj.Effect;
import fj.F;
import fj.data.Either;
import fj.function.Integers;

/**
 * Demonstrates usage of {@link Either}.
 *
 * @author Martin Grotzke
 */
public class EitherDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(EitherDemo.class);

    private static final F<NumberFormatException, Integer> exceptionToNum = new F<NumberFormatException, Integer>() {

        @Override
        public Integer f(final NumberFormatException a) {
            return -1;
        }
    };

    public static void main(final String[] args) {
        final Either<NumberFormatException, Integer> parsed = parseInt("-23");
        LOGGER.info("Absolute value: {}", parsed.either(exceptionToNum, Integers.abs));

        LOGGER.info("Absolute value: {}", Integers.fromString().f("-23").map(Integers.abs).orSome(-1));

        new Effect<Integer>() {

            @Override
            public void e(final Integer a) {

            }};
    }

    static Either<NumberFormatException, Integer> parseInt(final String value) {
        try {
            return right(Integer.parseInt(value));
        } catch(final NumberFormatException e) {
            return left(e);
        }
    }

}
