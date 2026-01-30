package pl.farmapp.backend.mapper;

import pl.farmapp.backend.dto.TradeOfPepperDto;
import pl.farmapp.backend.entity.TradeOfPepper;

public class TradeOfPepperMapper {

    public static TradeOfPepperDto toDto(TradeOfPepper e) {
        TradeOfPepperDto d = new TradeOfPepperDto();
        d.id = e.getId();
        d.farmerId = e.getFarmer().getId();
        d.pointOfSaleId = e.getPointOfSale().getId();
        d.tradeDate = e.getTradeDate();
        d.pepperClass = e.getPepperClass();
        d.pepperColor = e.getPepperColor();
        d.tradePrice = e.getTradePrice();
        d.tradeWeight = e.getTradeWeight();
        d.vatRate = e.getVatRate();
        return d;
    }
}
