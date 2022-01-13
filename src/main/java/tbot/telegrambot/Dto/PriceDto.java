package tbot.telegrambot.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceDto {
    private String name;

    private double price;

    private String figi;

    private String currency;

    @Override
    public String toString() {
        return "Название: " + name + " Цена: " + price + " " + currency +"\n";
    }
}
