import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String input = "";
        do {
            System.out.print("$ ");
            input = sc.nextLine();
            String[] split = input.split(" ");
            if (input.startsWith("type ")){
                if (split[1].equals("exit") || split[1].equals("echo") || split[1].equals("type")){
                    System.out.println(split[1] + " is a shell builtin");
                }else  {
                    System.out.println(split[1] + ": not found");
                }
            }else if (input.startsWith("echo ")){
                for (int i = 1; i < split.length; i++){
                    System.out.print(split[i] + " ");
                }
                System.out.println();
            }else {
                System.out.println(input + ": command not found");
            }
        }while (!input.equals("exit"));
    }
}
