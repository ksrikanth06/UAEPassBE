package com.srikanth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeController {


	@GetMapping("/redirectURL")
	public String home(@RequestParam String code, @RequestParam String state) {
		System.out.println("Code: " + code + " State: " + state);
		return code;
	}

	@GetMapping("/getToken")
	public ResponseEntity<String> sucess(@RequestParam String authCode) {

		RestTemplate restTemplate = new RestTemplate();

		// URL with query parameters
		String url = "https://stg-id.uaepass.ae/idshub/token" + "?grant_type=authorization_code"
				+ "&redirect_uri=srikanth://redirect" + "&code=" + authCode;

		// Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", "Basic c2FuZGJveF9zdGFnZTpzYW5kYm94X3N0YWdl");

		// Since you're using multipart/form-data, even without form fields:
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		// Send POST request
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

		// Print response
		System.out.println("Response: " + response.getBody());
		return response;
	}

	@GetMapping("/getUserInfo")
	public ResponseEntity<String> falure(@RequestParam String authToken) {
		String url = "https://stg-id.uaepass.ae/idshub/userinfo";
		RestTemplate restTemplate = new RestTemplate();
		// Set Authorization header
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);

		// Create request
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// Make GET request
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		return response;
	}

}
