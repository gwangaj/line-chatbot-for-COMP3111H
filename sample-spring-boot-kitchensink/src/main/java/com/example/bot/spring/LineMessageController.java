/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import java.io.File;
import java.io.IOException;

import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

//import javax.xml.ws.Action;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import com.linecorp.bot.model.profile.UserProfileResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.action.Action;

import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
/**
 * This class is line message controller which deals with user event
 * @author Group 16
 */

@Slf4j
@LineMessageHandler
public class LineMessageController {

	@Autowired
	private LineMessagingClient lineMessagingClient;
	/**
	 * Handles text message event
	 * @param event received text message event from user
	 * @throws Exception when handleTextContent fails
	 */
	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		log.info("This is your entry point:");
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		TextMessageContent message = event.getMessage();
		handleTextContent(event.getReplyToken(), event, message);
	}
	/**
	 * Handles sticker message event
	 * @param event received sticker message event from user
	 */
	@EventMapping
	public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
		handleSticker(event.getReplyToken(), event.getMessage());
	}
//	/**
//	 * Handle Location Event
//	 * @param event
//	 */
//	@EventMapping
//	public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
//		LocationMessageContent locationMessage = event.getMessage();
//		reply(event.getReplyToken(), new LocationMessage(locationMessage.getTitle(), locationMessage.getAddress(),
//				locationMessage.getLatitude(), locationMessage.getLongitude()));
//	}
//	/**
//	 * Handle Image Event
//	 * @param event
//	 */
//	@EventMapping
//	public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
//		final MessageContentResponse response;
//		String replyToken = event.getReplyToken();
//		String messageId = event.getMessage().getId();
//		try {
//			response = lineMessagingClient.getMessageContent(messageId).get();
//		} catch (InterruptedException | ExecutionException e) {
//			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
//			throw new RuntimeException(e);
//		}
//		DownloadedContent jpg = saveContent("jpg", response);
//		reply(((MessageEvent) event).getReplyToken(), new ImageMessage(jpg.getUri(), jpg.getUri()));
//
//	}
//	/**
//	 * Handle Audio Event
//	 * @param event
//	 */
//	@EventMapping
//	public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
//		final MessageContentResponse response;
//		String replyToken = event.getReplyToken();
//		String messageId = event.getMessage().getId();
//		try {
//			response = lineMessagingClient.getMessageContent(messageId).get();
//		} catch (InterruptedException | ExecutionException e) {
//			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
//			throw new RuntimeException(e);
//		}
//		DownloadedContent mp4 = saveContent("mp4", response);
//		reply(event.getReplyToken(), new AudioMessage(mp4.getUri(), 100));
//	}
//	/**
//	 * Handle Unfollow Event
//	 * @param event
//	 */
//	@EventMapping
//	public void handleUnfollowEvent(UnfollowEvent event) {
//		log.info("unfollowed this bot: {}", event);
//	}
//	/**
//	 * Handle Follow Event
//	 * @param event
//	 */
//	@EventMapping
//	public void handleFollowEvent(FollowEvent event) {
//		String replyToken = event.getReplyToken();
//		this.replyText(replyToken, "Got followed event");
//	}
//	/**
//	 * Handle Join Event
//	 * @param event
//	 */
//	@EventMapping
//	public void handleJoinEvent(JoinEvent event) {
//		String replyToken = event.getReplyToken();
//		this.replyText(replyToken, "Joined " + event.getSource());
//	}
	/**
	 * Handles postback event
	 * @param event received postback event from user
	 * @throws Exception when listTourForBooking fails
	 */
	@EventMapping
	public void handlePostbackEvent(PostbackEvent event)  throws Exception  {
		String replyToken = event.getReplyToken();
		String reply="";
		String text=event.getPostbackContent().getData();
        String userID = event.getSource().getUserId();
        User user = database.getUserInformation(userID);
		if (text.contains(Constant.TEXT_NEW_BOOKING)){
			database.setUserState(userID,Constant.BOOKING_TOUR_ID);
			listTourForBooking(replyToken, reply);
		}
		//this.replyText(replyToken, "Got postback " + event.getPostbackContent().getData());
	}
