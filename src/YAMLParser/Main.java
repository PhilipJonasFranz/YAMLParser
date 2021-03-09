package YAMLParser;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		try {
			YAMLParser.parse(new File("res/test.yml")).print(System.out);
		} catch (MalformedYMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
