package yonam.attendence.domain.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final Message coolsms;
    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, String> validationMap = new HashMap<>();
    private Timer timer = new Timer();

    public MessageResult send(MessageForm messageForm) {
        initializeParams(messageForm);

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

    public MessageResult sendPhoneValidation(String phone) {
        if (phone == null) {
            return new MessageResult(false, "전화번호가 입력되지 않았습니다.");
        }

        String validationCode = String.valueOf(generateValidationCode());
        String message = "학부모 휴대폰 인증번호 발송 메시지입니다. 인증번호 : [" + validationCode + "]";
        MessageForm messageForm = new MessageForm(message, phone);
        MessageResult result = send(messageForm);
        if (result.isSuccess()) {
            log.info("validationCode:phone = {}:{}", validationCode, phone);
            validationMap.put(validationCode, phone);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    log.info("validationCode removed = {}", validationCode);
                    validationMap.remove(validationCode);
                }
            }, 60_000); //60초 후 인증 시간 종료
            return result;
        }

        result.setMessage("인증코드 메시지 전송에 실패하였습니다.");
        log.info("message send 실패");
        return result;
    }

    public String phoneValidation(String validationCode) {
        if (validationMap.containsKey(validationCode)) {
            return validationMap.remove(validationCode); //해당번호 인증 완료
        }

        return null; //인증번호 틀림
    }

    private int generateValidationCode() {
        int validationLength = 6;
        Random random = new Random(System.currentTimeMillis());

        int range = (int) Math.pow(10, validationLength);
        int trim = (int) Math.pow(10, validationLength - 1);
        return random.nextInt(range) + trim;
    }

    private void initializeParams(MessageForm messageForm) {
        params.put("to", messageForm.getTo());
        params.put("from", CoolsmsConnectionConst.MESSAGE_FROM_NUMBER);
        params.put("type", "sms");
        params.put("text", messageForm.getMessage());
    }
}
