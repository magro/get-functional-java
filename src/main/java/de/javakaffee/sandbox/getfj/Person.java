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

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fj.data.Option;


/**
 * @author Martin Grotzke
 */
public class Person {
    
    public static enum Gender {
        MALE, FEMALE
    }
    
    public static enum Skill {
        HTML, JAVASCRIPT, JAVA, SCALA, PYTHON, RUBY
    }
    
    private final String name;
    private final Gender gender;
    private final int age;
    private final Set<Skill> skills;
    private final List<WebIdentity> webIdentities;
    private Calendar dateOfBirthNullable;
    private Option<Calendar> dateOfBirth;
    
    // WebIdentities -> map auf person-url, count nach Anbieter
    // MusicCollection(Albums, Artist, Song) -> Gruppieren, zählen
    // parallele Ausführung: finden mit Ähnlichkeitssuche (Levenstein) auf Song
    
    public Person(final String name, final Gender gender, final int age) {
        this(name, gender, age, Collections.<Skill> emptySet(), Collections.<WebIdentity> emptyList());
    }
    public Person(final String name, final Gender gender, final int age, final Set<Skill> skills, final List<WebIdentity> webIdentities) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.skills = skills;
        this.webIdentities = webIdentities;
    }
    
    public Calendar getDateOfBirthNullable() {
		return dateOfBirthNullable;
	}
	public void setDateOfBirthNullable(Calendar dateOfBirthNullable) {
		this.dateOfBirthNullable = dateOfBirthNullable;
	}
	public Option<Calendar> getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Option<Calendar> dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getName() {
        return name;
    }
    public Gender getGender() {
        return gender;
    }
    public int getAge() {
        return age;
    }
    public boolean isOfFullAge() {
        return age >= 18;
    }
    public Set<Skill> getSkills() {
        return skills;
    }
    public List<WebIdentity> getWebIdentities() {
        return webIdentities;
    }

}
