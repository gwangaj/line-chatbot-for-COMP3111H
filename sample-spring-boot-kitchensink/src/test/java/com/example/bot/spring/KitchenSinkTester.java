package com.example.bot.spring;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.example.bot.spring.DatabaseEngine;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
// @SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { KitchenSinkTester.class,  FaqDatabase.class })
public class KitchenSinkTester {
	@Autowired
	private SQLDatabaseEngine databaseEngine;

	@Autowired
	private FaqDatabase faqEngine;


	@Test
	public void testTour() throws Exception {
		//for testing this class
		Tour tour = new Tour("ID","Name","Description",2);
		TourOffering offering = new TourOffering("offeringID", "date", "guideName", "guideAccount",
				"hotel", 40, 5);
		tour.addTourOffering(offering);
		tour.removeTourOffering(offering);
		System.out.println(tour.getTourID());
		System.out.println(offering.getGuideName());
		assertThat(tour.getTourID()).isEqualTo("ID");
		assertThat(offering.getOfferingID()).isEqualTo("offeringID");

	}

	@Test
	public void testSQL_7() throws Exception {
		//for testing this class
		boolean thrown = false;
		boolean result = false;
		try {
			result = this.databaseEngine.tourOfferingFound(12,1);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void testSQL_10() throws Exception {
		//for testing this class
		boolean thrown = false;
		String result = null;
		try {
			result = this.databaseEngine.displayTourOffering(12);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isNotEqualTo("null");
	}


	@Test
	public void testSQL_12() throws Exception {
		//for testing this class
		boolean thrown = false;
		int result = -1;
		boolean result2 = false;
		try {
			result2 = this.databaseEngine.setBufferTourID("test", 12);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result2).isEqualTo(true);

		try {
			result = this.databaseEngine.getBufferTourID("test");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isNotEqualTo(-1);

		try {
			result2 = this.databaseEngine.deleteBufferBookingEntry("test");
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result2).isEqualTo(true);
	}
	@Test
	public void testSQL_13() throws Exception {
		//for testing this class
		boolean thrown = false;
		boolean result = false;
		try {
			result = this.databaseEngine.setBookingTourOfferingID("test4",1);
		} catch (Exception e) {
			thrown = true;
		}
		assertThat(!thrown);
		assertThat(result).isEqualTo(true);
	}
}
