package com.learning;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateUserService {
    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:users_database.db";
        this.connection = DriverManager.getConnection(url);

        // create user table
        this.connection.createStatement().execute("create table tb_user(" +
                "uuid_user varchar(200) primary key," +
                "eml_user varchar(200) null" +
                ")");
    }


    public static void main(String[] args) throws SQLException {

        var createNewUser = new CreateUserService();

        String groupId = CreateUserService.class.getName();
        String topicName = "ECOMMERCE_NEW_ORDER";

        try(var service = new KafkaService<Order>(
                  groupId
                , topicName
                , createNewUser::parse
                , Order.class.getName()
                , GsonDeserializer.class.getName())) {

            service.run();
        }

    }

    private void parse(ConsumerRecord<String, Order> rec) throws SQLException {
        System.out.println("---------- NEW ORDER HAS BEEN ARRIVED ----------");
        System.out.println("Topic: " + rec.topic());
        System.out.println("Key: " + rec.key());
        System.out.println("Value: " + rec.value());
        
        System.out.println("Checking for new user...");
        Order order = rec.value();

        if (isNewUser(order.getEmail())){
            insertNewUser(order.getEmail());
        }
    }

    private void insertNewUser(String email) throws SQLException {
        var insert = connection.prepareStatement("insert into tb_user values(?, ?)");
        insert.setString(1, "uuid_user");
        insert.setString(2, email);
        insert.execute();
        System.out.println("User has been added: " + email);
    }

    private boolean isNewUser(String orderEmail) throws SQLException {
        var select = this.connection.prepareStatement("select * from tb_users where eml_user = ? limit 1");
        select.setString(1, orderEmail);
        var emailExists = select.executeQuery();

        return !emailExists.next();
    }



}
