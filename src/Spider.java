import java.io.*;
import java.net.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import com.mongodb.*;


public class Spider extends Bot {

	/**
	 * Indexer 
	 */
	public static void main(String[] args) throws IOException {
		
		// get a list of documents to be indexed		
		DB db = Connect();	
		DBCollection documents = db.getCollection("documents");		
		DBCollection pipe = db.getCollection("pipe");
		// Extract the text from documents and adding the result to the pipe.
		DBCursor cursor = documents.find();
		for (DBObject document : cursor)
		{
			
		URL url = new URL(document.get("adress").toString());
		System.out.println("Adding "+url.getPath()+"...");
		try
		{
		  URLConnection urlConn = url.openConnection();
		  if(!urlConn.getContentType().equalsIgnoreCase("application/pdf"))
		  {
			  System.out.println("Sorry , the file is not supported");			  
		  }
		  else
		  {
			  try {
				  
				  InputStream input = urlConn.getInputStream();
				  PDFParser parser = new PDFParser(input);
				  parser.parse();
				  COSDocument doc = parser.getDocument();
				  PDFTextStripper stripper = new PDFTextStripper();
				  PDDocument pdoc = new PDDocument(doc);
				  String result = stripper.getText(pdoc);
				  doc.close();
				  input.close();
				  
				  // Insert the extracted text into the pipe
				  BasicDBObject rslt = new BasicDBObject("url",url.getPath()).
						  									append("text",result);
				  
				  pipe.insert(rslt);				 			  				  
			} 
			  
			  catch (Exception e) {
				// TODO: handle exception
			}
		  }
		  	
		}
		catch (Exception e)
		{
		 e.printStackTrace();
		}	

	}

}
}
