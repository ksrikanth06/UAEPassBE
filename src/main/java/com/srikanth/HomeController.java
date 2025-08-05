package com.srikanth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.client.RestClientException;

@RestController
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static final Logger apiAccessLogger = LoggerFactory.getLogger("API_ACCESS");

	@GetMapping("/redirectURL")
	public String home(@RequestParam String code, @RequestParam String state) {
		logger.info("Received redirect callback with code: {} and state: {}", 
				code != null ? "***" + code.substring(Math.max(0, code.length() - 4)) : "null", state);
		apiAccessLogger.info("GET /redirectURL - Code received, State: {}", state);
		
		try {
			// Log successful processing
			logger.debug("Processing redirect URL with code length: {}", code != null ? code.length() : 0);
			return code;
		} catch (Exception e) {
			logger.error("Error processing redirect URL: ", e);
			throw e;
		}
	}

	@GetMapping("/getToken")
	public ResponseEntity<String> sucess(@RequestParam String authCode) {
		logger.info("Token request initiated for auth code: {}", 
				authCode != null ? "***" + authCode.substring(Math.max(0, authCode.length() - 4)) : "null");
		apiAccessLogger.info("GET /getToken - Token exchange request started");

		try {
			RestTemplate restTemplate = new RestTemplate();

			// URL with query parameters
			String url = "https://stg-id.uaepass.ae/idshub/token" + "?grant_type=authorization_code"
					+ "&redirect_uri=srikanth://redirect" + "&code=" + authCode;

			logger.debug("Making token request to URL: {}", url.replaceAll("code=[^&]*", "code=***"));

			// Headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("Authorization", "Basic c2FuZGJveF9zdGFnZTpzYW5kYm94X3N0YWdl");

			// Since you're using multipart/form-data, even without form fields:
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			// Send POST request
			logger.debug("Sending POST request to UAE Pass token endpoint");
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			// Log response (without sensitive data)
			logger.info("Token request completed successfully with status: {}", response.getStatusCode());
			logger.debug("Response body length: {}", response.getBody() != null ? response.getBody().length() : 0);
			apiAccessLogger.info("GET /getToken - Token exchange successful, Status: {}", response.getStatusCode());
			
			return response;
		} catch (RestClientException e) {
			logger.error("REST client error during token request: ", e);
			apiAccessLogger.info("GET /getToken - Token exchange failed: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error during token request: ", e);
			apiAccessLogger.info("GET /getToken - Unexpected error: {}", e.getMessage());
			throw e;
		}
	}

	@GetMapping("/getUserInfo")
	public ResponseEntity<String> falure(@RequestParam String authToken) {
		logger.info("User info request initiated");
		apiAccessLogger.info("GET /getUserInfo - User info request started");

		try {
			String url = "https://stg-id.uaepass.ae/idshub/userinfo";
			logger.debug("Making user info request to URL: {}", url);
			
			RestTemplate restTemplate = new RestTemplate();
			// Set Authorization header
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(authToken);

			// Create request
			HttpEntity<String> entity = new HttpEntity<>(headers);

			// Make GET request
			logger.debug("Sending GET request to UAE Pass userinfo endpoint");
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			logger.info("User info request completed successfully with status: {}", response.getStatusCode());
			logger.debug("User info response body length: {}", response.getBody() != null ? response.getBody().length() : 0);
			apiAccessLogger.info("GET /getUserInfo - User info request successful, Status: {}", response.getStatusCode());

			return response;
		} catch (RestClientException e) {
			logger.error("REST client error during user info request: ", e);
			apiAccessLogger.info("GET /getUserInfo - User info request failed: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error during user info request: ", e);
			apiAccessLogger.info("GET /getUserInfo - Unexpected error: {}", e.getMessage());
			throw e;
		}
	}
}
