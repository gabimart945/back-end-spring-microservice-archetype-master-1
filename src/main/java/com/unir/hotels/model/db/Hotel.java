package com.unir.hotels.model.db;

import com.unir.hotels.model.request.CreateUpdateHotelRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Hotel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "description", columnDefinition="TEXT")
	private String description;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "stars")
	private Integer stars;
	
	@Column(name = "opinion")
	private Double opinion;

	@Column(name = "max_rooms")
	private Integer maxRooms;

	@Column(name = "contact_mail")
	private String contactMail;

	@Column(name = "facilities", columnDefinition="TEXT")
	private String facilities;

	@Column(name = "contact_number")
	private String contactNumber;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "hotel_id", referencedColumnName = "id")
	private List<HotelImage> images;

	public void update(CreateUpdateHotelRequest hotelDto) {
		this.name = hotelDto.getName();
		this.address = hotelDto.getAddress();
		this.description = hotelDto.getDescription();
		this.latitude = hotelDto.getLatitude();
		this.longitude = hotelDto.getLongitude();
		this.price = hotelDto.getPrice();
		this.stars = hotelDto.getStars();
		this.opinion = hotelDto.getOpinion();
		this.maxRooms = hotelDto.getMaxRooms();
		this.contactMail = hotelDto.getContactMail();
		this.facilities = hotelDto.getFacilities();
		this.contactNumber = hotelDto.getContactNumber();
		this.images.removeAll(this.getImages());
		hotelDto.getImages().forEach(path -> this.images.add(new HotelImage(path)));
	}

}
