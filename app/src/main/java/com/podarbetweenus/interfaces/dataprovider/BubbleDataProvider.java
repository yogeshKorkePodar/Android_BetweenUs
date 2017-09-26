package com.podarbetweenus.interfaces.dataprovider;

import com.podarbetweenus.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
