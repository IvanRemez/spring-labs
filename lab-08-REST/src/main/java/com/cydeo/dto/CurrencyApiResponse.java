package com.cydeo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyApiResponse {

    private Boolean success;
    private String terms;
    private String privacy;
    private Long timestamp;
    private String source;
    private Map<String, Double> quotes;
    // ^^ Separate JSON Body in API Response
        // (Currency abbreviation, Exchange rate)
}
