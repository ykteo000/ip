public class TaskTracker {
	public static void main(String[] args) {
		
		// Define a divider line using underscores
		String line = "_".repeat(60);
       
		// Define the TaskTracker banner logo
		String banner = "  ______           __                 \n"
                    + " /_  ______ ______/ /__                \n"
                    + "  / / / __ `/ ___/ //_/                \n"
                    + " / / / /_/ (__  / ,<                   \n"
                    + "/_______,_/____/_/|_|   __            \n"
                    + " /_  ___________ ______/ /_____  _____\n"
                    + "  / / / ___/ __ `/ ___/ //_/ _ \\/ ___/\n"
                    + " / / /  / /_/ / /__/ ,< /  __/ /    \n"
                    + "/_/ /_/   \\__,_/\\___/_/|_|\\___/_/     \n";

		System.out.println(line);
		System.out.println(banner);
		System.out.println("Hello there! I'm TaskTracker.");
		System.out.println("I am a chatbot used to track your tasks.\n");
		System.out.println("What can I do for you today?\n");
		System.out.println(line);
		System.out.println("Bye! See you again!\n");
		System.out.println(line);
	}
}