//	/**
//	 * Handle Beacon Event
//	 * @param event
//	 */
//	@EventMapping
//	public void handleBeaconEvent(BeaconEvent event) {
//		String replyToken = event.getReplyToken();
//		this.replyText(replyToken, "Got beacon message " + event.getBeacon().getHwid());
//	}
//	/**
//	 * Handle Other Event
//	 * @param event
//	 */
//	@EventMapping
//	public void handleOtherEvent(Event event) {
//		log.info("Received message(Ignored): {}", event);
//	}
	/**
	 * Replies a single piece of message
	 * @param replyToken line replyToken for replying
	 * @param message message to be replied
	 */
	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, Collections.singletonList(message));
	}
	/**
	 * Replies a list of messages
	 * @param replyToken line replyToken for replying
	 * @param messages message to be replied
	 */
	public void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
			log.info("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Replies text content to user
	 * @param replyToken line replyToken for replying
	 * @param message message to be replied
	 */
	public void replyText(@NonNull String replyToken, @NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		//if (message.length() > 1000) {
			//message = message.substring(0, 1000 - 2) + "..";
		//}
		this.reply(replyToken, new TextMessage(parse(message)));
	}
  	/**
	 * Replies sticker to user
	 * @param replyToken line replyToken for replying
	 * @param content sticker content
	 */
	private void handleSticker(String replyToken, StickerMessageContent content) {
		reply(replyToken, new StickerMessage(content.getPackageId(), content.getStickerId()));
	}
	/**
	 * Handles text content event from user
	 * @param replyToken line replyToken for replying
	 * @param event received event from user
	 * @param content text message content
	 * @throws Exception when state handler fails
	 */

	public void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws Exception {

        String text = content.getText();
        log.info("Got text message from {}: {}", replyToken, text);

        java.sql.Timestamp time = new java.sql.Timestamp(new java.util.Date().getTime());
        String userID = event.getSource().getUserId();
        User user = database.getUserInformation(userID);
        String reply = "";

        if(user.getUserID().equals("null")) {
        		reply += greeting();
        		reply += "!\n";
        		reply += Constant.GREETING_FIRST_USE;
        		database.createUser(userID,time,Constant.FAQ_NO_USER_INFORMATION);
        		user.setUser(userID,time,Constant.FAQ_NO_USER_INFORMATION);
        }

        int state = user.getState();
        java.sql.Timestamp last_time = user.getTime();
        long difference = (time.getTime()-last_time.getTime())/(60*1000);

        // welcome user back if the time gapping is larger than 10 minutes
        reply += welcomeBack(difference,user);

        // update last_time
        database.setUserTime(userID,time);

        switch(state) {
        case Constant.FAQ_NO_USER_INFORMATION:
        		FAQ_NO_USER_INFORMATION_handler(replyToken, text, userID, reply);
        		break;
        case Constant.FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION:
        		FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION_handler(replyToken, text, userID, reply);
        		break;
        case Constant.FAQ_AFTER_CONFIRMATION:
            FAQ_AFTER_CONFIRMATION_handler(replyToken, text, userID, reply);
        		break;
        case Constant.FILL_NAME:
        		FILL_NAME_handler(replyToken, text, userID, reply);
        		break;
        case Constant.FILL_PHONE_NUM:
        		FILL_PHONE_NUM_handler(replyToken, text, userID, reply);
        		break;
        case Constant.FILL_AGE:
        		FILL_AGE_handler(replyToken, text, userID, reply);
        		break;
        case Constant.BOOKING_TOUR_ID:
    			BOOKING_TOUR_ID_handler(replyToken, text, userID, reply);
    			break;
        case Constant.BOOKING_OFFERING_ID:
        		BOOKING_OFFERING_ID_handler(replyToken, text, userID, reply);
        		break;
        case Constant.BOOKING_ADULT:
    			BOOKING_ADULT_handler(replyToken, text, userID, reply);
    			break;
        case Constant.BOOKING_CHILDREN:
        		BOOKING_CHILDREN_handler(replyToken, text, userID, reply);
        		break;
        case Constant.BOOKING_TODDLER:
    			BOOKING_TODDLER_handler(replyToken, text, userID, reply);
    			break;
        case Constant.BOOKING_CONFIRMATION:
    			BOOKING_CONFIRMATION_handler(replyToken, text, userID, reply);
    			break;
        case Constant.BOOKING_PAYMENT:
    			BOOKING_PAYMENT_handler(replyToken, text, userID, reply);
    			break;
        case Constant.BOOKING_OR_REVIEW:
            BOOKING_OR_REVIEW_handler(replyToken, text, userID, reply);
       		break;
       	default:
       		break;
        }
	}
	
	
	/**
	 * Handles event when user asks faq without user information
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID id of the user
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */

	public void FAQ_NO_USER_INFORMATION_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		if(!text.toLowerCase().contains("book")) {
			faqsearch(replyToken, text, reply, userID);
		}
		else {
			database.setUserState(userID,Constant.FILL_NAME);
			reply += Constant.INSTRUCTION_FILL_INFORMATION;
			reply += Constant.INSTRUCTION_ENTER_NAME;
			log.info("Returns message {}: {}", replyToken, reply);
			this.replyText(replyToken,reply);
		}
	}
	
	
	/**
	 * Handles event when user asks faq with user information but without confirmation
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when listTourForBooking fails
	 */
	public void FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		if(!text.toLowerCase().contains("book")) {
			faqsearch(replyToken, text, reply, userID);
		}
		else {
			database.setUserState(userID,Constant.BOOKING_TOUR_ID);
			listTourForBooking(replyToken, reply);
		}
	}
	
	
	/**
	 * Handles event when user asks faq after confirmation
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when reply fails
	 */
	public void FAQ_AFTER_CONFIRMATION_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		if(!text.toLowerCase().contains("book")) {
			faqsearch(replyToken, text, reply, userID);
		}
		else{
		    database.setUserState(userID,Constant.BOOKING_OR_REVIEW);
		    ConfirmTemplate confirmTemplate = new ConfirmTemplate(
		    		Constant.QUESTION_REVIEW_OR_BOOKING,
		            new MessageAction("Review", "Review"),
		            new PostbackAction(Constant.TEXT_NEW_BOOKING, Constant.TEXT_NEW_BOOKING)
		    );
		    TemplateMessage whichBook = new TemplateMessage("Review Booking/New Booking", confirmTemplate);

		    log.info("Returns review/new button {}: {}", replyToken);
		    this.reply(replyToken,whichBook);

		 }
	}
	

	/**
	 * Handles event when user needs to fill in his/her name
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */

	public void FILL_NAME_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		database.setUserState(userID,Constant.FILL_PHONE_NUM);
		// store the name info
		database.setUserName(userID,text);
		reply += Constant.INSTRUCTION_ENTER_PHONE_NUM;
		log.info("Returns message {}: {}", replyToken, reply);
		this.replyText(replyToken,reply);

	}
	
	
	/**
	 * Handles event when user needs to fill in his/her phone number
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */
	public void FILL_PHONE_NUM_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		database.setUserState(userID,Constant.FILL_AGE);
		// store phone number
		database.setUserPhoneNum(userID,text);
		reply += Constant.INSTRUCTION_ENTER_AGE;
		log.info("Returns message {}: {}", replyToken, reply);
		this.replyText(replyToken,reply);
	}
	
	/**
	 * Handles event when user needs to fill in his/her age
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */
	public void FILL_AGE_handler(String replyToken, String text, String userID, String reply) throws Exception {
		text=text.replaceAll(" ","");
		if(isNumeric(text) && Integer.parseInt(text)>=0) {
    		database.setUserAge(userID,text);//extract number preferred here
    		database.setUserState(userID,Constant.BOOKING_TOUR_ID);
    		reply += Constant.CONFIRM_REGISTRATION;
			// use function here
			listTourForBooking(replyToken, reply);

	}
	else {
		reply += Constant.ERROR_REENTER_AGE;
		log.info("Returns instruction message {}: {}", replyToken, reply);
		this.replyText(replyToken,reply);
	}
	}
	
	/**
	 * Handles event when user needs to fill in tour ID in the booking stage 
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */
	public void BOOKING_TOUR_ID_handler(String replyToken, String text, String userID, String reply) throws Exception {
		text=text.replaceAll(" ","");
		String result="";
		if(!checkQuit(text,userID,reply,replyToken,Constant.DELETING_NOTHING)) {
			//here		
			List<Tour> listOfTours = database.getTours();
			List<Message> messages = new ArrayList<Message>();

			//end

			if(isNumeric(text) && database.tourFound(Integer.parseInt(text))) {
				String description = " ";
				String[] lines;
				for (Tour tour : listOfTours) {
					if(tour.getTourID()==Integer.parseInt(text)) {
						description = tour.getDescription();
						description=description.replace("* ", "\n");
						description=description.replace("*", "\n");
						lines=description.split("[\\r\\n]+");
						description=tour.getTourName();
						for (String line :lines) {
							description+="\n\u2606" +line;
						}

					}
				}
				
				

				messages.add(new TextMessage(parse(description)));
				
				List<TourOffering> listOfTourOfferings=new ArrayList<TourOffering>();
				try {
					listOfTourOfferings =database.displayTourOffering(Integer.parseInt(text));
					}
					catch(Exception e) {
						reply += Constant.ERROR_REENTER_TOUR_ID;
		    			log.info("Returns instruction message {}: {}", replyToken, reply);
		    			this.replyText(replyToken,reply);
					}
    			if(listOfTourOfferings.isEmpty()) {
    				reply += Constant.INFORMATION_NO_TOUR_OFFERING;
    				log.info("Returns instruction message {}: {}", replyToken, reply);
        			this.replyText(replyToken,reply);
    			}
    			else {
    			       //todo
    				
    				for (TourOffering tourOffering:listOfTourOfferings) {
						result += ("Tour Offering "+tourOffering.getOfferingID()+"\nData and time: "+ tourOffering.getDate()+"\nHotel: "+tourOffering.getHotel()+"\nMax people: "+tourOffering.getMaxCapacity()+
								"\nQuota Left: "+tourOffering.getQuota()+"\nFull price for adult: HKD"+tourOffering.getPrice()+"\nDuration: "+tourOffering.getDuration()+" Days\n\n");
					}

    				database.setUserState(userID,Constant.BOOKING_OFFERING_ID);
    				database.setBufferTourID(userID,Integer.parseInt(text));
    				reply += Constant.INFORMATION_TOUR_OFFERING;
    				reply += result;
    				reply += Constant.INSTRTUCTION_ENTER_TOUR_OFFERING_ID;
    				messages.add(new TextMessage(parse(reply)));
    				//prepare button for display
    				int count=0;
    				List<Action> listOfButton=new ArrayList<Action>();
    				for (TourOffering tourOffering:listOfTourOfferings) {
    					String tourofferingID=Integer.toString(tourOffering.getOfferingID());
    					MessageAction button=new MessageAction("Tour Offering "+tourofferingID, tourofferingID);
	    				listOfButton.add(button);
	    				count++;
	    				if (count%5==0) break;
    				}	
    				ButtonsTemplate buttonTemplate = new ButtonsTemplate(
	            			null,"Shortcut","You may press button instead.", listOfButton
		            		);
					messages.add(new TemplateMessage("button",buttonTemplate));
    				
        			log.info("Returns instruction message {}: {}", replyToken, reply);
        			this.reply(replyToken,messages);
    			}
			}
			else {
				reply += Constant.ERROR_REENTER_TOUR_ID;
    			log.info("Returns instruction message {}: {}", replyToken, reply);
    			this.replyText(replyToken,reply);
			}
		}
	}
	/**
	 * Handles event when user needs to fill in tour offering ID in the booking stage 
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */
	public void BOOKING_OFFERING_ID_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		text=text.replaceAll(" ","");
		if(!checkQuit(text,userID,reply,replyToken,Constant.DELETING_BOOKING_BUFFER)) {
			if(isNumeric(text) && database.tourOfferingFound(database.getBufferTourID(userID),Integer.parseInt(text))) {
				database.setUserState(userID,Constant.BOOKING_ADULT);
				database.deleteBufferBookingEntry(userID);
				database.setBookingTourOfferingID(userID,Integer.parseInt(text));
				reply += Constant.INSTRTUCTION_ENTER_ADULT_NUMBER;
				log.info("Returns instruction message {}: {}", replyToken, reply);
					this.replyText(replyToken,reply);
			}
			else {
				reply += Constant.ERROR_REENTER_TOUR_OFFERING_ID;
				log.info("Returns instruction message {}: {}", replyToken, reply);
				this.replyText(replyToken,reply);
			}
		}
	}
	/**
	 * Handles event when user needs to fill in the number of adults in the booking stage 
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */
	public void BOOKING_ADULT_handler(String replyToken, String text, String userID, String reply) throws Exception {
		if(!checkQuit(text,userID,reply,replyToken,Constant.DELETING_BOOKING_ENTRY)) {
			text=text.replaceAll(" ","");
			if(isNumeric(text) && Integer.parseInt(text)>=0) {
					database.setUserState(userID,Constant.BOOKING_CHILDREN);
					database.setBookingAdultNumber(userID,Integer.parseInt(text));
					reply += Constant.INSTRTUCTION_ENTER_CHILDREN_NUMBER;
					log.info("Returns instruction message {}: {}", replyToken, reply);
	    				this.replyText(replyToken,reply);
				}
				else {
					reply += Constant.ERROR_REENTER_ADULT_NUMBER;
    				log.info("Returns instruction message {}: {}", replyToken, reply);
    				this.replyText(replyToken,reply);
				}


		}
	}
	/**
	 * Handles event when user needs to fill in the number of children in the booking stage 
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */
	public void BOOKING_CHILDREN_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		if(!checkQuit(text,userID,reply,replyToken,Constant.DELETING_BOOKING_ENTRY)) {
			text=text.replaceAll(" ","");
			if(isNumeric(text) && Integer.parseInt(text)>=0) {
					database.setUserState(userID,Constant.BOOKING_TODDLER);
					database.setBookingChildrenNumber(userID,Integer.parseInt(text));
					reply += Constant.INSTRTUCTION_ENTER_TODDLER_NUMBER;
					log.info("Returns instruction message {}: {}", replyToken, reply);
						this.replyText(replyToken,reply);
				}
				else {
					reply += Constant.ERROR_REENTER_CHILDREN_NUMBER;
					log.info("Returns instruction message {}: {}", replyToken, reply);
					this.replyText(replyToken,reply);
				}


		}
	}

	/**
	 * Handles event when user needs to fill in the number of toddlers in the booking stage 
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when replyText fails
	 */
	public void BOOKING_TODDLER_handler(String replyToken, String text, String userID, String reply) throws Exception {
		if(!checkQuit(text,userID,reply,replyToken,Constant.DELETING_BOOKING_ENTRY)) {
			text=text.replaceAll(" ","");
			if(isNumeric(text) && Integer.parseInt(text)>=0) {
					database.setBookingToddlerNumber(userID,Integer.parseInt(text));

					int quota=database.checkQuota(userID);
					if(quota>=0) {
						database.setUserState(userID,Constant.BOOKING_CONFIRMATION);
						//reply += Constant.INSTRTUCTION_ENTER_SPECIAL_REQUEST;
			            ButtonsTemplate buttonTemplate = new ButtonsTemplate(
		            			null,
		            			"Special request",
		                    "Press \"No\" if you don't have any request.",
		                    Arrays.asList(new MessageAction("No", "No"))
			            		);
		           
		            TemplateMessage ButtonMessageBlock = new TemplateMessage("Sepcial requests?",buttonTemplate);
	
					log.info("Returns instruction message {}: {}", replyToken, reply);
					this.reply(replyToken,
		                    Arrays.asList(new TextMessage(Constant.INSTRTUCTION_ENTER_SPECIAL_REQUEST),ButtonMessageBlock));

					}
					else {
						quota=database.checkQuota(userID);
						database.setUserState(userID,Constant.BOOKING_ADULT);
						reply += Constant.QUOTA_FULL_1;
						reply += quota;
						reply += Constant.QUOTA_FULL_2;
						log.info("Returns instruction message {}: {}", replyToken, reply);
	    					this.replyText(replyToken,reply);
	    			}

				}
				else {
					reply += Constant.ERROR_REENTER_TODDLER_NUMBER;
    					log.info("Returns instruction message {}: {}", replyToken, reply);
    					this.replyText(replyToken,reply);
				}
			//end

		}
	}

	/**
	 * Handles event when user needs to confirm his/her booking in the booking stage 
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when reply fails
	 */
	public void BOOKING_CONFIRMATION_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		if(!checkQuit(text,userID,reply,replyToken,Constant.DELETING_BOOKING_ENTRY)) {

			database.setUserState(userID,Constant.BOOKING_PAYMENT);
			database.setBookingSpecialRequest(userID,text);
			
			reply += database.displaytBookingInformation(userID);

            ConfirmTemplate confirmTemplate = new ConfirmTemplate(
                    Constant.QUESTION_CONFIRM_OR_NOT,
                    new MessageAction("Yes", "Yes!"),
                    new MessageAction("No", "No!")
            );
            TemplateMessage confirmMessageBlock = new TemplateMessage("Confirm booking?", confirmTemplate);

			log.info("Returns instruction message {}: {}", replyToken, reply);

            this.reply(replyToken,
                    Arrays.asList(new TextMessage(parse(reply)),confirmMessageBlock));
		}
	}
	/**
	 * Handles event when user needs to pay for his/her booking in the booking stage 
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when reply fails
	 */
	public void BOOKING_PAYMENT_handler(String replyToken, String text, String userID, String reply) throws Exception {

			if(text.toLowerCase().contains("yes")||text.toLowerCase().contains("confirm")) {
				database.setUserState(userID,Constant.FAQ_AFTER_CONFIRMATION);
				database.setBookingConfirmation(userID);
				reply += Constant.INSTRUCTION_PAYMENT;
				log.info("Returns instruction message {}: {}", replyToken, reply);
	    		List<Message> msgToReply=new ArrayList<Message>();
	    		msgToReply.add(new StickerMessage("1",Constant.STICKER_ID_CONFIRMBOOK));
	    		msgToReply.add(new TextMessage(parse(reply)));
	    		this.reply(replyToken,msgToReply);
			}
			else {
				checkQuit("Q",userID,reply,replyToken,Constant.DELETING_BOOKING_ENTRY);
			}

	}

	/**
	 * Handles event when user needs to choose whether to add a new booking or review his/her old bookings
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param userID userID
	 * @param reply message to be replied
	 * @throws Exception when reply fails
	 */
	public void BOOKING_OR_REVIEW_handler(String replyToken, String text, String userID, String reply)
			throws Exception {
		if (text.toLowerCase().contains("review")) {
			database.setUserState(userID, Constant.FAQ_AFTER_CONFIRMATION);
			String review = database.reviewBookingInformation(userID);
			List<Message> messages = splitMessages(review, "\n\n\n\n");
			log.info("split");
			log.info("Returns message {}: {}", replyToken, reply);
			this.reply(replyToken, messages);
		} else {
			database.setUserState(userID, Constant.BOOKING_TOUR_ID);
			listTourForBooking(replyToken, reply);

		}
	}
	/**
	 * Welcomes user back when time gapping is largr than a specific period
	 * @param difference time difference 
	 * @param user	line user
	 * @return welcome back message
	 */
	public String welcomeBack(long difference, User user){
		String result = "";
		if(difference > Constant.TIME_GAPPING){
			result += greeting();
			if(!user.getUserName().equals("null")) {
				result += ", ";
				result += user.getUserName();
			}
			result += Constant.WELCOME_BACK;
		}
		return result;
	}
	/**
	 * Greets user based on the current time
	 * @return the greeting message
	 */
	public String greeting() {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);

		if((hour+8)%24 < 12) {
        		return Constant.MORNING;
        }
        else if((hour+8)%24 >= 18) {
        		return Constant.EVENING;
        }
        else {
        		return Constant.AFTERNOON;
        }
	}
	/**
	 * Checks whether user presses 'Q' to cancel booking
	 * @param text user input message
	 * @param userID userID
	 * @param reply reply content
	 * @param replyToken line replyToken for replying
	 * @param choice delete choice
	 * @return the result is true if user presses 'Q', otherwise the result is false
	 * @throws Exception when replyText fails
	 */
	public boolean checkQuit(String text, String userID, String reply, String replyToken, int choice) throws Exception{
		if (text.equals("Q")){
			String result = database.reviewBookingInformation(userID);
			if(result.equals("null")) {
				database.setUserState(userID, Constant.FAQ_NO_CONFIRMATION_WITH_USER_INFORMATION);
			}
			else {
				database.setUserState(userID, Constant.FAQ_AFTER_CONFIRMATION);
			}
			if(choice == Constant.DELETING_BOOKING_ENTRY) {
				database.deleteBookingEntry(userID);
			}
			else if(choice == Constant.DELETING_BOOKING_BUFFER) {
				database.deleteBufferBookingEntry(userID);
			}
			reply += Constant.CANCEL;
			log.info("Returns message {}: {}", replyToken, reply);
			this.replyText(replyToken,reply);
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Checks whether an input string is numeric
	 * @param str a stirng
	 * @return the result is true of the input string can be converted to a numeric number, otherwise returns false
	 */
	public static boolean isNumeric(String str)
		 {
		   try
		   {
		     int d = Integer.parseInt(str);
		   }
		   catch(NumberFormatException nfe)
		   {
		     return false;
		   }
		   return true;
		 }

	/**
	 * Splits a long message into list of messages based on the type of splitter
	 * @param longstring a string to be splitted
	 * @param splitter a splitter string
	 * @return message to be replied
	 */

	private List<Message> splitMessages(String longstring,String splitter){
		if(longstring!=null) {

			List<Message> messages = new ArrayList<Message>();
			String [] shortStrings = longstring.split(splitter);
			int numPerGroup = (shortStrings.length/4)+1; //split into 4 groups
			String groupString = "";
			for(int i = 0; i<shortStrings.length;i++ ) {
				groupString += shortStrings[i];
				groupString += "\n\n";
				if((i+1)%numPerGroup==0) {
					Message message = new TextMessage(parse(groupString));
					messages.add(message);
					groupString = "";//clear the groupString
				}else if(i+1 ==shortStrings.length) { //dealing with boundary case, e.g 27/5=5 5+1=6, the last one does not give 0
					Message message = new TextMessage(parse(groupString));
					messages.add(message);
					groupString = "";//clear the groupString
				}
			}
			return messages;
		}
		else {
			return null;
		}

	}
	/**
	 * Lists tours for user to make booking
	 * @param replyToken line replyToken for replying
	 * @param reply message to be replied
	 * @throws Exception when reply fails
	 */
	public void listTourForBooking(String replyToken, String reply) throws Exception {
		List<Message> msgToReply=new ArrayList<Message>();
		// if (reply!=null && !reply.replaceAll(" ", "").isEmpty()) msgToReply.add(new TextMessage(reply));
		TextMessage heading = new TextMessage(parse(reply+Constant.INSTRUCTION_BOOKING));
		msgToReply.add(heading);
		
		List<Tour> listOfTours=new ArrayList<Tour>();
		
		try {
		listOfTours =database.getTours();
		}
		catch(Exception e) {
			log.info(e.toString());
			msgToReply.add(new TextMessage("No Tours Avaliable"));
			log.info(String.valueOf(msgToReply.size()));
			this.reply(replyToken,msgToReply);
			return;
		}
		
		List<CarouselColumn> carousel=new ArrayList<CarouselColumn>();
		int count=0;
		for (Tour tour:listOfTours) {
			String imagePath=tour.getImagePath();
			if (imagePath==null || imagePath.replaceAll(" ", "").isEmpty()) imagePath="404.png";
			
				log.info("6666666666666666666");
				String imagePrefix=Constant.IMGPRFIX;
				String imageUrl = createUri(imagePath);
				log.info("6666666666666666666");
				log.info(imageUrl);
				log.info("6666666666666666666");
				log.info(tour.getTourName());


			String trancatedDescription=tour.getDescription();
			//if (trancatedDescription.length()>60) trancatedDescription=tour.getShortDescription();
			if (trancatedDescription.length()>60) trancatedDescription=trancatedDescription.substring(0, 60-2)+"..";
			log.info(imageUrl);
			log.info(tour.getTourName());
			log.info(trancatedDescription);
			log.info(String.valueOf(tour.getTourID()));
			CarouselColumn item=new CarouselColumn(imageUrl, tour.getTourName(), trancatedDescription, Arrays.asList(
              new MessageAction("Book",Integer.toString(tour.getTourID()))
              ));
			carousel.add(item);
			count++;
			//every 4 items one carousel 
			if (count%4==0 || count==listOfTours.size()) {
				CarouselTemplate carouselTemplate = new CarouselTemplate(carousel);
				TemplateMessage templateMessage = new TemplateMessage("Carousel of List", carouselTemplate);
				msgToReply.add(templateMessage);
				log.info(carousel.size()+"");
				carousel=new ArrayList<CarouselColumn>();
			}

		}
		log.info(msgToReply.size()+"");
		log.info("Listed tours for booking{}", replyToken);
		this.reply(replyToken,msgToReply);
	}
	
	/**
	 * Parses description based on '*'
	 * 
	 * @param description a string to be parsed
	 * @return a string after parsing
	 */
	public String parse(String description) {
		String parseDescription = "";
		for (int i = 0; i < description.length(); i++) {
			if (description.charAt(i) == '*') {
				parseDescription += "\n*";
			} else {
				parseDescription += description.charAt(i);
			}
		}
		return parseDescription;
	}

	/**
	 * Faq Search
	 * @param replyToken line replyToken for replying
	 * @param text user input message
	 * @param reply message to be replied
	 * @param userID userID
	 * @throws Exception when replyText fails
	 */
	public void faqsearch(String replyToken, String text, String reply, String userID) throws Exception {
		try {
		String answer = faqDatabase.search(text, userID);
		reply += answer;
		String imageURL=faqDatabase.replyImage(answer);
		if (imageURL!=null) {
			log.info("\n\n\n\n\n\n\n");
			imageURL=createUri(imageURL);
			log.info("imageURL {}", imageURL);
			log.info("\n\n\n\n\n\n\n");
			this.reply(replyToken, Arrays.asList(new TextMessage(parse(reply)),new ImageMessage(imageURL, imageURL)));
			log.info("Replied image message {}: {}", replyToken, reply);

		}
		else {
			log.info("Returns answer message {}: {}", replyToken, reply);
			this.replyText(replyToken,reply);
		}
		}catch(Exception e) {
			reply += Constant.FAQ_NOT_FOUND;
			//unanswered question, add to unknown question database
			database.addToUnknownDatatabse(text);
			log.info("Returns message {}: {}", replyToken, reply);
			this.replyText(replyToken,reply);

		}
	}
	/**
	 * Create uri based on path
	 * @param path path of the uri
	 */
	static String createUri(String path) {
		return "https://comp3111h-line-chatbot.herokuapp.com/" + Constant.IMGPRFIX + path;
//		return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUriString();
	}
	
	/**
	 * system
	 * @param args arguments
	 */
	private void system(String... args) {
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		try {
			Process start = processBuilder.start();
			int i = start.waitFor();
			log.info("result: {} =>  {}", Arrays.toString(args), i);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {
			log.info("Interrupted", e);
			Thread.currentThread().interrupt();
		}
	}
//	/**
//	 * Save Content
//	 * @param ext
//	 * @param responseBody
//	 * @return tempFile
//	 */
//	private static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
//		log.info("Got content-type: {}", responseBody);
//
//		DownloadedContent tempFile = createTempFile(ext);
//		try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
//			ByteStreams.copy(responseBody.getStream(), outputStream);
//			log.info("Saved {}: {}", ext, tempFile);
//			return tempFile;
//		} catch (IOException e) {
//			throw new UncheckedIOException(e);
//		}
//	}
//	/**
//	 * Create Temp File
//	 * @param ext
//	 */
//	private static DownloadedContent createTempFile(String ext) {
//		String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString() + '.' + ext;
//		Path tempFile = KitchenSinkApplication.downloadedContentDir.resolve(fileName);
//		tempFile.toFile().deleteOnExit();
//		return new DownloadedContent(tempFile, createUri("/downloaded/" + tempFile.getFileName()));
//	}



	/**
	 * Constructor for line message controller
	 */
	public LineMessageController() {
		database = new SQLDatabaseEngine();
		faqDatabase = new FaqDatabase();
		itscLOGIN = System.getenv("ITSC_LOGIN");
	}

	private SQLDatabaseEngine database;
	private FaqDatabase faqDatabase;
	private String itscLOGIN;
	
//	/**
//	 * Constructor. 
//	 * The annontation @Value is from the package lombok.Value
//	 * Basically what it does is to generate constructor and getter for the class below
//	 * See https://projectlombok.org/features/Value
//	 */
//
//	//The annontation @Value is from the package lombok.Value
//	//Basically what it does is to generate constructor and getter for the class below
//	//See https://projectlombok.org/features/Value
//	@Value
//	public static class DownloadedContent {
//		Path path;
//		String uri;
//	}
	/**
	 * This class is a container for profile getter
	 * @see LineMessageController
	 * @author Group 16
	 */

	//an inner class that gets the user profile and status message
	class ProfileGetter implements BiConsumer<UserProfileResponse, Throwable> {
		private LineMessageController ksc;
		private String replyToken;
		
		/**
		 * Constructor for ProfileGetter class
		 * @param ksc linemessageController
		 * @param replyToken line replyToken for replying
		 */
		public ProfileGetter(LineMessageController ksc, String replyToken) {
			this.ksc = ksc;
			this.replyToken = replyToken;
		}
		@Override
		
		/**
		 * Accept
		 * @param profile an object of UserProfileResponse type
		 * @param throwable an object of Throwable type
		 */
    	public void accept(UserProfileResponse profile, Throwable throwable) {
    		if (throwable != null) {
            	ksc.replyText(replyToken, throwable.getMessage());
            	return;
        	}
        	ksc.reply(
                	replyToken,
                	Arrays.asList(new TextMessage(
                		"Display name: " + profile.getDisplayName()),
                              	new TextMessage("Status message: "
                            		  + profile.getStatusMessage()))
        	);
    	}
    }



}