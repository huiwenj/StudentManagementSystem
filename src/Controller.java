import ui.UserInterface;

/**
 * Main controller class for the application.
 * @author Xuanhe Zhang, Huiwen Jia, Haoran Hua
 */
public class Controller {
    public static void main(String[] args) {
        try {
            UserInterface ui = new UserInterface();
            ui.start();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
