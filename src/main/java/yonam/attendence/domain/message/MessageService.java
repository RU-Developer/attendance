package yonam.attendence.domain.message;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final Message coolsms;
    private HashMap<String, String> params = new HashMap<>();

    public MessageResult send(MessageForm messageForm) {
        params.put("to", messageForm.getTo());
        params.put("from", CoolsmsConnectionConst.MESSAGE_FROM_NUMBER);
        params.put("type", "sms");
        params.put("text", messageForm.getMessage());

        boolean success = true;
        String errorMessage = "";

        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            success = false;
            errorMessage = e.getMessage();
        }

        return new MessageResult(success, errorMessage);
    }
}
