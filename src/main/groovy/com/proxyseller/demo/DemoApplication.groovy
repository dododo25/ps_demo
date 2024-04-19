package com.proxyseller.demo

import com.mongodb.client.MongoClients
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class DemoApplication {

	static void main(String[] args) {
		MongoOperations template = new MongoTemplate(
				new SimpleMongoClientDatabaseFactory(MongoClients.create(), "testDb"))

		template.getDb().drop()
		SpringApplication.run(DemoApplication, args)
	}
}
