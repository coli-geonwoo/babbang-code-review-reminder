package coli.babbang.domain.reminder;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReminderMessage {

    MESSAGE1("“지금 리뷰 안 하면… 바빵이 식빵 됩니다 \uD83E\uDD76\uD83D\uDD25 제발 오븐 문 닫기 전에 봐주세요!!”"),
    MESSAGE2("“리뷰 없어서 바빵이 딱딱해지고 있어요 \uD83D\uDC94 나중엔 못 씹습니다 \uD83E\uDEE0”"),
    MESSAGE3("“경고 ⚠\uFE0F 바빵이가 식고 있습니다. 남은 시간 3... 2... 1... \uD83C\uDF5E\uD83D\uDCA3”"),
    MESSAGE4("“이 코드는 이제 미지근빵이에요 \uD83E\uDD7A 지금 아니면 바삭함은 영영 안 돌아옵니다 \uD83D\uDE2D”"),
    MESSAGE5("“리뷰요!! 리뷰요!! 지금이라도 구워야 해요 \uD83D\uDD25 식은 빵은 바빵이의 최대의 적!!”"),
    MESSAGE6("지금 당장 바게트 빵으로 곤장을 때리고 싶어요! 리뷰해주세요 \uD83D\uDCA3\uD83D\uDD25"),
    MESSAGE7("“바빵이의 인내심: 0.2% 남음 ⚠\uFE0F 다음 알림은 ‘빵 터짐’입니다 \uD83D\uDCA3\uD83C\uDF5E”"),
    MESSAGE8("“지금 리뷰 안 하면 내일 아침 ‘딱딱빵 사건’ 뉴스에 나옵니다. \uD83D\uDCF0”"),
    MESSAGE9("“바빵이 오늘도 새벽부터 반죽했어요. 이거 안 봐주면 진짜… 오븐 뒤엎습니다 \uD83D\uDE24”"),
    MESSAGE10("많이 바빵? ㅋㅋㅋ 안 바쁘면 리뷰해줘요 \uD83D\uDE07"),
    ;

    private final String message;

    public static String getOne() {
        ReminderMessage[] values = values();
        int randomIndex = ThreadLocalRandom.current().nextInt(values.length);
        return values[randomIndex].getMessage();
    }
}
