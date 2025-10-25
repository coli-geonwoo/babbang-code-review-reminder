package coli.babbang.domain.reminder;

import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReminderType {

    REMINDER(ReminderMessage::getOne),
    MORNING(MorningMessage::getOne),
    ;

    private final Supplier<String> headerSupplier;

    public String getHeader() {
        return headerSupplier.get();
    }
}
