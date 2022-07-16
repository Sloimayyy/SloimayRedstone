package me.sloimay.sredstone.tickers;

import java.util.ArrayList;
import java.util.List;

/**
 * Inits tickers, which are classes with only a tick() method that
 * gets run on the client every start client tick.
 */
public class ClientTickersInit
{
    // ### Fields

    // ###



    // ### Init

    public ClientTickersInit()
    {

    }

    // ###



    // ### Public methods

    public void initTickers()
    {
        // ## First create all the tickers objects and put them in the list
        List<ClientTicker> tickers = new ArrayList<ClientTicker>();
        tickers.add(new RedstoneNetworkPoolTimingTicker());

        // ## Register all tickers
        tickers.forEach(ticker -> ticker.register());
    }

    // ###
}
