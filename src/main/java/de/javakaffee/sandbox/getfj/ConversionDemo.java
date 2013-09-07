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

import static fj.data.List.iterableList;

import java.util.Arrays;
import java.util.Objects;

import com.google.common.base.Preconditions;

import fj.data.List;
import fj.data.Option;
/**
 * Shows conversions between FJ and POJava types.
 *
 * @author Martin Grotzke
 */
public class ConversionDemo {

    public static void main(final String[] args) {
        listConversion();
        final Option<String> o1 = Option.fromNull("foo");
        assertEquals(o1.toNull(), "foo");

        final Option<String> o2 = Option.fromString("bar");
        assertEquals(o2.toNull(), "bar");
    }

    static void listConversion() {
        final java.util.List<String> jList = Arrays.asList("Foo", "Bar");
        final List<String> fjList = iterableList(jList);
        assertEquals(fjList.toCollection(), jList);
    }

    static void assertEquals(Object one, Object another) {
        Preconditions.checkState(one == null ? another == null : another != null);
        Preconditions.checkState(one.equals(another));
    }
}
