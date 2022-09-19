package yonam.attendence.web.teacher;

import java.util.HashMap;
import java.util.Map;

public class TeacherAddResultConstToMessage {
    private final Map<String, String> errorMessageToMessageMap;

    public TeacherAddResultConstToMessage() {
        errorMessageToMessageMap = new HashMap<>();
        errorMessageToMessageMap.put(TeacherAddResultConst.DUPLICATE_ID, "이미 등록된 아이디입니다.");
    }

    public String resolveMessage(String origin) {
        return errorMessageToMessageMap.getOrDefault(origin, origin);
    }
}
