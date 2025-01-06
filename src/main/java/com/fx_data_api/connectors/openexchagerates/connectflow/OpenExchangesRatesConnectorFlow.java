package com.fx_data_api.connectors.openexchagerates.connectflow;

import java.io.File;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.dsl.HttpMessageHandlerSpec;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;

@Configuration
public class OpenExchangesRatesConnectorFlow {

	private static final Logger logger = LoggerFactory.getLogger(OpenExchangesRatesConnectorFlow.class);

	private static final String FILE_EXTENSION = ".json";

	private static final String FILE_NAME_PREFIX = "OPENEXCHANGERATES_";

	private static final String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd'T'HH-mm-ss'Z'";

	@Value("${OPEN_EXCHANGE_RATES_API_URL}")
	private String API_URL;

	@Value("${OPEN_EXCHANGE_RATES_APP_ID}")
	private String APP_ID;

	@Value("${OPEN_EXCHANGE_RATES_JSON_DIR}")
	private String exchangeRatesJsonDir;
	
	@Value("${OPEN_EXCHANGE_RATES_API_POLL_SECONDS}")	
	private Long exchangeRatesAPIPollSeconds;

	@Bean
	public IntegrationFlow exchangeRatesPollingFlow() {
		// Validate that appId is provided
		if (APP_ID == null || APP_ID.isEmpty()) {
			throw new IllegalStateException(
					"Environment variable 'OPEN_EXCHANGE_RATES_APP_ID' or 'openexchangerates.app_id' is not set.");
		}

		String uri = API_URL + "?app_id=" + APP_ID;

		logger.debug("API Endpoint URI: {}", uri);

		return IntegrationFlow
				.from(() -> new GenericMessage<>(""), c -> c.poller(Pollers.fixedDelay(Duration.ofSeconds(exchangeRatesAPIPollSeconds))))
				.handle(apiOutboundGatewayHandler(uri))
                .handle(jsonFileWritingMessageHandler())
                .get();

	}

	private MessageHandler apiOutboundGatewayHandler(String uri) {
		return Http.outboundGateway(uri).httpMethod(HttpMethod.GET).expectedResponseType(String.class).getObject();
	}
	
	private MessageHandler jsonFileWritingMessageHandler() {
		
		FileWritingMessageHandler fileWritingMessageHandler = new FileWritingMessageHandler(new File(exchangeRatesJsonDir));
        
		fileWritingMessageHandler.setFileNameGenerator(  message -> {
                    String fileName = ZonedDateTime.now(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern(FILE_NAME_DATE_FORMAT));
                    return FILE_NAME_PREFIX + fileName + FILE_EXTENSION;
                });
		
		return fileWritingMessageHandler;
	}
}
