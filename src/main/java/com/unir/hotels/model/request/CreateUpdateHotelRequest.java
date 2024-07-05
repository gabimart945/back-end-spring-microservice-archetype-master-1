package com.unir.hotels.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateHotelRequest {

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
	private String facilities;
	private String contactNumber;
	private List<String> images;

}
