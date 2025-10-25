package coli.babbang.domain.reminder;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MorningMessage {

    MESSAGE1("“혹시 너무 바빵…? 바빵이가 기다리다 식겠어요 \uD83D\uDE2D”"),
    MESSAGE2("“리뷰 안 하면 바빵이 눅눅빵 됩니다 \uD83E\uDD76 조금만 봐주세요!”"),
    MESSAGE3("“리뷰 안 해주시면… 바빵이 상합니다. (이미 좀 식었어요 \uD83E\uDD76)”"),
    MESSAGE4("“리뷰가 없어서 바빵이가 퍽퍽해졌어요. 수분 좀 주세요 \uD83D\uDE2D\uD83D\uDCA6”"),
    MESSAGE5("“여기 따끈한 코드 하나 있습니다. 지금 리뷰 안 하면 내일은 딱딱빵 됩니다 \uD83D\uDE24”"),
    MESSAGE6("“리뷰요… 리뷰요… 바빵이가 리뷰 중독이라 리뷰 없으면 금단증세와요 \uD83C\uDF00\uD83C\uDF5E”"),
    MESSAGE7("“오븐은 이미 예열 완료 \uD83D\uDD25 오늘의 첫 리뷰, 함께 굽지 않으실래요? \uD83E\uDD50”"),
    MESSAGE8("“커피는 드셨죠? ☕ 그럼 리뷰빵 하나 곁들여보세요 \uD83C\uDF5E 하루가 바삭해집니다~”"),
    MESSAGE9("“바빵이가 새벽부터 반죽했어요. 근데 리뷰 없으면 반죽으로 ‘치대기 모드’ 돌입합니다 \uD83D\uDCAA\uD83E\uDD56”"),
    MESSAGE10("“오븐은 예열 완료\uD83D\uDD25 코드도 발효 완료✨ 리뷰만 미완료… 이거 진짜 위험한 조합이에요 \uD83D\uDE2D”"),
    ;

    private final String message;

    public static String getOne() {
        MorningMessage[] values = values();
        int randomIndex = ThreadLocalRandom.current().nextInt(values.length);
        return values[randomIndex].getMessage();
    }
}
