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

import static fj.Function.uncurryF2;
import static fj.Monoid.longAdditionMonoid;
import static fj.Monoid.monoid;
import static fj.Ord.stringOrd;
import static fj.control.parallel.ParModule.parModule;
import static fj.data.LazyString.fromStream;
import static fj.data.LazyString.str;
import static fj.data.List.list;
import static fj.function.Integers.add;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;

import fj.F;
import fj.F2;
import fj.Monoid;
import fj.Ord;
import fj.Unit;
import fj.control.parallel.ParModule;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.data.LazyString;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;
import fj.data.TreeMap;
import fj.function.Characters;

/**
 * @author Martin Grotzke
 */
public class WordCountFJ {
	
	private static final F2<Integer, Integer, Integer> integersAdd = uncurryF2(add);

	// reads the given files and returns their content as char stream
	private static final F<String, LazyString> readFileToLazyString = new F<String, LazyString>() {
		@Override
		public LazyString f(final String fileName) {
			try {
				return str(readFileToString(new File(fileName)));
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
	/**
	 * counts words from the given char stream
	 * @deprecated Use {@link #countWordsFromLazyString} instead
	 */
	@Deprecated
	private static final F<Stream<Character>, Long> countWords = new F<Stream<Character>, Long>() {
		@Override
		public Long f(final Stream<Character> document) {
			return (long) fromStream(document).split(Characters.isWhitespace).length();
		}
	};
	
	// counts words from the given char stream
	private static final F<LazyString, Long> countWordsFromLazyString = new F<LazyString, Long>() {
		@Override
		public Long f(final LazyString document) {
			return (long) document.split(Characters.isWhitespace).length();
		}
	};
	
	// map of words to their counts (occurrences)
    private static final F2<TreeMap<String, Integer>, LazyString, TreeMap<String, Integer>> wordsAndCounts =
    		new F2<TreeMap<String, Integer>, LazyString, TreeMap<String, Integer>>() {
		@Override
		public TreeMap<String, Integer> f(final TreeMap<String, Integer> map,
				final LazyString word) {
			return map.update(word.toString(), add.f(1), Integer.valueOf(1));
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
		return m.parFoldMap(fileNames, readFileToLazyString.andThen(countWordsFromLazyString), longAdditionMonoid);
	}
	
	// Read documents and extract words and word counts of documents
	public static TreeMap<String, Integer> getWordsAndCountsFromFiles(final List<String> fileNames) {
		return fileNames.map(readFileToLazyString).bind(wordsFromLazyString)
				.foldLeft(wordsAndCounts, TreeMap.<String, Integer> empty(stringOrd));
	}
	
	public static TreeMap<String, Integer> getWordsAndCountsFromFilesInParallel(
			final List<String> fileNames) {
		final ExecutorService pool = newFixedThreadPool(16);
		final ParModule m = parModule(Strategy.<Unit> executorStrategy(pool));

		// Long wordCount = countWords(fileNames.map(readFile), m).claim();
		final TreeMap<String, Integer> result = getWordsAndCountsFromFiles(fileNames, m).claim();

		pool.shutdown();

		return result;
	}
	
	// Read documents and extract words and word counts of documents
	public static Promise<TreeMap<String, Integer>> getWordsAndCountsFromFiles(
			final List<String> fileNames, final ParModule m) {
		final F<TreeMap<String, Integer>, F<TreeMap<String, Integer>, TreeMap<String, Integer>>> treeMapSum = new F<TreeMap<String, Integer>, F<TreeMap<String, Integer>, TreeMap<String, Integer>>>() {
			@Override
			public F<TreeMap<String, Integer>, TreeMap<String, Integer>> f(
					final TreeMap<String, Integer> a) {
				return new F<TreeMap<String, Integer>, TreeMap<String, Integer>>() {

					@Override
					public TreeMap<String, Integer> f(final TreeMap<String, Integer> b) {
						return plus(a, b, integersAdd, stringOrd);
					}
					
				};
			}
			
		};
		final F<String, TreeMap<String, Integer>> fileNameToWordsAndCounts = new F<String, TreeMap<String, Integer>>() {
			@Override
			public TreeMap<String, Integer> f(final String a) {
				return wordsFromLazyString.f(readFileToLazyString.f(a)).foldLeft(wordsAndCounts, TreeMap.<String, Integer> empty(stringOrd));
			}
		};
		final Monoid<TreeMap<String, Integer>> monoid = monoid(treeMapSum, TreeMap.<String, Integer> empty(stringOrd));
		return m.parFoldMap(fileNames, fileNameToWordsAndCounts, monoid);
	}
	
	private static <K, V> TreeMap<K, V> plus(final TreeMap<K, V> a, final TreeMap<K, V> b, final F2<V, V, V> update, final Ord<K> ord) {
		if(a.isEmpty()) {
			return b;
		}
		else if (b.isEmpty()) {
			return a;
		}
		final Map<K, V> ma = a.toMutableMap();
		// Update all entries in a by adding the values of matching keys from b
		for(final Entry<K, V> entry : ma.entrySet()) {
			final Option<V> value = b.get(entry.getKey());
			if(value.isSome()) {
				entry.setValue(update.f(entry.getValue(), value.some()));
			}
		}
		// Add all entries from b that are not already in a
		for(final Entry<K, V> entry : b.toMutableMap().entrySet()) {
			if(!ma.containsKey(entry.getKey())) {
				ma.put(entry.getKey(), entry.getValue());
			}
		}
		return TreeMap.fromMutableMap(ord, ma);
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
