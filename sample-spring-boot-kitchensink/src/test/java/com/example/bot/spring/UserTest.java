package com.example.bot.spring;


import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
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

@Slf4j
@RunWith(SpringRunner.class)
// @SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { UserTest.class,  SQLDatabaseEngine.class })
public class UserTest {
	@Autowired
	private SQLDatabaseEngine databaseEngine;

	private static final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
	private static final java.util.Date now = calendar.getTime();
	private static final java.sql.Timestamp time = new java.sql.Timestamp(now.getTime());
	private static final String test_id = "test_id";
	private static final String test_name = "test_name";
	private static final String test_phoneno = "00001111";
	private static final String test_age = "20";
	private static final int test_state = 1;
	private static boolean thrown = false;
	private static String query_result = null;
	private static boolean update_result = false;

	public UserTest() {
		try {
			databaseEngine.createUser(test_id, time, 0);
		} catch (Exception e) {
			log.info("Test User Exist!");
		}
	}

	@Before
	public void setUp() {
		thrown = false;
		update_result = false;
	}

	@After
	public void check() {
		assertFalse(thrown);
		log.info("No Exception");
		assertTrue(update_result);
		log.info("Update Succeed");
	}

	@Test
	public void testCreateUser() throws Exception {
		//for testing User class createUser function
		try {
			databaseEngine.deleteUser(test_id);
			update_result = databaseEngine.createUser(test_id, time, 0);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

	@Test
	public void testSetUserTime() throws Exception {
		//for testing User class setUserTime function
		try {
			update_result = databaseEngine.setUserTime(test_id, time);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

	@Test
	public void testSetUserState() throws Exception {
		//for testing User class setUserState function
		try {
			update_result = databaseEngine.setUserState(test_id, test_state);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}


	@Test
	public void testSetUserName() throws Exception {
		//for testing User class setUserName function
		try {
			update_result = databaseEngine.setUserName(test_id, test_name);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

	@Test
	public void testSetUserPhoneNum() throws Exception {
		//for testing User class setUserPhoneNum function
		try {
			update_result = databaseEngine.setUserPhoneNum(test_id, test_phoneno);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

	@Test
	public void testSetUSerAge() throws Exception {
		//for testing User class setUserAge function
		try {
			update_result = databaseEngine.setUserAge(test_id, test_age);
		} catch (Exception e) {
			log.info(e.toString());
			thrown = true;
		}
	}

}
