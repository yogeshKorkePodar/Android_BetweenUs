package com.podarbetweenus.interfaces.dataprovider;


import com.podarbetweenus.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
