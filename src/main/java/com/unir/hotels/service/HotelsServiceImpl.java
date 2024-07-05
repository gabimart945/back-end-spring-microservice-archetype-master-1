package com.unir.hotels.service;

import com.unir.hotels.data.DataAccessRepository;
import com.unir.hotels.data.HotelRepository;
import com.unir.hotels.facade.BookingsFacade;
import com.unir.hotels.model.db.HotelImage;
import com.unir.hotels.model.dto.BookingDto;
import com.unir.hotels.model.dto.HotelDto;
import com.unir.hotels.model.dto.SearchDto;
import com.unir.hotels.model.request.CreateUpdateHotelRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unir.hotels.model.db.Hotel;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.net.URLDecoder;


@Service
@Slf4j
public class HotelsServiceImpl implements HotelsService {

	@Autowired
	private HotelRepository repository;

	@Autowired
	private DataAccessRepository docRepository;

	@Autowired
	private BookingsFacade bookingsFacade;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public List<HotelDto> getDbHotels(String name, String maxPrice, String minPrice, String maxStars, String minStars,
									String maxOpinion, String minOpinion, String rooms, String facilities,
									Date startDate, Date endDate) {

		if (StringUtils.hasLength(name) || StringUtils.hasLength(maxPrice) || StringUtils.hasLength(minPrice)
				|| StringUtils.hasLength(maxStars) || StringUtils.hasLength(minStars)
				|| StringUtils.hasLength(maxOpinion) || StringUtils.hasLength(minOpinion)
				|| StringUtils.hasLength(rooms) || StringUtils.hasLength(facilities)
				|| (startDate != null && endDate != null)) {

			List<Hotel> hotels = repository.search(name, maxPrice, minPrice, maxStars, minStars, maxOpinion,
					minOpinion, rooms, facilities);

			if (startDate != null && endDate != null)
				return hotels.stream().map((Hotel hotel) -> calculateAvailability(hotel, startDate, endDate)).toList();
			return hotels.stream().map(HotelDto::new).toList();
		}

		List<HotelDto> hotels =  repository.getHotels().stream().map(HotelDto::new).toList();
		return hotels.isEmpty() ? null : hotels;
	}

	@Override
	public SearchDto getHotels(String name, String address, String description, String maxPrice, String minPrice, List<String> priceValues, String maxStars, String minStars,
							   List<String> starsValues, String maxOpinion, String minOpinion, List<String> opinionValues, String rooms, String searchInput, List<String> facilities,
							   Date startDate, Date endDate, String page) {

		// TODO: Esta mierda igual la puede hacer Spring con alguna configuracion
        try {
			if(StringUtils.hasLength(name)){
				name = URLDecoder.decode(name, "UTF-8");
			}
			if(StringUtils.hasLength(address)){
				address = URLDecoder.decode(address, "UTF-8");
			}
			if(StringUtils.hasLength(description)){
				description = URLDecoder.decode(description, "UTF-8");
			}
			if(StringUtils.hasLength(searchInput)){
				searchInput = URLDecoder.decode(searchInput, "UTF-8");
			}
			if(facilities!=null && !facilities.isEmpty()){
				facilities = facilities.stream().map(facility -> {
					try {
						return URLDecoder.decode(facility, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				}).toList();
			}

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        SearchDto result =  docRepository.findProducts(
				name,
				address,
				description,
				maxPrice,
				minPrice,
				priceValues,
				maxStars,
				minStars,
				starsValues,
				maxOpinion,
				minOpinion,
				opinionValues,
				searchInput,
				facilities,
				page);

		if (startDate != null && endDate != null)
			result.setHotels(result.getHotels().stream().map((HotelDto hotel) -> calculateAvailability(hotel, startDate, endDate)).toList());

		return result;
	}

	private HotelDto calculateAvailability(HotelDto hotel, Date startDate, Date endDate) {
		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);
		List<BookingDto> bookings = bookingsFacade.getBookings(hotel.getId().toString(), df.format(startDate), df.format(endDate));
		if(bookings!= null && !bookings.isEmpty()) {
			for (BookingDto booking : bookings) {
				hotel.setAvailableRooms(hotel.getAvailableRooms() - booking.getRooms());
			}
		}

		return hotel;
	}

	private HotelDto calculateAvailability(Hotel hotel, Date startDate, Date endDate) {
		HotelDto hotelDto = new HotelDto(hotel);
		return calculateAvailability(hotelDto, startDate, endDate);
	}

	@Override
	public HotelDto getHotel(String hotelId) {
		return new HotelDto(repository.getById(Long.valueOf(hotelId)));
	}

	@Override
	public Boolean removeHotel(String hotelId) {

		Hotel hotel = repository.getById(Long.valueOf(hotelId));

		if (hotel != null) {
			repository.delete(hotel);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Hotel createHotel(CreateUpdateHotelRequest request) {

		if (request != null && StringUtils.hasLength(request.getName().trim()) && request.getStars() != null
				&&  request.getPrice() != null && request.getOpinion() != null && request.getMaxRooms() != null
				&& StringUtils.hasLength(request.getContactMail().trim())
				&& StringUtils.hasLength(request.getFacilities().trim())
				&& StringUtils.hasLength(request.getContactNumber().trim())) {

			List<HotelImage> images = request.getImages().stream().map(
					path -> HotelImage.builder().path(path).build()).toList();

			Hotel hotel = Hotel.builder().name(request.getName()).price(request.getPrice()).stars(request.getStars())
					.opinion(request.getOpinion()).facilities(request.getFacilities()).maxRooms(request.getMaxRooms())
					.contactMail(request.getContactMail()).contactNumber(request.getContactNumber()).images(images)
					.build();

			return repository.save(hotel);
		} else {
			return null;
		}
	}

	@Override
	public Hotel updateHotel(String hotelId, String request) {

		Hotel hotel = repository.getById(Long.valueOf(hotelId));
		if (hotel != null) {
			try {
				JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
				JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(hotel)));
				Hotel patched = objectMapper.treeToValue(target, Hotel.class);
				patched = repository.save(patched);
				return patched;
			} catch (JsonProcessingException | JsonPatchException e) {
				log.error("Error updating hotel {}", hotelId, e);
                return null;
            }
        } else {
			return null;
		}
	}

	@Override
	public Hotel updateHotel(String hotelId, CreateUpdateHotelRequest updateRequest) {
		Hotel hotel = repository.getById(Long.valueOf(hotelId));
		if (hotel != null) {
			hotel.update(updateRequest);
			repository.save(hotel);
			return hotel;
		} else {
			return null;
		}
	}

}
