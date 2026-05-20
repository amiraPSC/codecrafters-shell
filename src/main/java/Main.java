import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    private static Path currentDir =  Paths.get(System.getProperty("user.dir"));
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
                if (split[1].equals("exit") || split[1].equals("echo")
                        || split[1].equals("type") || split[1].equals("pwd")
                        || split[1].equals("cd")){
                    System.out.println(split[1] + " is a shell builtin");
                }else{
                    String result = searchInDirs(split[1]);
                    System.out.println(result);
                }
            }else if (input.startsWith("cd")){
                Path path = Paths.get(split[1]);
                if (path.toFile().exists()){
                    if (path.isAbsolute()) {
                        currentDir = path;
                    }
                }else {
                    System.out.println("cd: " + split[1] + ": No such file or directory");
                }
            }else if (input.startsWith("pwd")){
                System.out.println(currentDir);
            }else if (input.startsWith("echo ")){
                for (int i = 1; i < split.length; i++){
                    System.out.print(split[i] + " ");
                }
                System.out.println();
            }else {
                String resultSearch = searchInDirs(split[0]);
                if (!resultSearch.contains("not found")){
                    ProcessBuilder processBuilder = new ProcessBuilder(split);
                    Process process = processBuilder.start();
                    process.getInputStream().transferTo(System.out);
                    process.waitFor();
                }else {
                    System.out.println(input + ": command not found");
                }
            }
        }
    }

    private static String searchInDirs(String input){
        String path = System.getenv("PATH");
        String separator = System.getProperty("path.separator");
        String[] paths = path.split(separator);

        for (String path1 : paths){
            Path p = Paths.get(path1, input);
            if (p.toFile().exists() && p.toFile().canExecute()){
                return (input + " is " + p.toFile().getAbsolutePath());
            }
        }
        return (input + ": not found");
    }
}
