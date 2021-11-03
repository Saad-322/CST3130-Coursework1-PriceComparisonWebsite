package mdx.saad.webscraper;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		HibernateExample example = new HibernateExample();
		example.init();
		// TODO Auto-generated method stub
		Website1 w1 = new Website1("RIGHTMOVE",example);//RightMove
		Website2 w2 = new Website2("OPENRENT",example);//OpenRent
		Website3 w3 = new Website3("ZOOPLA",example);//IdealFlatemate
		Website4 w4 = new Website4("GUMTREE",example);//GumTree
		w1.start();
		w2.start();
		w3.start();
		w4.start();
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		while (!userInput.equals("stop")) {
			userInput = scanner.nextLine();
		}		
		w1.interrupt();
		w2.interrupt();
		w3.interrupt();
		w4.interrupt();
		//Wait for threads to finish - join can throw an InterruptedException
        try{
            w1.join();
            w2.join();
            w3.join();
            w4.join();
        }
        catch(InterruptedException ex){
            System.out.println("Interrupted exception thrown: " + ex.getMessage());
        }
        example.shutDown();
        System.out.println("Web scraping complete");
	}

}
