import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import com.mongodb.*;

public class Indexer extends Bot {

	/**
	 * Indexer 
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException, ParseException {
	
		     // Specify the directory of the index	
		
				RAMDirectory index = new RAMDirectory();
				// Specify the elements of the indexer
				IndexWriter writer = new IndexWriter(index,new StandardAnalyzer(Version.LUCENE_30), MaxFieldLength.LIMITED);	
				DB db = Connect();
				DBCollection pipe = db.getCollection("pipe");
				DBCursor cursor = pipe.find();
				for (DBObject document : cursor)
				{
					// a document is the basic unit to be added to the index.
					
					Document doc = new Document();
					
					// Specify the part of pipe to be indexed.
					
					doc.add(new Field("url",(String) document.get("url"),Field.Store.YES,Field.Index.NO));
					doc.add(new Field("text",(String) document.get("text"),Field.Store.YES,Field.Index.ANALYZED));
					
					writer.addDocument(doc);
					System.out.println("Document Added.");			
					
				}
				
				writer.optimize();
				writer.close();
				
				// Search for a document 
				
				IndexSearcher searcher = new IndexSearcher(index);		
				QueryParser parser = new QueryParser(Version.LUCENE_30,"text",new StandardAnalyzer(Version.LUCENE_30));
				Query query = parser.parse("METU");
				
				int hitpages = 10;
				TopScoreDocCollector collector = TopScoreDocCollector.create(5 * hitpages, false);
				searcher.search(query, collector);
				
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				
				int count = collector.getTotalHits();
				
				System.out.println(count+" Documents found.");
				
				for (int i = 0; i < hits.length; i++) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					System.out.println(d.get("url"));
					System.out.println(docId);
				}			
				
				searcher.close();
				
}

}