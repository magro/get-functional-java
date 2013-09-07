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

import static com.google.common.collect.Collections2.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import scala.actors.threadpool.Arrays;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import de.javakaffee.sandbox.getfj.Person.Gender;

/**
 * @author Martin Grotzke
 *
 */
public class ListDemoGuava {

    private static final Function<Person, String> getName = new Function<Person, String>() {
        @Override
        public String apply(final Person input) {
            return input.getName();
        }
    };

    private static final Predicate<Person> ofFullAge = new Predicate<Person>(){

        @Override
        public boolean apply(final Person input) {
            return input.isOfFullAge();
        }
    };

    private static final Predicate<Person> male = new Predicate<Person>(){

        @Override
        public boolean apply(final Person input) {
            return input.getGender() == Gender.MALE;
        }
    };

    private final List<Person> persons;

    public ListDemoGuava(final List<Person> persons) {
        this.persons = persons;
    }

    public Collection<String> getNames() {
        return Collections2.transform(persons, getName);
    }

    public Collection<Person> getPersonsOfFullAge() {
        return filter(persons, ofFullAge);
    }

    public List<Person> getPersonsOfFullAgeAsList() {
        return new ArrayList<Person>(filter(persons, ofFullAge));
    }

    public Collection<Person> getMenOfFullAge() {
        return filter(filter(persons, ofFullAge), male);
    }

    public List<Person> getMenOfFullAgeAsList() {
        return new ArrayList<Person>(filter(persons, Predicates.and(Arrays.asList(ofFullAge, male))));
        // return new ArrayList<Person>(filter(filter(persons, ofFullAge), male));
    }

}
