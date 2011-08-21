package de.javakaffee.sandbox.getfj

class SPerson(val name: String, val gender: Person.Gender, val age: Int, val skills: Set[Person.Skill], val webIdentities: List[WebIdentity]) {

  final def isOfFullAge = age >= 18
  
}