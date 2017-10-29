package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		String result = null;
		int result2=0;
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT answer, hit FROM line_faq WHERE ? like concat('%',question,'%')");
			stmt.setString(1, text);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result=rs.getString(1);
				int hit =rs.getInt(2);
				hit=hit+1;
				PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_faq SET hit = ? WHERE answer = ?;");
				
				stmt2.setInt(1, hit);
				stmt2.setString(2, result);
				result2=stmt2.executeUpdate();
				stmt2.close();
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result == null)
			result ="null";
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
	
	User getUserInformation(String id) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		User result=new User();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_user WHERE id = ?;");
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
					result.setID(rs.getString(1));
					result.setName(rs.getString(2));
					result.setPhoneNumber(rs.getString(3));
					result.setAge(rs.getString(4));
					result.setState(rs.getInt(5));
					result.setTime(rs.getTimestamp(6));
					// train offering
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (1==1)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserTime(String id, java.sql.Timestamp time) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET last_login = ? WHERE id = ?;");
			stmt2.setTimestamp(1, time);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserState(String id, int FAQ1) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET state = ? WHERE id = ?;");
			stmt2.setInt(1, FAQ1);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserName(String id, String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET name = ? WHERE id = ?;");
			stmt2.setString(1, text);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserPhoneNum(String id, String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET phone_num = ? WHERE id = ?;");
			stmt2.setString(1, text);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setUserAge(String id, String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_user SET age = ? WHERE id = ?;");
			stmt2.setString(1, text);
			stmt2.setString(2, id);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean createUser(String id, java.sql.Timestamp time, int state) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO line_user (id, state, last_login) VALUES (?, ?, ?);");
			stmt2.setString(1, id);
			stmt2.setInt(2, state);
			stmt2.setTimestamp(3, time);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean addToUnknownDatatabse(String text) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO line_unknownquestion (question) VALUES (?);");
			stmt2.setString(1, text);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	String getTourNames() throws Exception {
		//Write your code here
		Connection connection = getConnection();
		String result="";
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_tour;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result += (rs.getString(1)+" "+rs.getString(2)+"\n"+rs.getString(3)+"\n\n");
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result == "")
			result ="null";
		if (result != "")
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean tourFound(int tourID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		boolean result=false;
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_tour WHERE id = ?;");
			stmt.setInt(1, tourID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result=true;
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (1==1)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	String displayTourOffering(int tourID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		String result="";
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT id,offer_date,hotel,capacity_max FROM line_touroffering WHERE tour_id = ? AND state < 2 AND id IN "
					+ "(SELECT id FROM line_touroffering WHERE tour_id = ? EXCEPT "
					+ "SELECT line_touroffering.id FROM line_touroffering, line_booking "
					+ "WHERE line_touroffering.tour_id = ? AND line_touroffering.id=line_booking.\"tourOffering_id\" "
					+ "AND line_booking.state=2 "
					+ "GROUP BY line_touroffering.id "
					+ "HAVING COUNT(*) >= line_touroffering.capacity_max);");
			stmt.setInt(1, tourID);
			stmt.setInt(2, tourID);
			stmt.setInt(3, tourID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result += (rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" max people: "+rs.getInt(4)+"\n\n");
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result == "")
			result ="null";
		if (result != "")
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean setBufferTourID(String userID, int tourID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO line_userchoose (user_id, tour_id) VALUES (?, ?);");
			stmt2.setString(1, userID);
			stmt2.setInt(2, tourID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean deleteBufferBookingEntry(String userID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM line_userchoose WHERE user_id = ?;");
			stmt2.setString(1, userID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean deleteBookingEntry(String userID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM line_booking WHERE user_id = ? AND state = 0;");
			stmt2.setString(1, userID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	int getBufferTourID(String userID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=-1;
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM line_userchoose WHERE user_id = ?;");
			stmt.setString(1, userID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
					result=rs.getInt(2);
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		
		if (1==1)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean tourOfferingFound(int tourID,int tourOfferingID)throws Exception {
		//Write your code here
		Connection connection = getConnection();
		boolean result=false;
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT id,offer_date,hotel,capacity_max FROM line_touroffering WHERE tour_id = ? AND state < 2 AND id = ? AND id IN "
					+ "(SELECT id FROM line_touroffering WHERE tour_id = ? EXCEPT "
					+ "SELECT line_touroffering.id FROM line_touroffering, line_booking "
					+ "WHERE line_touroffering.tour_id = ? AND line_touroffering.id=line_booking.\"tourOffering_id\" "
					+ "AND line_booking.state=2 "
					+ "GROUP BY line_touroffering.id "
					+ "HAVING COUNT(*) >= line_touroffering.capacity_max);");
			stmt.setInt(1, tourID);
			stmt.setInt(2, tourOfferingID);
			stmt.setInt(3, tourID);
			stmt.setInt(4, tourID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result=true;
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (1==1)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean setBookingTourOfferingID(String userID, int tourOfferingID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO line_booking (user_id, \"tourOffering_id\") VALUES (?, ?);");
			stmt2.setString(1, userID);
			stmt2.setInt(2, tourOfferingID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setBookingAdultNumber(String userID,int number) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_booking SET adult_num = ? WHERE user_id = ? AND state = 0;");
			stmt2.setInt(1, number);
			stmt2.setString(2, userID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setBookingChildrenNumber(String userID,int number) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_booking SET child_num = ? WHERE user_id = ? AND state = 0;");
			stmt2.setInt(1, number);
			stmt2.setString(2, userID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setBookingToddlerNumber(String userID,int number) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_booking SET toddler_num = ? WHERE user_id = ? AND state = 0;");
			stmt2.setInt(1, number);
			stmt2.setString(2, userID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	boolean setBookingSpecialRequest(String userID,String request) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_booking SET special_request = ? WHERE user_id = ? AND state = 0;");
			stmt2.setString(1, request);
			stmt2.setString(2, userID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
	String displaytBookingInformation(String userID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		String result="";
		int result2=0;
		String special="";
		int adult=0;
		int child=0;
		int toddler=0;
		double fee=0;
		double price=0;
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT line_booking.adult_num, line_booking.child_num, line_booking.toddler_num, line_booking.price, line_booking.special_request "//5
					+ "line_touroffering.offer_date, line_touroffering.hotel, line_touroffering.capacity_max, line_touroffering.guide_name, line_touroffering.guide_line "//5
					+ "line_tour.name, line_tour.decription, line_tour.duration "//3
					+ "FROM line_booking, line_touroffering, line_tour "
					+ "WHERE line_booking.user_id = ? AND line_booking.state = 0 "
					+ "AND line_booking.\"tourOffering_id\"=line_touroffering.id "
					+ "AND line_touroffering.tour_id=line_tour.id;");
			stmt.setString(1, userID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				adult=rs.getInt(1);
				child=rs.getInt(2);
				toddler=rs.getInt(3);
				price=rs.getDouble(4);
				special=rs.getString(5);
				result=("Tour name: "+rs.getString(11)+"\n\nDescription: "+rs.getString(12)+"\n\nDuration: "+rs.getInt(13)+"\n\nOffer date: "
				+rs.getTimestamp(6)+"\n\nHotel: "+rs.getString(7)+"\n\nMax people: "+rs.getInt(8)+"\n\nGuide name: "+rs.getString(9)
				+"\n\nGuide line account: "+rs.getString(10)+"\n\nAdult: "+adult+"\n\nChild: "+child+"\n\nToddler: "+toddler
				+"\n\nSpecial request: "+special);
				fee = price*adult + price*0.8*child;
				result+=("\n\nTotal fee: "+fee);
			}
			rs.close();
			stmt.close();
			PreparedStatement stmt2 = connection.prepareStatement(
					"UPDATE line_booking SET tour_fee = ?, paid_fee = ? WHERE user_id = ? AND state = 0;");
			stmt2.setDouble(1, fee);
			stmt2.setDouble(2, 0.0);
			stmt2.setString(3, userID);
			result2=stmt2.executeUpdate();
			stmt2.close();
			
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result == "")
			result ="null";
		if (result != "")
			return result;
		throw new Exception("NOT FOUND");
	}
	
	boolean setBookingConfirmation(String userID) throws Exception {
		//Write your code here
		Connection connection = getConnection();
		int result=0;
		try {
			PreparedStatement stmt2 = connection.prepareStatement("UPDATE line_booking SET state = 1 WHERE user_id = ? AND state = 0;");
			stmt2.setString(1, userID);
			result=stmt2.executeUpdate();
			stmt2.close();
			connection.close();
		} catch (Exception e) {
			log.info(e.toString());
		} finally {
			
		}
		if (result!=0)
			return true;
		if (result==0)
			return false;
		throw new Exception("NOT FOUND");
	}
	
}