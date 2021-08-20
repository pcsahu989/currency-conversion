package com.develop2learn.currency.conversion.proxy;

import com.develop2learn.currency.conversion.bean.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange", url = "localhost:8000")
//Kubernetes Change
//@FeignClient(name = "currency-exchange")
//@FeignClient(name = "currency-exchange", url = "${CURRENCY_EXCHANGE_SERVICE_HOST:http://localhost}:8000") //commenting this because if currency-exchange service is not up while creating currency-conversion, this predefined variable will not available
@FeignClient(name = "currency-exchange", url = "${CURRENCY_EXCHANGE_SERVICE_URI:http://localhost}:8000") //So using custom environment variables
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversion getExchangeValue(@PathVariable String from, @PathVariable String to);
}
