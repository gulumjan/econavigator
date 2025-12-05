package com.example.econavigator.utils;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%&*";

    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARS;
    private static final SecureRandom random = new SecureRandom();

    /**
     * Генерировать случайный пароль
     *
     * @param length длина пароля (рекомендуется 8-12)
     * @return сгенерированный пароль
     */
    public static String generatePassword(int length) {
        if (length < 6) {
            length = 8; // минимум 8 символов
        }

        StringBuilder password = new StringBuilder(length);

        // Обязательно добавляем по одному символу каждого типа
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Заполняем остальное случайными символами
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Перемешиваем символы
        return shuffleString(password.toString());
    }

    /**
     * Генерировать простой пароль (только буквы и цифры)
     * Для отправки ученикам
     *
     * @param length длина пароля
     * @return сгенерированный пароль
     */
    public static String generateSimplePassword(int length) {
        if (length < 6) {
            length = 8;
        }

        String simpleChars = UPPERCASE + LOWERCASE + DIGITS;
        StringBuilder password = new StringBuilder(length);

        // Обязательно добавляем хотя бы одну букву и цифру
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        // Заполняем остальное
        for (int i = 3; i < length; i++) {
            password.append(simpleChars.charAt(random.nextInt(simpleChars.length())));
        }

        return shuffleString(password.toString());
    }

    /**
     * Генерировать запоминающийся пароль
     * Формат: Слово + Цифры (например: "Eco2024")
     *
     * @return сгенерированный пароль
     */
    public static String generateMemorablePassword() {
        String[] words = {
                "Eco", "Green", "Nature", "Earth", "Clean",
                "Planet", "Save", "World", "Tree", "Ocean"
        };

        String word = words[random.nextInt(words.length)];
        int number = 1000 + random.nextInt(9000); // 4-значное число

        return word + number;
    }

    /**
     * Генерировать пароль на основе имени ученика
     * Формат: ПервыеБуквыИмени + Год + СлучайноеЧисло
     * Например: "Bek2024#42"
     *
     * @param studentName имя ученика
     * @return сгенерированный пароль
     */
    public static String generateStudentPassword(String studentName) {
        if (studentName == null || studentName.isEmpty()) {
            return generateSimplePassword(8);
        }

        // Берём первые 3 буквы имени
        String prefix = studentName.length() >= 3 ?
                studentName.substring(0, 3) : studentName;
        prefix = prefix.substring(0, 1).toUpperCase() +
                prefix.substring(1).toLowerCase();

        // Добавляем год
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        // Добавляем случайное число
        int randomNum = 10 + random.nextInt(90);

        return prefix + year + "#" + randomNum;
    }

    /**
     * Перемешать символы в строке
     */
    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }

    /**
     * Проверить надёжность пароля
     *
     * @param password пароль для проверки
     * @return уровень надёжности (1-5)
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return 1; // Очень слабый
        }

        int strength = 0;

        // Длина
        if (password.length() >= 8) strength++;
        if (password.length() >= 12) strength++;

        // Содержит цифры
        if (password.matches(".*\\d.*")) strength++;

        // Содержит заглавные и строчные буквы
        if (password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) {
            strength++;
        }

        // Содержит спецсимволы
        if (password.matches(".*[!@#$%&*].*")) strength++;

        return Math.min(strength, 5);
    }

    /**
     * Получить текстовое описание надёжности пароля
     */
    public static String getPasswordStrengthText(int strength) {
        switch (strength) {
            case 1: return "Очень слабый";
            case 2: return "Слабый";
            case 3: return "Средний";
            case 4: return "Сильный";
            case 5: return "Очень сильный";
            default: return "Неизвестно";
        }
    }
}