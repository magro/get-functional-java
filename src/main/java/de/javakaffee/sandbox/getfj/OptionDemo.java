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

import static fj.data.Option.none;
import static fj.data.Option.some;
import static fj.function.Integers.add;
import static fj.function.Integers.even;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fj.F;
import fj.data.Option;
import fj.function.Strings;

/**
 * Demonstrates usage of {@link Option}.
 *
 * @author Martin Grotzke
 */
public class OptionDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionDemo.class);

    public static void main(final String[] args) {
        createDemo("foo", "bar");
        LOGGER.info("------- map ---------");
        mapDemo();
        LOGGER.info("------- filter ---------");
        filterDemo();
        LOGGER.info("------- bind ---------");
        bindDemo();
    }

    static void createDemo(final String value1, final String value2) {
        final Option<Integer> option = Option.some(1);
        final Option<Integer> none = Option.none();
        final Option<String> o1 = Option.iif(StringUtils.isNotEmpty(value1), value1);
        final Option<String> o2 = Option.iif(Strings.isNotNullOrEmpty, value2);
        final String foo = o1.isSome() ? o1.some() : "<isEmpty>";
        LOGGER.info("Have o1: {}", o1.orSome("<isEmpty>"));
        LOGGER.info("One of o1 or o2 is not empty: {}", o1.orElse(o2).isSome());
    }

    static void mapDemo() {
        final Option<Integer> o1 = some(7);
        final Option<Integer> o2 = none();
        final Option<Integer> p1 = o1.map(add.f(42));
        final Option<Integer> p2 = o2.map(add.f(42));
        LOGGER.info("p1: {}", p1); // Some(49)
        LOGGER.info("p2: {}", p2); // None
    }

    static void filterDemo() {
        final Option<Integer> o1 = some(7);
        final Option<Integer> o2 = none();
        final Option<Integer> o3 = some(8);
        final Option<Integer> p1 = o1.filter(even);
        final Option<Integer> p2 = o2.filter(even);
        final Option<Integer> p3 = o3.filter(even);
        LOGGER.info("p1: {}", p1); // None
        LOGGER.info("p2: {}", p2); // None
        LOGGER.info("p3: {}", p3); // Some(8)
      }

    static void bindDemo() {
      final Option<Integer> o1 = some(7);
      final Option<Integer> o2 = some(8);
      final Option<Integer> o3 = none();
      final F<Integer, Option<Integer>> evenTimes3 = new F<Integer, Option<Integer>>() {
        @Override
        public Option<Integer> f(final Integer i) {
          return i % 2 == 0 ? some(i * 3) : Option.<Integer>none();
        }
      };
      final Option<Integer> p1 = o1.bind(evenTimes3);
      final Option<Integer> p2 = o2.bind(evenTimes3);
      final Option<Integer> p3 = o3.bind(evenTimes3);
      LOGGER.info("p1: {}", p1); // None
      LOGGER.info("p2: {}", p2); // Some(24)
      LOGGER.info("p3: {}", p3); // None
    }

}
