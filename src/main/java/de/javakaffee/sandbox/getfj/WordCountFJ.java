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

import static fj.Monoid.longAdditionMonoid;
import static fj.Ord.stringOrd;
import static fj.control.parallel.ParModule.parModule;
import static fj.data.LazyString.fromStream;
import static fj.data.List.list;
import static fj.data.Stream.fromString;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import fj.F;
import fj.F2;
import fj.Unit;
import fj.control.parallel.ParModule;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.data.LazyString;
import fj.data.List;
import fj.data.Stream;
import fj.data.TreeMap;
import fj.function.Characters;

/**
 * @author Martin Grotzke
 */
public class WordCountFJ {

	// reads the given files and returns their content as char stream
	private static final F<String, Stream<Character>> readFile = new F<String, Stream<Character>>() {
		@Override
		public Stream<Character> f(final String fileName) {
			try {
				return fromString(readFileToString(new File(fileName)));
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
	// counts words from the given char stream
	private static final F<Stream<Character>, Long> countWords = new F<Stream<Character>, Long>() {
		@Override
		public Long f(final Stream<Character> document) {
			return (long) fromStream(document).split(Characters.isWhitespace).length();
		}
	};
	
	// map of words to their counts (occurrences)
    private static final F2<TreeMap<String, Integer>, LazyString, TreeMap<String, Integer>> wordsAndCounts =
    		new F2<TreeMap<String, Integer>, LazyString, TreeMap<String, Integer>>() {
		@Override
		public TreeMap<String, Integer> f(final TreeMap<String, Integer> map,
				final LazyString word) {
			final String wordAsString = word.toString();
			return map.set(wordAsString, map.get(wordAsString).orSome(0) + 1);
		}
	};
	
	private static final F<LazyString, List<LazyString>> wordsFromLazyString = new F<LazyString, List<LazyString>>() {
		@Override
		public List<LazyString> f(final LazyString a) {
			return a.split(Characters.isWhitespace).toList();
		}
	};

	// Read documents and count words of documents in parallel
	private static Promise<Long> countWordsFromFiles(final List<String> fileNames,
			final ParModule m) {
		return m.parFoldMap(fileNames, readFile.andThen(countWords), longAdditionMonoid);
	}
	
	// Read documents and extract words and word counts of documents
	public static TreeMap<String, Integer> getWordsAndCountsFromFiles(final List<String> fileNames) {
		return fileNames.map(readFile).map(fromStream).bind(wordsFromLazyString).foldLeft(wordsAndCounts, TreeMap.<String, Integer> empty(stringOrd));
	}

	// Count words of documents in parallel
	private static Promise<Long> countWords(
			final List<Stream<Character>> documents, final ParModule m) {
		return m.parFoldMap(documents, countWords, longAdditionMonoid);
	}

	// Main program does the requisite IO gymnastics
	public static Long countWords(final String... fileNames) {
		return countWords(list(fileNames));
	}

	// Main program does the requisite IO gymnastics
	public static Long countWords(final List<String> fileNames) {

		final ExecutorService pool = newFixedThreadPool(1);
		final ParModule m = parModule(Strategy.<Unit> executorStrategy(pool));

		// Long wordCount = countWords(fileNames.map(readFile), m).claim();
		final Long wordCount = countWordsFromFiles(fileNames, m).claim();
		System.out.println("Word Count: " + wordCount);

		pool.shutdown();

		return wordCount;
	}

}
