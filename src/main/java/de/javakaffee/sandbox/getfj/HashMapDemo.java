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

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javakaffee.sandbox.getfj.Person.Gender;
import fj.Equal;
import fj.F;
import fj.Hash;
import fj.Ord;
import fj.Ordering;
import fj.data.HashMap;
import fj.data.TreeMap;

/**
 * Demonstrates usage of {@link HashMap}.
 *
 * @author Martin Grotzke
 */
public class HashMapDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashMapDemo.class);

    private static final Equal<Person> personNameEquals = Equal.equal(new F<Person, F<Person, Boolean>>() {

        @Override
        public F<Person, Boolean> f(final Person a) {
            return new F<Person, Boolean>() {
                @Override
                public Boolean f(final Person b) {
                    return ObjectUtils.equals(a.getName(), b.getName());
                }};
        }});

    private static final Ord<Person> personNameOrd = Ord.ord(new F<Person, F<Person, Ordering>>() {

        @Override
        public F<Person, Ordering> f(final Person a) {
            return new F<Person, Ordering>() {
                @Override
                public Ordering f(final Person b) {
                    return Ord.stringOrd.compare(a.getName(), b.getName());
                }};
        }});

    private static final Hash<Person> personNameHashCode = Hash.hash(new F<Person, Integer>() {
        @Override
        public Integer f(final Person a) {
            return ObjectUtils.hashCode(a.getName());
        }});

    public static void main(final String[] args) {
        treeMapDemo();
        hashMapDemo();
    }

    static void treeMapDemo() {
        TreeMap<Person, String> map = TreeMap.empty(personNameOrd);
        map = map.set(new Person("Foo", Gender.MALE, 34), "Some description");
        LOGGER.info("Have map keys: {}", map.keys().toCollection());
        map = map.set(new Person("Bar", Gender.MALE, 34), "Some description");
        LOGGER.info("Have map keys: {}", map.keys().toCollection());
        map = map.set(new Person("Bar", Gender.MALE, 35), "Some description");
        LOGGER.info("Have map keys: {}", map.keys().toCollection());
    }

    static void hashMapDemo() {
        final HashMap<Person, String> map = new HashMap<Person, String>(personNameEquals, personNameHashCode);
        map.set(new Person("Foo", Gender.MALE, 34), "Some description");
        LOGGER.info("Have map keys: {}", map.keys().toCollection());
        map.set(new Person("Bar", Gender.MALE, 34), "Some description");
        LOGGER.info("Have map keys: {}", map.keys().toCollection());
        map.set(new Person("Bar", Gender.MALE, 35), "Some description");
        LOGGER.info("Have map keys: {}", map.keys().toCollection());
    }

}
