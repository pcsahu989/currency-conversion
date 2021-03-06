package com.develop2learn.currency.conversion.controller;

import com.develop2learn.currency.conversion.bean.CurrencyConversion;
import com.develop2learn.currency.conversion.proxy.CurrencyExchangeProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;

    private Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class.getName());

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}" )
    public CurrencyConversion calculateConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {

        logger.info("calculateConversion(...) Calculating conversion value from {} to {}", from, to);
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
        CurrencyConversion currencyConversion = responseEntity.getBody();
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(currencyConversion.getEnvironment());

        return currencyConversion;
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}" )
    public CurrencyConversion calculateConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {

        logger.info("calculateConversionFeign(...) Calculating conversion value from {} to {}", from, to);
        CurrencyConversion currencyConversion = currencyExchangeProxy.getExchangeValue(from, to);
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(currencyConversion.getEnvironment());

        return currencyConversion;
    }
}
