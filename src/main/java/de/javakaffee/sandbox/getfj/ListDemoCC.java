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

import static org.apache.commons.collections15.CollectionUtils.select;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Predicate;

import de.javakaffee.sandbox.getfj.Person.Gender;

/**
 * @author Martin Grotzke
 *
 */
public class ListDemoCC {
    
    private static final Predicate<Person> ofFullAge = new Predicate<Person>(){
        @Override
        public boolean evaluate(final Person object) {
            return object.isOfFullAge();
        }
    };
    
    private static final Predicate<Person> male = new Predicate<Person>(){
        @Override
        public boolean evaluate(final Person object) {
            return object.getGender() == Gender.MALE;
        }
    };
        
    private final List<Person> persons;
    
    public ListDemoCC(final List<Person> persons) {
        this.persons = persons;
    }

    public List<Person> getPersonsOfFullAge() {
        return select(persons, ofFullAge, new ArrayList<Person>(persons.size()));
    }

    public List<Person> getMenOfFullAge() {
        return select(select(persons, ofFullAge, new ArrayList<Person>(persons.size())), male, new ArrayList<Person>());
    }

}
