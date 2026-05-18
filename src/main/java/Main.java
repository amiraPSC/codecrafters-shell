import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.print("$ ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (input.equals("exit")){
                break;
            }else if (input.startsWith("echo ")){
                String[] split = input.split(" ");
                for (int i = 1; i < split.length; i++){
                    System.out.print(split[i] + " ");
                }
                System.out.println();
            }
            System.out.println(input + ": command not found");
        }
    }
}
