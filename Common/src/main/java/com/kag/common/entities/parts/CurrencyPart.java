package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

/**
 *
 * @author Emil
 */
public class CurrencyPart implements IPart {

    private int currencyAmount;

    public CurrencyPart(int currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public int getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(int currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

}
