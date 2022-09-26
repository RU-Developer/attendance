package yonam.attendence.domain.util;

public class Util {
    /**
     * 핸드폰 번호에서 특수문자를 제거하는 유틸리티입니다.
     * @param phone 핸드폰 번호
     * @return
     */
    public static String deleteSpecialSymbolsAtPhoneNumber(String phone) {
        if (phone == null) {
            return null;
        }

        return phone.replaceAll("[^0-9]", "");
    }
}
