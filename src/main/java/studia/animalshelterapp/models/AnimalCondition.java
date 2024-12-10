package studia.animalshelterapp.models;

public enum AnimalCondition {
    ZDROWE {
        @Override
        public String toString() {
            return "Zdrowe";
        }
    },
    CHORE{
        @Override
        public String toString() {
            return "Chore";
        }
    },
    W_TRAKCIE_ADOPCJI{
        @Override
        public String toString() {
            return "W trakcie adopcji";
        }
    },
    KWARANTANNA{
        @Override
        public String toString() {
            return "Kwarantanna";
        }
    };

    public static AnimalCondition fromString(String condition) {
        return switch(condition) {
            case "Zdrowe" -> ZDROWE;
            case "Chore" -> CHORE;
            case "W trakcie adopcji" -> W_TRAKCIE_ADOPCJI;
            case "Kwarantanna" -> KWARANTANNA;
            default -> null;
        };
    }
}