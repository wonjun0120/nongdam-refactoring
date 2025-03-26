package shop.nongdam.nongdambackend.openai.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.nongdam.nongdambackend.openai.application.OpenAiService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/openai")
public class OpenAiController {
    private final OpenAiService openAiService;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt") String prompt) {
        return openAiService.chat(prompt);
    }
}
