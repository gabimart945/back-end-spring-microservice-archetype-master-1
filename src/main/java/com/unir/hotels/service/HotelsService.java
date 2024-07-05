package com.unir.hotels.service;


import com.unir.hotels.model.db.Hotel;
import com.unir.hotels.model.dto.HotelDto;


import com.unir.hotels.model.dto.SearchDto;
import com.unir.hotels.model.request.CreateUpdateHotelRequest;

import java.util.Date;
import java.util.List;

public interface HotelsService {
	
	List<HotelDto> getDbHotels(String name, String maxPrice, String minPrice, String maxStars, String minStars,
							 String maxOpinion, String minOpinion, String guests, String facilities, Date startDate,
							 Date endDate);

	SearchDto getHotels(String name, String address, String description, String maxPrice, String minPrice,
						List<String> priceValues, String maxStars, String minStars, List<String> starsValues, String maxOpinion, String minOpinion,
						List<String> opinionValues, String guests, String searchInput, List<String> facilities, Date startDate, Date endDate, String page);

	HotelDto getHotel(String hotelId);
	
	Boolean removeHotel(String hotelId);

	Hotel createHotel(CreateUpdateHotelRequest request);

	Hotel updateHotel(String hotelId, String updateRequest);

	Hotel updateHotel(String hotelId, CreateUpdateHotelRequest updateRequest);

}
