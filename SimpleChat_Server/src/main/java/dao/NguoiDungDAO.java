package dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;

import entities.NguoiDung;

public class NguoiDungDAO {
	
	private static NguoiDungDAO instance;
	private DynamoDBMapper mapper;
	
	private NguoiDungDAO(DynamoDBMapper mapper) {
		this.mapper = mapper;
	}
	
	public static NguoiDungDAO getInstance(DynamoDBMapper mapper) {
		if(instance == null)
			instance = new NguoiDungDAO(mapper);
		return instance;
	}
	
	public void addNguoiDung(NguoiDung nguoiDung) {
		mapper.save(nguoiDung);
	}
	
	private NguoiDung getNguoiDung(String email) {
		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
		values.put(":e", new AttributeValue().withS(email));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
			.withFilterExpression("email = :e")
			.withExpressionAttributeValues(values);
		List<NguoiDung> items = mapper.scan(NguoiDung.class, expression);
		if(items.isEmpty())
			return null;
		return items.get(0);
	}
	
	public boolean updateFriends(NguoiDung nguoiDung) {
		NguoiDung temp = getNguoiDung(nguoiDung.getEmail());
		System.out.println(temp);
		if(temp == null)
			return false;
		DynamoDBSaveExpression expression = new DynamoDBSaveExpression();
		Map<String, ExpectedAttributeValue> expectedAttributes = new HashMap<>();
		expectedAttributes.put("id", new ExpectedAttributeValue(new AttributeValue().withS(temp.getId())));
		expression.setExpected(expectedAttributes);
		temp.setFriends(nguoiDung.getFriends());
		mapper.save(temp, expression);
		return true;
	}
	
	public String isValidLogin(String email, String password) {
		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
		values.put(":e", new AttributeValue().withS(email));
		values.put(":p", new AttributeValue().withS(password));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
			.withFilterExpression("email = :e and password = :p")
			.withExpressionAttributeValues(values);
		List<NguoiDung> items = mapper.scan(NguoiDung.class, expression);
		if(items.isEmpty())
			return "Tài khoản không tồn tại";
		return items.get(0).getStatus().equalsIgnoreCase("locked") ? "Tài khoản bị khóa" : "true";
	}
	
	public List<String> getFriends(String email, String password){
		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
		values.put(":e", new AttributeValue().withS(email));
		values.put(":p", new AttributeValue().withS(password));
		DynamoDBScanExpression expression = new DynamoDBScanExpression()
			.withFilterExpression("email = :e and password = :p")
			.withExpressionAttributeValues(values);
		List<NguoiDung> items = mapper.scan(NguoiDung.class, expression);
		if(items.isEmpty())
			return null;
		List<String> friends = items.get(0).getFriends();
		if(friends == null)
			return null;
		return friends;
	}
	
	
	
	
}
