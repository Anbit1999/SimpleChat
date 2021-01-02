package dao;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class MyDatabase {

	private static MyDatabase instance;
	private DynamoDBMapper mapper;
	
	private MyDatabase(String accessKey, String secretKey) {
		EndpointConfiguration configuration = new AwsClientBuilder
		.EndpointConfiguration("dynamodb.ap-southeast-1.amazonaws.com", "ap-southeast-1");
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(configuration)
				.withCredentials(provider)
				.build();
		mapper = new DynamoDBMapper(dynamoDB);
	}
	
	public static synchronized MyDatabase getInstance(String accessKey, String secretKey) {
		if(instance == null)
			instance = new MyDatabase(accessKey, secretKey);
		return instance;
	}
	
	public DynamoDBMapper getMapper() {
		return mapper;
	}
	
}
