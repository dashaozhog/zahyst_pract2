import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CipherApp {

    // окремо великі і малі літери (по 33 літери)
    static String big = "АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ";
    static String small = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя";

    // зсуває одну літеру на shift позицій.
    // якщо це не літера - повертає її без змін.
    static char shiftLetter(char c, int shift) {
        int pos = big.indexOf(c);          // шукаємо серед великих
        if (pos != -1) {
            int newPos = (pos + shift) % 33;
            if (newPos < 0) {              // якщо вийшло мінус - додаємо 33
                newPos = newPos + 33;
            }
            return big.charAt(newPos);
        }

        pos = small.indexOf(c);            // шукаємо серед малих
        if (pos != -1) {
            int newPos = (pos + shift) % 33;
            if (newPos < 0) {
                newPos = newPos + 33;
            }
            return small.charAt(newPos);
        }

        return c;                          // пробіл, кома і тд
    }

    // повертає номер літери в алфавіті або -1, якщо це не літера
    static int letterNumber(char c) {
        int pos = big.indexOf(c);
        if (pos != -1) {
            return pos;
        }
        pos = small.indexOf(c);
        return pos;                        // якщо не знайшли - тут буде -1
    }

    //цезар

    static String caesar(String text, int key, boolean encrypt) {
        int shift = key;
        if (!encrypt) {
            shift = -key;                  // для дешифрування зсуваємо назад
        }

        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            result = result + shiftLetter(c, shift);
        }
        return result;
    }

    // віженер

    static String vigenere(String text, String keyword, boolean encrypt) {
        // скільки літер у ключі
        int keyLength = 0;
        for (int i = 0; i < keyword.length(); i++) {
            if (letterNumber(keyword.charAt(i)) != -1) {
                keyLength++;
            }
        }
        if (keyLength == 0) {
            return "Помилка: у ключі немає літер!";
        }

        // масив зсувів з літер ключа
        int[] shifts = new int[keyLength];
        int k = 0;
        for (int i = 0; i < keyword.length(); i++) {
            int num = letterNumber(keyword.charAt(i));
            if (num != -1) {
                shifts[k] = num;
                k++;
            }
        }

        // шифруємо текст
        String result = "";
        int keyIndex = 0;                  // яка зараз літера ключа
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (letterNumber(c) == -1) {
                result = result + c;       // не літера - просто додаємо
            } else {
                int shift = shifts[keyIndex];
                if (!encrypt) {
                    shift = -shift;
                }
                result = result + shiftLetter(c, shift);

                keyIndex++;                // беремо наступну літеру ключа
                if (keyIndex == keyLength) {
                    keyIndex = 0;          // дійшли до кінця - починаємо спочатку
                }
            }
        }
        return result;
    }

    // гамування

    // робимо з ключового слова число
    static long makeSeed(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        long seed = 0;
        for (int i = 0; i < keyBytes.length; i++) {
            int b = keyBytes[i] & 0xFF;                  // байт без знаку
            seed = (seed * 31 + b) & 0xFFFFFFFFL;        // тримаємо в межах 32 біт
        }
        return seed;
    }

    // XOR кожного байта даних з псевдовипадковим байтом
    static byte[] xorData(byte[] data, String key) {
        long next = makeSeed(key);
        byte[] result = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            // Лінійний конгруентний генератор (як у C++ з завдання)
            next = (next * 1103515245L + 12345L) & 0xFFFFFFFFL;
            long random = (next / 65536) % 32768;        // RAND_MAX + 1 = 32768

            int gamma = (int) (random % 256);            // беремо один байт
            result[i] = (byte) (data[i] ^ gamma);
        }
        return result;
    }

    // байти - рядок HEX
    static String bytesToHex(byte[] data) {
        String hex = "";
        for (int i = 0; i < data.length; i++) {
            int b = data[i] & 0xFF;
            String part = Integer.toHexString(b).toUpperCase();
            if (part.length() == 1) {
                part = "0" + part;                       // 5 -> 05
            }
            hex = hex + part;
        }
        return hex;
    }

    // рядок HEX - байти
    static byte[] hexToBytes(String hex) {
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < data.length; i++) {
            String part = hex.substring(i * 2, i * 2 + 2);
            data[i] = (byte) Integer.parseInt(part, 16);
        }
        return data;
    }

    // автотест

    static void runTests() {
        System.out.println("=== Завдання 1: Цезар, ключ 5 ===");
        String surname = "Ожог";
        String enc1 = caesar(surname, 5, true);
        System.out.println("Вихідний текст: " + surname);
        System.out.println("Шифротекст:     " + enc1);
        System.out.println("Дешифрування:   " + caesar(enc1, 5, false));

        System.out.println();
        System.out.println("=== Завдання 2: Віженер, ключ КЛЮЧ ===");
        String phrase = "КІБЕРБЕЗПЕКА ЦЕ ВАЖЛИВО";
        String enc2 = vigenere(phrase, "КЛЮЧ", true);
        System.out.println("Вихідний текст: " + phrase);
        System.out.println("Шифротекст:     " + enc2);
        System.out.println("Дешифрування:   " + vigenere(enc2, "КЛЮЧ", false));

        System.out.println();
        System.out.println("=== Завдання 3: XOR, ключ ТАЄМНИЦЯ ===");
        String msg = "Привіт, світе!";
        byte[] enc3 = xorData(msg.getBytes(StandardCharsets.UTF_8), "ТАЄМНИЦЯ");
        byte[] dec3 = xorData(enc3, "ТАЄМНИЦЯ");         // другий XOR = розшифрування
        System.out.println("Вихідний текст:   " + msg);
        System.out.println("Шифротекст (HEX): " + bytesToHex(enc3));
        System.out.println("Повторний XOR:    " + new String(dec3, StandardCharsets.UTF_8));
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        boolean work = true;

        while (work) {
            System.out.println();
            System.out.println("===== МЕНЮ =====");
            System.out.println("1 - Шифр Цезаря");
            System.out.println("2 - Шифр Віженера");
            System.out.println("3 - Гамування (XOR)");
            System.out.println("4 - Автотест");
            System.out.println("0 - Вихід");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                work = false;

            } else if (choice.equals("1")) {
                System.out.print("Режим (1 - шифрування, 2 - дешифрування): ");
                String mode = scanner.nextLine();
                System.out.print("Введіть текст: ");
                String text = scanner.nextLine();
                System.out.print("Введіть ключ (від 1 до 32): ");
                int key = Integer.parseInt(scanner.nextLine());

                if (key < 1 || key > 32) {
                    System.out.println("Ключ має бути від 1 до 32!");
                } else {
                    boolean encrypt = mode.equals("1");
                    System.out.println("Результат: " + caesar(text, key, encrypt));
                }

            } else if (choice.equals("2")) {
                System.out.print("Режим (1 - шифрування, 2 - дешифрування): ");
                String mode = scanner.nextLine();
                System.out.print("Введіть текст: ");
                String text = scanner.nextLine();
                System.out.print("Введіть ключове слово: ");
                String key = scanner.nextLine();

                boolean encrypt = mode.equals("1");
                System.out.println("Результат: " + vigenere(text, key, encrypt));

            } else if (choice.equals("3")) {
                System.out.print("Режим (1 - шифрування, 2 - дешифрування): ");
                String mode = scanner.nextLine();

                if (mode.equals("1")) {
                    System.out.print("Введіть текст: ");
                    String text = scanner.nextLine();
                    System.out.print("Введіть ключове слово: ");
                    String key = scanner.nextLine();

                    byte[] encrypted = xorData(text.getBytes(StandardCharsets.UTF_8), key);
                    System.out.println("Шифротекст (HEX): " + bytesToHex(encrypted));
                } else {
                    System.out.print("Введіть шифротекст (HEX): ");
                    String hex = scanner.nextLine();
                    System.out.print("Введіть ключове слово: ");
                    String key = scanner.nextLine();

                    byte[] decrypted = xorData(hexToBytes(hex), key);
                    System.out.println("Розшифровано: " + new String(decrypted, StandardCharsets.UTF_8));
                }

            } else if (choice.equals("4")) {
                runTests();

            } else {
                System.out.println("Такого пункту немає!");
            }
        }
    }
}