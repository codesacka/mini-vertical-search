import java.net.UnknownHostException;

import com.mongodb.*;
public class Bot {
	
	public static DB Connect() throws UnknownHostException, MongoException
	{	   
	   // Specify the mongodb server : host + port
	   Mongo mongo = new Mongo("localhost", 27017);
	   DB db = mongo.getDB("fyp");
	return db;	     
	   
	}
	
}
