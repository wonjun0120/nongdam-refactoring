package shop.nongdam.nongdambackend.openai.api.dto.request;

import lombok.Data;
import shop.nongdam.nongdambackend.openai.api.dto.Message;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String model, String prompt) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
