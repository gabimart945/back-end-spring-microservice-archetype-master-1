package com.unir.hotels.controller;

import com.unir.hotels.model.db.Hotel;
import com.unir.hotels.model.dto.HotelDto;
import com.unir.hotels.model.dto.SearchDto;
import com.unir.hotels.model.request.CreateUpdateHotelRequest;
import com.unir.hotels.service.HotelsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class HotelsController {

    private final HotelsService service;

    @GetMapping("/dbhotels")
    public ResponseEntity<List<HotelDto>> getDbHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxStars,
            @RequestParam(required = false) String minStars,
            @RequestParam(required = false) String maxOpinion,
            @RequestParam(required = false) String minOpinion,
            @RequestParam(required = false) String rooms,
            @RequestParam(required = false) String facilities,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate

    ) {

        List<HotelDto> hotels = service.getDbHotels(name, maxPrice, minPrice, maxStars, minStars, maxOpinion,
                minOpinion, rooms, facilities, startDate, endDate);

        if (hotels != null) {
            return ResponseEntity.ok(hotels);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/hotels")
    public ResponseEntity<SearchDto> getHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxStars,
            @RequestParam(required = false) String minStars,
            @RequestParam(required = false) String maxOpinion,
            @RequestParam(required = false) String minOpinion,
            @RequestParam(required = false) List<String> starsValues,
            @RequestParam(required = false) List<String> priceValues,
            @RequestParam(required = false) List<String> opinionValues,
            @RequestParam(required = false) String rooms,
            @RequestParam(required = false) String searchInput,
            @RequestParam(required = false) List<String> facilities,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
            @RequestParam(required = false, defaultValue = "0") String page) {

        SearchDto response = service.getHotels(name, address, description, maxPrice, minPrice, priceValues, maxStars, minStars,
                starsValues, maxOpinion, minOpinion, opinionValues, rooms, searchInput, facilities, startDate, endDate, page);

        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(new SearchDto());
        }
    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<HotelDto> getHotel(@PathVariable String hotelId) {

        HotelDto hotel = service.getHotel(hotelId);
        if (hotel != null) {
            return ResponseEntity.ok(hotel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/hotels/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable String hotelId) {

        Boolean removed = service.removeHotel(hotelId);
        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/hotels")
    public ResponseEntity<Hotel> addHotel(@RequestBody CreateUpdateHotelRequest request) {

        Hotel createdHotel = service.createHotel(request);
        if (createdHotel != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/hotels/{hotelId}")
    public ResponseEntity<Hotel> patchHotel(@PathVariable String hotelId, @RequestBody String patchBody) {

        Hotel patched = service.updateHotel(hotelId, patchBody);
        if (patched != null) {
            return ResponseEntity.ok(patched);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/hotels/{hotelId}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable String hotelId, @RequestBody CreateUpdateHotelRequest body) {

        Hotel updated = service.updateHotel(hotelId, body);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
