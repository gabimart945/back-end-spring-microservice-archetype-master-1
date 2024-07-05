package com.unir.hotels.model.dto;


import lombok.*;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SearchDto {

    private List<HotelDto> hotels;
    private Map<String, List<AggregationDetails>> aggregations;

}
