import command.Command;
import command.CommandFactory;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String input = "";
        while (true){
            System.out.print("$ ");
            input = sc.nextLine();
            String[] split = input.split(" ");

            Command cmd = CommandFactory.getCommand(split[0]);
            cmd.execute(split);
        }
    }
}
