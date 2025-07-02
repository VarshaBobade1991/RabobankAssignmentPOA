package nl.rabobank.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMongoRepositories(basePackages = "nl.rabobank.repository")
@EnableConfigurationProperties(MongoProperties.class)
@RequiredArgsConstructor
public class MongoConfiguration extends AbstractMongoClientConfiguration
{
    @Autowired
    private MongoProperties mongoProperties;

    @Override
    protected String getDatabaseName()
    {
        return mongoProperties.getMongoClientDatabase();
    }

    @Override
    @Bean(destroyMethod = "close")
    public MongoClient mongoClient()
    {
        String uri = mongoProperties.determineUri();
        return MongoClients.create(uri);
    }
}
