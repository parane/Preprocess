import java.io.IOException;

import org.tartarus.snowball.ext.PorterStemmer;


public class PosterStemmer {

	public static void main(String[] args) {
		try {
			System.out.println(removeStopWordsAndStem("cakes curies"));
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
	
	
}
