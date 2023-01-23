import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static HashMap<String, String> substitutions = new HashMap<>();

    public static void main(String[] args) {
        initialiseSubstitutions();
        generateNewWordList();
    }

    private static void showPasswordCount() {
        int count = 0;

        try {
            File file = new File("../new-word-list.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                scanner.nextLine();
                count++;
            }

            System.out.println(count);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void generateNewWordList() {
        addAlphanumericEntriesToFile("../top-100-passwords.txt", "../lowercase-passwords.txt");
        addAlphanumericEntriesToFile("../top-100-passwords.txt", "../new-word-list.txt");
        addTitleCaseEntriesToFile("../lowercase-passwords.txt", "../new-word-list.txt");
        addUppercaseEntriesToFile("../lowercase-passwords.txt", "../new-word-list.txt");
        addSubstitutedEntriesToFile("../new-word-list.txt", "../new-word-list.txt");
    }

    private static void initialiseSubstitutions() {
        substitutions.put("s", "5");
        substitutions.put("S", "5");
        substitutions.put("5", "$");
        substitutions.put("i", "!");
        substitutions.put("I", "1");
        substitutions.put("t", "7");
        substitutions.put("T", "7");
        substitutions.put("e", "3");
        substitutions.put("E", "3");
        substitutions.put("g", "9");
        substitutions.put("G", "6");
        substitutions.put("o", "0");
        substitutions.put("O", "0");
        substitutions.put("b", "8");
        substitutions.put("B", "8");
        substitutions.put("A", "4");
        substitutions.put("a", "@");
    }

    private static boolean entryIsNumeric(String entry) {
        Matcher numberMatcher = NUMBER_PATTERN.matcher(entry);
        return numberMatcher.matches();
    }

    private static void addSubstitutedEntriesToFile(String inputFile, String outputFile) {
        try {
            File file = new File(inputFile);
            Scanner scanner = new Scanner(file);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            while (scanner.hasNextLine()) {
                String password = scanner.nextLine();

                if (entryIsNumeric(password)) {
                    continue;
                }

                String substitutedEntry = addSubstitutionToEntry(password);

                while (!substitutedEntry.equals(password)) {
                    fileWriter.write(substitutedEntry + "\n");
                    password = substitutedEntry;
                    substitutedEntry = addSubstitutionToEntry(password);
                }
            }

            fileWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String addSubstitutionToEntry(String entry) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] splitEntry = entry.split("");

        int i;

        for (i = 0; i < splitEntry.length; i++) {
            String character = splitEntry[i];

            if (substitutions.containsKey(character)) {
                stringBuilder.append(substitutions.get(character));
                break;
            } else {
                stringBuilder.append(substitutions.getOrDefault(character, character));
            }
        }

        i++;

        while (i < splitEntry.length) {
            stringBuilder.append(splitEntry[i]);
            i++;
        }

        return stringBuilder.toString();
    }

    private static void addAlphanumericEntriesToFile(String inputFile, String outputFile) {
        try {
            File file = new File(inputFile);
            Scanner scanner = new Scanner(file);
            FileWriter fileWriter = new FileWriter(outputFile);

            while (scanner.hasNextLine()) {
                String password = scanner.nextLine();

                if (entryIsNumeric(password)) {
                    continue;
                }

                fileWriter.write(password + "\n");
            }

            fileWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addUppercaseEntriesToFile(String inputFile, String outputFile) {
        try {
            File file = new File(inputFile);
            Scanner scanner = new Scanner(file);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            while (scanner.hasNextLine()) {
                String password = scanner.nextLine();

                if (entryIsNumeric(password)) {
                    continue;
                }

                fileWriter.write(password.toUpperCase() + "\n");
            }

            fileWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String titleCaseEntry(String entry) {
        String[] characters = entry.split("");
        characters[0] = characters[0].toUpperCase();
        return String.join("", characters);
    }

    private static void addTitleCaseEntriesToFile(String inputFile, String outputFile) {
        try {
            File file = new File(inputFile);
            Scanner scanner = new Scanner(file);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            while (scanner.hasNextLine()) {
                String password = scanner.nextLine();

                if (entryIsNumeric(password)) {
                    continue;
                }

                fileWriter.write(titleCaseEntry(password) + "\n");
            }

            fileWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}