package org.skroutz.elasticsearch.action.support;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class WordDelimiterActionListener implements ActionListener<SearchResponse> {
	private static WordDelimiterActionListener instance = null;
	private final ESLogger logger = ESLoggerFactory.getLogger(WordDelimiterActionListener.class.getSimpleName());
	private Set<String> protectedWords;
	
	protected WordDelimiterActionListener() {
		protectedWords = new HashSet<String>();
	}

	public void onResponse(SearchResponse response) {
		SearchHit[] hits = response.getHits().hits();
		Set<String> localProtectedWords = new HashSet<String>();

		String word;
		for (SearchHit hit : hits) {
			word = hit.getSource().get("word").toString();
			localProtectedWords.add(word);
		}

		protectedWords = localProtectedWords;
	}

	public void onFailure(Throwable e) {
		logger.error(e.getMessage());
	}
	
	public Set<String> getProtectedWords() {
		return protectedWords;
	}
	
	public synchronized static WordDelimiterActionListener getInstance() {
		if(instance == null) {
			instance = new WordDelimiterActionListener();
		}

		return instance;
	}
}
