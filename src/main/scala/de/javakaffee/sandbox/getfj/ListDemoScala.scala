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
package de.javakaffee.sandbox.getfj

import de.javakaffee.sandbox.getfj.Person.Gender
import de.javakaffee.sandbox.getfj.Person.Skill

/**
 * @author Martin Grotzke
 */
class ListDemoScala(val persons: List[SPerson]) {

  def menOfFullAge: List[SPerson] = {
    persons.filter(_.isOfFullAge).filter(_.gender == Gender.MALE)
  }
  
  def webIdentitiesOfMenWithSkill(skill: Skill): List[String] = {
    persons.filter(_.isOfFullAge).flatMap(_.webIdentities).map(_.getProfileUrl)
  }

}