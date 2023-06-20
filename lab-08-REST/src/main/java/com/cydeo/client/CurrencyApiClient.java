package com.cydeo.client;

import com.cydeo.dto.CurrencyApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(url = "http://apilayer.net/api", name = "currencyApiClient")
public interface CurrencyApiClient {

    // http://apilayer.net/api  --> baseUrl

    // /live    --> Endpoint
    // ?
    // access_key=b95bf4595a674eeb829906ce9819e8d4
    // &currencies=EUR,TRY
    // &source=USD
    // &format=1
        // ^^ PARAMS

    @GetMapping("/live")
    Map<String, Object> getCurrencyRates(@RequestParam("access_key") String accessKey,
                                             @RequestParam("currencies") String currencies,
                                             @RequestParam("source") String source,
                                             @RequestParam("format") int format);
// ^^ Casting to a Collection response (Key always String, Value different Objects)
// allows for general API Response so no need to create multiple Static ApiResponse DTO Classes
}
