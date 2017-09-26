package com.podarbetweenus.interfaces.dataprovider;


import com.podarbetweenus.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
