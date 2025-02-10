package com.xm.technical.cryptoinvestment.controller;

import com.xm.technical.cryptoinvestment.model.CryptoDetails;
import com.xm.technical.cryptoinvestment.model.CryptoNormalizedRange;
import com.xm.technical.cryptoinvestment.model.DateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/cryptos")
public interface CryptoControllerInterface {

    @GetMapping("/normalized")
    @Operation(summary = "Calculates the normalized range for each crypto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "normalized range for calculated"),
            @ApiResponse(responseCode = "409", description = "Exception thrown when no cryptos were found")
    })
    List<CryptoNormalizedRange> cryptoNormalizedRangeDetails();
    @GetMapping("/details/{cryptoName}")
    @Operation(summary = "Returns Max/Min/Oldest/Newest for a specific crypto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Max/Min/Oldest/Newest values returned"),
            @ApiResponse(responseCode = "409", description = "Exception thrown if crypto is not supported")
    })
    CryptoDetails cryptoDetails(@PathVariable String cryptoName);
    @PostMapping("/normalized")
    @Operation(summary = "Returns the highest normalized range and crypto name for a specific day")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HighestNormalizedRange for this day calculated"),
            @ApiResponse(responseCode = "409", description = "Exception thrown if no normalized range found for this day")
    })
    CryptoNormalizedRange highestNormalizedThisDay(@RequestBody DateRequestDTO date);

}
