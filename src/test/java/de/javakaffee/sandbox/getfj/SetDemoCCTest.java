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
import static de.javakaffee.sandbox.getfj.TestData.P2;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.*;

import org.testng.annotations.Test;

/**
 * @author Martin Grotzke
 */
public class SetDemoCCTest {

    @Test
    public void testFilterCC() {
        final Set<Person> items = new TreeSet<Person>(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return Integer.compare(o1.getAge(), o2.getAge());
            }
        });
        items.addAll(Arrays.asList(P1, P2));
        final SetDemoCC demo = new SetDemoCC(items);
        final Set<Person> ofFullAge = demo.getMenOfFullAge();
        assertNotNull(ofFullAge);
        assertEquals(ofFullAge.size(), 1);
        assertEquals(ofFullAge, items);
    }
    
    @Test
    public void testFilterGuava() {
        final List<Person> items = Arrays.asList(P1, P2);
        final ListDemoGuava demo = new ListDemoGuava(items);
        final List<Person> ofFullAge = demo.getMenOfFullAgeAsList();
        assertNotNull(ofFullAge);
        assertEquals(ofFullAge.size(), 2);
        assertEquals(ofFullAge, items);
    }

}
