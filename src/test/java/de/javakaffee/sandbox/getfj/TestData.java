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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.javakaffee.sandbox.getfj.Person.Gender;
import de.javakaffee.sandbox.getfj.Person.Skill;

/**
 * Just stores/provides common test data.
 * @author Martin Grotzke
 */
public final class TestData {
    
    public static final Person P1 = new Person("Martin", Gender.MALE, 34, asSet(Skill.JAVA, Skill.SCALA), Arrays.asList(new WebIdentity("hp1", "pu1")));
    public static final Person P2 = new Person("Ole", Gender.MALE, 34, asSet(Skill.JAVA, Skill.JAVASCRIPT), Arrays.asList(new WebIdentity("hp1", "pu2")));
    public static final Person P3 = new Person("Dennis", Gender.MALE, 33, asSet(Skill.JAVA, Skill.PYTHON), Arrays.asList(new WebIdentity("hp2", "pu3")));
    
    private TestData(){}
    
    static <T> Set<T> asSet(final T ... items) {
        if (items == null) {
            return Collections.emptySet();
        }
        final Set<T> result = new HashSet<T>(items.length);
        for(final T item : items) {
            result.add(item);
        }
        return result;
    }

}
