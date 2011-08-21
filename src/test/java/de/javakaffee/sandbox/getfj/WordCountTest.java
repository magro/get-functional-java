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

import static fj.data.List.nil;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fj.Effect;
import fj.data.List;
import fj.data.TreeMap;
/**
 * @author Martin Grotzke
 */
public class WordCountTest {

	private List<String> _fileNames;
	private int _numFiles;

	@BeforeClass
	void beforeClass() throws IOException, URISyntaxException {
		_fileNames = nil();;
		
		_numFiles = 10;
		for(int i = 0; i < _numFiles; i++) {
			final String fileName = "/tmp/wordcounttest-"+ i +".txt";
			final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write("File" + i + "\n");
			writer.write("foo bar");
			writer.write('\n');
			writer.write("baz");
			for(int j = 0; j < 200; j++)
				writer.write("\nsomeword" + j);
			writer.close();
			_fileNames = _fileNames.cons(fileName);
		}
		
		final URI srcDir = new URL(getClass().getResource("/").toString() + "../../src/main/java").toURI().normalize();
		System.out.println(srcDir);
	}
	
	@AfterClass
	void afterClass() {
		_fileNames.foreach(new Effect<String>() {
			@Override
			public void e(final String a) {
				new File(a).delete();
			}});
	}
    
    @Test
    public void testWordCountFJ() {
    	assertEquals(WordCountFJ.countWords(_fileNames).intValue(), 4 * _numFiles);
    }
    
    @Test
    public void testWordsAndWourdCountsFJ() {
    	final TreeMap<String, Integer> wordsAndCountsFromFiles = WordCountFJ.getWordsAndCountsFromFiles(_fileNames);
    	for(final Map.Entry<String, Integer> entry : wordsAndCountsFromFiles.toMutableMap().entrySet()) {
    		System.out.println("Have " + entry.getKey() + ": " + entry.getValue());
    	}
		assertNotNull(wordsAndCountsFromFiles);
    }

}
