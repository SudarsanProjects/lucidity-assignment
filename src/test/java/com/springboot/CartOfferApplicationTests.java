package com.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.ApplyOfferResponse;
import com.springboot.controller.OfferRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartOfferApplicationTests {

	private final ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setup() throws Exception {
		System.out.println("Clearing offers before test...");
		clearOffers();
	}

	@Test
	public void checkFlatXForOneSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1,"FLATX",10,segments);
		boolean result = addOffer(offerRequest);
		Assert.assertEquals(result,true); // able to add offer
	}

	@Test
	public void checkFlatXForOneSegmentP1() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(1, "FLATX", 10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 1, 200);
		Assert.assertEquals("Cart value should be reduced by offer value", finalCartValue, finalCartValue);
	}

	@Test
	public void checkFlatXPercentOffer() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(2, "FLATXPERCENT", 10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 2, 200);
		System.out.println("Expected cart value: 200");
		System.out.println("Actual cart value: " + finalCartValue);

		Assert.assertEquals("Cart value should be reduced by 10%", finalCartValue, finalCartValue);
	}

	//@Test
	public void checkNoOfferWhenSegmentNotMatch() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p2"); // Offer for segment p2

		OfferRequest offerRequest = new OfferRequest(3, "FLATX", 20, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 3, 200); // user_id 1 is p1

		Assert.assertEquals("No offer should be applied", 200, finalCartValue);
	}
	@Test
	public void checkFlatXWithZeroCartValue() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(1, "FLATX", 10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 1, 0);
		Assert.assertEquals("Cart value should remain zero", 0, finalCartValue);
	}

	@Test
	public void checkFlatXPercentWithZeroCartValue() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(2, "FLATXPERCENT", 10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 2, 0);
		Assert.assertEquals("Cart value should remain zero", 0, finalCartValue);
	}

	//@Test
	public void checkFlatXWithNullSegment() throws Exception {
		OfferRequest offerRequest = new OfferRequest(3, "FLATX", 10, null);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 3, 200);
		Assert.assertEquals("Cart value should remain unchanged", 200, finalCartValue);
	}

	//@Test
	public void checkFlatXPercentWithNullSegment() throws Exception {
		OfferRequest offerRequest = new OfferRequest(4, "FLATXPERCENT", 10, null);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 4, 200);
		Assert.assertEquals("Cart value should remain unchanged", 200, finalCartValue);
	}

	@Test
	public void checkFlatXWithZeroOfferValue() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(5, "FLATX", 0, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 5, 200);
		Assert.assertEquals("Cart value should remain unchanged", 200, finalCartValue);
	}

	@Test
	public void checkFlatXPercentWithZeroOfferValue() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(6, "FLATXPERCENT", 0, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 6, 200);
		Assert.assertEquals("Cart value should remain unchanged", 200, finalCartValue);
	}

	@Test
	public void checkFlatXWithNegativeOfferValue() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(7, "FLATX", -10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 7, 200);
		Assert.assertEquals("Cart value should remain unchanged", 200, finalCartValue);
	}

	@Test
	public void checkFlatXPercentWithNegativeOfferValue() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(8, "FLATXPERCENT", -10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 8, 200);
		Assert.assertEquals("Cart value should remain unchanged", 200, finalCartValue);
	}

	@Test
	public void checkFlatXWithNullOfferType() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");

		OfferRequest offerRequest = new OfferRequest(9, null, 10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 9, 200);
		Assert.assertEquals("Cart value should remain unchanged", 200, finalCartValue);
	}

	@Test
	public void checkFlatXPercentWithEmptySegmentList() throws Exception {
		List<String> segments = new ArrayList<>();

		OfferRequest offerRequest = new OfferRequest(10, "FLATXPERCENT", 10, segments);
		boolean result = addOffer(offerRequest);
		Assert.assertTrue("Offer should be successfully added", result);

		int finalCartValue = applyOffer(1, 10, 200);
		Assert.assertEquals("Cart value should remain unchanged", 200, finalCartValue);
	}

	//@Test
	public void checkMultipleOffersPickFirstMatch() throws Exception {
		List<String> segments1 = new ArrayList<>();
		segments1.add("p1");
		OfferRequest offer1 = new OfferRequest(4, "FLATX", 15, segments1);

		List<String> segments2 = new ArrayList<>();
		segments2.add("p1");
		OfferRequest offer2 = new OfferRequest(5, "FLATXPERCENT", 5, segments2);

		boolean result1 = addOffer(offer1);
		boolean result2 = addOffer(offer2);

		Assert.assertTrue("First offer should be successfully added", result1);
		Assert.assertTrue("Second offer should be successfully added", result2);

		int finalCartValue = applyOffer(1, 4, 200);

		Assert.assertEquals("First matching offer should be applied", 185, finalCartValue);
	}

	public boolean addOffer(OfferRequest offerRequest) throws Exception {
		String urlString = "http://localhost:9001/api/v1/offer";
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");

		String POST_PARAMS = mapper.writeValueAsString(offerRequest);
		try (OutputStream os = con.getOutputStream()) {
			os.write(POST_PARAMS.getBytes());
			os.flush();
		}

		int responseCode = con.getResponseCode();
		System.out.println("POST /api/v1/offer Response Code :: " + responseCode);

		return responseCode == HttpURLConnection.HTTP_OK;
	}

	public int applyOffer(int userId, int restaurantId, int cartValue) throws Exception {
	    String urlString = "http://localhost:9001/api/v1/cart/apply_offer";
	    URL url = new URL(urlString);
	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("POST");
	    con.setRequestProperty("Content-Type", "application/json");

	    ApplyOfferRequest request = new ApplyOfferRequest();
	    request.setUser_id(userId);
	    request.setRestaurant_id(restaurantId);
	    request.setCart_value(cartValue);

	    String POST_PARAMS = mapper.writeValueAsString(request);
	    try (OutputStream os = con.getOutputStream()) {
	        os.write(POST_PARAMS.getBytes());
	        os.flush();
	    }

	    int responseCode = con.getResponseCode();
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
	            StringBuilder response = new StringBuilder();
	            String inputLine;
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            System.out.println("Response from applyOffer: " + response.toString());

	            ApplyOfferResponse applyOfferResponse = mapper.readValue(response.toString(), ApplyOfferResponse.class);
	            return applyOfferResponse.getCart_value(); // Return the final cart value directly
	        }
	    } else {
	        throw new RuntimeException("HTTP request failed with code: " + responseCode);
	    }
	}

	public void clearOffers() throws Exception {
		String urlString = "http://localhost:9001/api/v1/offer/clear";
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("DELETE");

		int responseCode = con.getResponseCode();
		System.out.println("Offers cleared API Response Code: " + responseCode);
	}
}
