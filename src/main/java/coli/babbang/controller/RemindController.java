package coli.babbang.controller;

import coli.babbang.dto.request.ReminderCreateRequest;
import coli.babbang.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RemindController {

    private final ReminderService reminderService;

    @PostMapping("/api/reminder")
    public ResponseEntity<Void> registerReminder(
            @RequestBody ReminderCreateRequest request
    ) {
        reminderService.create(request);
        return ResponseEntity.ok().build();
    }
}
