import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.PorterStemmer;


public class LuceneTest 
{
	static List<String> stopWords = new ArrayList<String>();
	static String db="testsys";
	static String table="stopwords";
	public static void main(String[] args)
	{
		//readStopWords();
		try {
			//stemmize1("Pretty good dinner with a nice selection of food. Open 24 hours and provide nice service. I usually go here after a night of partying. My favorite dish is the Fried Chicken Eggs Benedict.")
			System.out.println(removeStopWordsAndStem("cakes curies"));
		//	System.out.println( Constant.reviewWords.size());
		//	AprioriMining.doCalculation(new String[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static  String removeStopWordsAndStem(String input) throws IOException {
	    
		PorterStemmer stemmer = new PorterStemmer();
	    stemmer.setCurrent(input);
	    stemmer.stem();
	    return stemmer.getCurrent();
	}
	
	
	public static String stemmize(String text) throws IOException {

		StringBuffer result = new StringBuffer();
        if (text!=null && text.trim().length()>0){
            StringReader tReader = new StringReader(text);
            CharArraySet stopSet = new CharArraySet(Version.LUCENE_4_10_1, stopWords, true);
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_1,stopSet); //EnglishAnalyzer
            TokenStream tStream = analyzer.tokenStream("contents", tReader);
            CharTermAttribute charTermAttribute = tStream.addAttribute(CharTermAttribute.class);
            tStream.reset();
            try {
                while (tStream.incrementToken()){
                	String s=charTermAttribute.toString();
                	Constant.reviewWords.add(s);
                    result.append(s);
                    result.append(" ");
                }
            } catch (IOException ioe){
                System.out.println("Error: "+ioe.getMessage());
            }
            tStream.end();
            tStream.close();
        }

        // If, for some reason, the stemming did not happen, return the original text
        if (result.length()==0)
            result.append(text);
        
       
        return result.toString().trim();
	}
	
	  public static String stemmize1(String text) throws IOException {

	        StringBuffer result = new StringBuffer();
	        if (text!=null && text.trim().length()>0){
	            StringReader tReader = new StringReader(text);
	            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_1); //EnglishAnalyzer
	            TokenStream tStream = analyzer.tokenStream("contents", tReader);
	            CharTermAttribute charTermAttribute = tStream.addAttribute(CharTermAttribute.class);
	            tStream.reset();
	            try {
	                while (tStream.incrementToken()){
	                    result.append(charTermAttribute.toString());
	                    result.append(" ");
	                }
	            } catch (IOException ioe){
	                System.out.println("Error: "+ioe.getMessage());
	            }
	            tStream.end();
	            tStream.close();
	        }

	        // If, for some reason, the stemming did not happen, return the original text
	        if (result.length()==0)
	            result.append(text);


	        return result.toString().trim();
	    }
	
	public static void  readStopWords()
	{
		Connection con = null;

		try{

		  Class.forName("com.mysql.jdbc.Driver");
		  con = DriverManager.getConnection
		("jdbc:mysql://localhost:3306/"+db,"root",""); //change
		  try{
		  Statement st = con.createStatement();
		  ResultSet res = st.executeQuery
		("SELECT word FROM "+"stopwords");  //Join two tables
		  
		  while(res.next()){
		  String review = res.getString("word");
		  stopWords.add(review);
		  System.out.println(review );
		  }
		  }
		  catch (SQLException s){
		  System.out.println("SQL statement is not executed!"+s);
		  }
		 }
	      catch (Exception e){
	      e.printStackTrace();
	     }
	}
}