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
 */
package de.javakaffee.sandbox.getfj;

import static de.javakaffee.sandbox.getfj.TestData.P1;
import static de.javakaffee.sandbox.getfj.TestData.*;
import static fj.Show.*;
import static fj.data.List.iterableList;
import static fj.data.Option.some;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.annotations.Test;

import de.javakaffee.sandbox.getfj.Person.Skill;
import fj.F;
import fj.F2;
import fj.data.TreeMap;
/**
 * @author Martin Grotzke
 */
public class ListDemoTest {

    final List<Person> items = Arrays.asList(P1, P2, P3);
    
    @Test
    public void testFilterCC() {
        final ListDemoCC demo = new ListDemoCC(items);
        final List<Person> ofFullAge = demo.getMenOfFullAge();
        assertNotNull(ofFullAge);
        assertEquals(ofFullAge.size(), 1);
        assertEquals(ofFullAge, items);
    }
    
    @Test
    public void testFilterGuava() {
        final ListDemoGuava demo = new ListDemoGuava(items);
        final List<Person> ofFullAge = demo.getMenOfFullAgeAsList();
        assertNotNull(ofFullAge);
        assertEquals(ofFullAge.size(), 1);
        assertEquals(ofFullAge, items);
    }
    
    @Test
    public void testFilterFJ() {
        final ListDemoFJ demo = new ListDemoFJ(items);
        final fj.data.List<Person> ofFullAge = demo.getMenOfFullAge();
        assertNotNull(ofFullAge);
        assertEquals(ofFullAge.length(), 1);
        assertEquals(ofFullAge.toCollection(), items);
    }
    
    @Test
    public void testGetWebIdentitiesOfFullAgeMenBySkillFJ() {
        final ListDemoFJ demo = new ListDemoFJ(items);
        final fj.data.List<String> items = demo.getWebIdentitiesOfMenWithSkill(Skill.JAVA);
        listShow(stringShow).println(items);
        assertNotNull(items);
        assertEquals(items.length(), 2);
    }
    
    private static final F2<Integer, Person, Integer> setDateOfBirth = new F2<Integer, Person, Integer>() {
		@Override
		public Integer f(Integer minusYears, Person p) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -p.getAge());
			p.setDateOfBirth(some(cal));
			return minusYears;
		}};
    
    @Test
    public void testGetYearOfBirthGroupedByYear() {
    	iterableList(items).foldLeft(setDateOfBirth, -20);
        final ListDemoFJ demo = new ListDemoFJ(items);
        TreeMap<Integer, Integer> yearAndCount = demo.getYearOfBirthGroupedByYear();
        assertNotNull(yearAndCount);
        listShow(stringShow).println(iterableList(yearAndCount.toMutableMap().entrySet()).map(new F<Map.Entry<Integer, Integer>, String>() {
			@Override
			public String f(Entry<Integer, Integer> a) {
				return a.getKey() + ": " + a.getValue();
			}}));
        assertEquals(yearAndCount.size(), 2);
    }

}
