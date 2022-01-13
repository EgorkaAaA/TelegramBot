package tbot.telegrambot.Config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
public class BotConfig {
    @Value("${key.bot_name}")
    private String botName;

    @Value("${key.bot_token}")
    private String botToken;
}
