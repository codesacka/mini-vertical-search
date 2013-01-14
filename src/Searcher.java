import java.io.IOException;

import org.apache.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
public class Searcher {

	/**
	 *  Searcher
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ParseException, IOException {
		
		RAMDirectory index = new RAMDirectory();
		IndexSearcher searcher = new IndexSearcher(index);		
		QueryParser parser = new QueryParser(Version.LUCENE_30,"text",new StandardAnalyzer(Version.LUCENE_30));
		Query query = parser.Query("database");
		
		int hitpages = 10;
		TopScoreDocCollector collector = TopScoreDocCollector.create(5 * hitpages, false);
		searcher.search(query, collector);
		
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		int count = collector.getTotalHits();
		
		System.out.println(count+" Documents found.");
		searcher.close();

	}

}
