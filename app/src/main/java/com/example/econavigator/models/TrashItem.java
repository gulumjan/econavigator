package com.example.econavigator.models;

public class TrashItem {
    private String name;
    private TrashType type;
    private String emoji;

    public enum TrashType {
        PLASTIC("Пластик"),
        PAPER("Бумага"),
        GLASS("Стекло"),
        METAL("Металл"),
        ORGANIC("Органика");

        private String russianName;

        TrashType(String russianName) {
            this.russianName = russianName;
        }

        public String getRussianName() {
            return russianName;
        }
    }

    public TrashItem(String name, TrashType type, String emoji) {
        this.name = name;
        this.type = type;
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public TrashType getType() {
        return type;
    }

    public String getEmoji() {
        return emoji;
    }
}