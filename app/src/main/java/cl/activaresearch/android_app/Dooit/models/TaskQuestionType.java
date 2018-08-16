package cl.activaresearch.android_app.Dooit.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used as
 *
 * @author Virendra Kachhi
 * @version 1.0
 * @since 13 Jul,2018
 */
public enum TaskQuestionType {
    SingleLineText(1),
    MultiLineText(2),
    SimpleAlternative(3),
    MultiAlternative(4),
    Image(5),
    Video(6),
    Location(7),
    Audio(8);

    private static Map map = new HashMap<>();

    static {
        for (TaskQuestionType pageType : TaskQuestionType.values()) {
            map.put(pageType.value, pageType);
        }
    }

    private int value;

    private TaskQuestionType(int value) {
        this.value = value;
    }

    public static TaskQuestionType valueOf(int qType) {
        return (TaskQuestionType) map.get(qType);
    }

    public int getValue() {
        return value;
    }
}
