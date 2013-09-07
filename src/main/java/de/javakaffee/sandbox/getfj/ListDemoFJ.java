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

import static fj.Ord.intOrd;
import static fj.data.List.iterableList;

import java.util.Calendar;

import de.javakaffee.sandbox.getfj.Person.Gender;
import de.javakaffee.sandbox.getfj.Person.Skill;
import fj.F;
import fj.F2;
import fj.data.List;
import fj.data.Option;
import fj.data.TreeMap;

/**
 * @author Martin Grotzke
 *
 */
public class ListDemoFJ {

    private static final F<Person, String> getName = new F<Person, String>() {
        @Override
        public String f(final Person a) {
            return a.getName();
        }
    };

    private static final F<Person, Boolean> ofFullAge = new F<Person, Boolean>(){
        @Override
        public Boolean f(final Person a) {
            return a.isOfFullAge();
        }
    };

    private static final F<Person, Boolean> male = new F<Person, Boolean>(){
        @Override
        public Boolean f(final Person a) {
            return a.getGender() == Gender.MALE;
        }
    };

    public static final F<Person, List<WebIdentity>> getWebIdentities = new F<Person, List<WebIdentity>>() {
        @Override
        public List<WebIdentity> f(final Person a) {
            return iterableList(a.getWebIdentities());
        }
    };

    public static final F2<List<WebIdentity>, Person, List<WebIdentity>> appendWebIdentities = new F2<List<WebIdentity>, Person, List<WebIdentity>>(){
        @Override
        public List<WebIdentity> f(final List<WebIdentity> a, final Person b) {
            return a.append(iterableList(b.getWebIdentities()));
        }
    };

    public static final F<WebIdentity, String> getProfileUrl = new F<WebIdentity, String>() {
        @Override
        public String f(final WebIdentity a) {
            return a.getProfileUrl();
        }
    };

    public static final F<Person, Option<Calendar>> getDateOfBirth = new F<Person, Option<Calendar>>() {
        @Override
        public Option<Calendar> f(final Person p) {
            return p.getDateOfBirth();
        }
    };

    /**
     * Generic function to extract the year from a Calendar, could go into some Calendars class.
     */
    public static final F<Calendar, Integer> getYear = new F<Calendar, Integer>() {
        @Override
        public Integer f(final Calendar cal) {
            return cal.get(Calendar.YEAR);
        }
    };

    public static final F<Person, Option<Integer>> getYearOfBirth = new F<Person, Option<Integer>>() {
		@Override
		public Option<Integer> f(final Person p) {
			return p.getDateOfBirth().isSome() ? Option.<Integer> some(p.getDateOfBirth().some().get(Calendar.YEAR)) : Option.<Integer> none();
		}
	};

	/**
	 * A generic function that sets or adds 1 for the given (optional) key.
	 */
    private static final F2<TreeMap<Integer, Integer>, Integer, TreeMap<Integer, Integer>> setOrAddOne = new F2<TreeMap<Integer, Integer>, Integer, TreeMap<Integer, Integer>>() {
		@Override
		public TreeMap<Integer, Integer> f(final TreeMap<Integer, Integer> map,
				final Integer key) {
		    // an alternative would be
//		        return map.update(key, Integers.add.f(1), 1);
			return map.set(key, map.get(key).orSome(0) + 1);
		}
	};

    private static F<Person, Boolean> hasSkill(final Skill skill) {
        return new F<Person, Boolean>() {
            @Override
            public Boolean f(final Person a) {
                return a.getSkills().contains(skill);
            }
        };
    }

    private final fj.data.List<Person> persons;

    public ListDemoFJ(final java.util.List<Person> persons) {
        this.persons = List.iterableList(persons);
    }

    public List<String> getNames() {
        return persons.map(getName);
    }

    public List<Person> getPersonsOfFullAge() {
        return persons.filter(ofFullAge);
    }

    public List<Person> getMenOfFullAge() {
        return persons.filter(ofFullAge).filter(male);
    }

    public fj.data.List<String> getWebIdentitiesOfMenWithSkill(final Skill skill) {
        return persons.filter(male).filter(hasSkill(skill))./*flatMap*/bind(getWebIdentities)
        /*or foldLeft(appendWebIdentities, List.<WebIdentity> nil())*/
        .map(getProfileUrl);
    }

	public TreeMap<Integer, Integer> getYearOfBirthGroupedByYear() {
	    // an alternative to
	    //   .map(getDateOfBirth.andThen(getYear.mapOption()))...
	    // is
	    //   .map(getDateOfBirth).map(getYear.mapOption)...
		final F<Person, Option<Integer>> getYearOfBirth = getDateOfBirth.andThen(getYear.mapOption());
        return Option.somes(persons.map(getYearOfBirth)).foldLeft(setOrAddOne, TreeMap.<Integer, Integer> empty(intOrd));
	}

}
