package shop.nongdam.nongdambackend.openai.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.nongdam.nongdambackend.openai.api.dto.Message;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTResponse {
    private List<Choice> choices;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Message message;

    }
}
