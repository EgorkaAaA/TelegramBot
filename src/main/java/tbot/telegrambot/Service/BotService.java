package tbot.telegrambot.Service;

import tbot.telegrambot.Dto.PriceDto;

import java.util.List;

public interface BotService {
    String messageDo(String textFromUserMessage);

    void startMessage();

    List<PriceDto> getFigis(List<String> tickers);

    List<PriceDto> getPrices(List<PriceDto> figis);
}
