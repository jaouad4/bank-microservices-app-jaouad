package ma.jaouad.bank.chatbotservice.controllers;

import lombok.RequiredArgsConstructor;
import ma.jaouad.bank.chatbotservice.models.ChatRequest;
import ma.jaouad.bank.chatbotservice.models.ChatResponse;
import ma.jaouad.bank.chatbotservice.services.ChatBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatBotController {

    private final ChatBotService chatBotService;

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(@RequestBody ChatRequest request) {
        if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ChatResponse.builder()
                            .answer("La question ne peut pas être vide.")
                            .build());
        }
        
        ChatResponse response = chatBotService.askQuestion(request.getQuestion());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestionGet(@RequestParam String question) {
        if (question == null || question.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ChatResponse.builder()
                            .answer("La question ne peut pas être vide.")
                            .build());
        }
        
        ChatResponse response = chatBotService.askQuestion(question);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/simple")
    public ResponseEntity<ChatResponse> simpleQuestion(@RequestBody ChatRequest request) {
        ChatResponse response = chatBotService.getSimpleAnswer(request.getQuestion());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chat-Bot Service is running!");
    }
}
