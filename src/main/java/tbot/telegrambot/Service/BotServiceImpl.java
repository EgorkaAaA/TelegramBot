package tbot.telegrambot.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tbot.telegrambot.Dto.FigiesDto;
import tbot.telegrambot.Dto.PriceDto;
import tbot.telegrambot.Dto.TickersDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotServiceImpl implements BotService {
    private String messageToUser;

    private final RestTemplate restTemplate;

    private String clientUrl = "http://localhost:8005";

    @Override
    public String messageDo(String textFromUserMessage) {
        if(textFromUserMessage.equals("/start")) {
            startMessage();
            return messageToUser;
        }
        messageToUser = "";
        List<PriceDto> prices = getPrices(getFigis(Arrays.asList(textFromUserMessage.split(","))));
        prices.forEach(p -> messageToUser+=p.toString());
        return messageToUser;
    }

    @Override
    public void startMessage() {
        messageToUser = "Привет инвестро скинь мне свои тикеры через запятую " +
                "и я скажу стоимость твоего портфеля на данный момент\n" +
                "Если ты зашел побаловаться без облигаций напиши:\n" +
                "RU000A0ZYLC4,RU000A100A66";
    }

    @Override
    public List<PriceDto> getFigis(List<String> tickers) {
        log.info(new JSONArray(tickers).toString());

        var stock = restTemplate.postForEntity(clientUrl + "/bonds/getBondsByTickers/", new TickersDto(tickers), String.class).getBody();
        log.info(stock);
        return jsonParseFigi(stock);
    }

    @Override
    public List<PriceDto> getPrices(List<PriceDto> figis) {
        var prices = restTemplate.postForEntity(clientUrl + "/bonds/prices",
                new FigiesDto(figis.stream().map(PriceDto::getFigi).collect(Collectors.toList())),
                String.class).getBody();
        JSONObject jsonObject = new JSONObject(prices);
        log.info(prices);
        var arr = jsonObject.getJSONArray("prices");
        for(int i = 0; i < arr.length(); i++) {
            figis.get(i).setPrice(Double.parseDouble(arr.getJSONObject(i).get("price").toString()));
        }
        return figis;
    }

    private List<PriceDto> jsonParseFigi(String stocks) {
        JSONObject jsonObject = new JSONObject(stocks);
        List<PriceDto> figis = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("stocks");
            for (int i = 0; i < jsonArray.length(); i++) {
                PriceDto priceDto = new PriceDto();
                priceDto.setFigi(jsonArray.getJSONObject(i).get("figi").toString());
                priceDto.setName(jsonArray.getJSONObject(i).get("name").toString());
                priceDto.setCurrency(jsonArray.getJSONObject(i).get("currency").toString());
                figis.add(priceDto);
            }
            return figis;
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        return figis;
    }
}
