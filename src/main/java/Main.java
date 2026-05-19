import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String input = "";
        while (true){
            System.out.print("$ ");
            input = sc.nextLine();
            String[] split = input.split(" ");
            if (input.trim().equals("exit")){
                break;
            }

            if (input.startsWith("type ")){
                if (split[1].equals("exit") || split[1].equals("echo") || split[1].equals("type")){
                    System.out.println(split[1] + " is a shell builtin");
                }else{
                    searchInDirs(split[1]);
                }
            }else if (input.startsWith("echo ")){
                for (int i = 1; i < split.length; i++){
                    System.out.print(split[i] + " ");
                }
                System.out.println();
            }else {
                System.out.println(input + ": command not found");
            }
        }
    }

    private static void searchInDirs(String input){
        String path = System.getenv("PATH");
        String separator = System.getProperty("path.separator");
        String[] paths = path.split(separator);

        for (String path1 : paths){
            Path p = Paths.get(path1, input);
            if (p.getFileName().equals(input)){
                if (p.toFile().exists() && p.toFile().canExecute()){
                    System.out.println(input + " is " + p.toFile().getAbsolutePath());
                    return;
                }
            }
        }
        System.out.println(input + ": not found");
    }
}
