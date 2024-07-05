package com.unir.hotels.model.dto;

import com.unir.hotels.model.db.Hotel;
import com.unir.hotels.model.db.HotelImage;
import com.unir.hotels.model.doc.HotelDoc;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HotelDto {

	private Long id;
	private String name;
	private String address;
	private String description;
	private Double latitude;
	private Double longitude;
	private Double price;
	private Integer stars;
	private Double opinion;
	private Integer maxRooms;
	private String contactMail;
	private List<String> facilities;
	private String contactNumber;
	private Integer availableRooms;
	private List<String> images;


	public HotelDto(Hotel hotel) {
		this.id = hotel.getId();
		this.name = hotel.getName();
		this.price = hotel.getPrice();
		this.stars = hotel.getStars();
		this.opinion = hotel.getOpinion();
		this.maxRooms = hotel.getMaxRooms();
		this.contactMail = hotel.getContactMail();
		this.facilities = List.of(hotel.getFacilities().split(",  "));
		this.contactNumber = hotel.getContactNumber();
		this.availableRooms = hotel.getMaxRooms();
		this.images = hotel.getImages().stream().map(HotelImage::getPath).toList();
		this.description = hotel.getDescription();
		this.address = hotel.getAddress();
		this.latitude = hotel.getLatitude();
		this.longitude = hotel.getLongitude();
	}

	public HotelDto(HotelDoc hotel) {
		this.id = hotel.getId();
		this.name = hotel.getName();
		this.price = hotel.getPrice();
		this.stars = hotel.getStars();
		this.opinion = hotel.getOpinion();
		this.maxRooms = hotel.getMaxRooms();
		this.contactMail = hotel.getContactMail();
		this.facilities = hotel.getFacilities();
		this.contactNumber = hotel.getContactNumber();
		this.availableRooms = hotel.getMaxRooms();
		this.images = hotel.getImages();
		this.description = hotel.getDescription();
		this.address = hotel.getAddress();
		this.latitude = hotel.getLatitude();
		this.longitude = hotel.getLongitude();
	}

}
