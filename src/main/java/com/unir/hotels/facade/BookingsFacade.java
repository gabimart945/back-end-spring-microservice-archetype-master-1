package com.unir.hotels.facade;

import com.unir.hotels.model.dto.BookingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingsFacade {

  @Value("${getBookings.url}")
  private String getBookingsUrl;

  private final RestTemplate restTemplate;

  public List<BookingDto> getBookings(String hotelId, String startDate, String endDate) {

    try {
      String url = String.format(getBookingsUrl, hotelId, startDate, endDate);
      log.info("Getting bookings for hotel with ID {}. Request to {}", hotelId, url);
      return List.of(Objects.requireNonNull(restTemplate.getForObject(url, BookingDto[].class)));
    } catch (HttpClientErrorException e) {
      log.error("Client Error: {}, Product with ID {}", e.getStatusCode(), hotelId);
      return null;
    } catch (HttpServerErrorException e) {
      log.error("Server Error: {}, Product with ID {}", e.getStatusCode(), hotelId);
      return null;
    } catch (Exception e) {
      log.error("Error: {}, Product with ID {}", e.getMessage(), hotelId);
      return null;
    }
  }

}
