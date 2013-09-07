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

import static fj.P.p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fj.P;
import fj.P2;
import fj.P3;

/**
 * Demonstrates usage of {@link P} and friends.
 *
 * @author Martin Grotzke
 */
public class ProductDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDemo.class);

    public static void main(final String[] args) {
        final P2<String, String> lowerAndUpper = getLowerAndUpper("Martin Grotzke");
        LOGGER.info("LCased: {}, ucased: {}", lowerAndUpper._1(), lowerAndUpper._2());
        final P3<String, String, String> variations = getVariations("Martin Grotzke");
        LOGGER.info("LCased: {}, ucased: {}, camelCased: {}", new String[] { variations._1(), variations._2(), variations._3() });
    }

    static P2<String, String> getLowerAndUpper(final String value) {
        return p(value.toLowerCase(), value.toUpperCase());
    }

    static P3<String, String, String> getVariations(final String value) {
        return p(value.toLowerCase(), value.toUpperCase(), value.replaceAll(" ", ""));
    }

}
