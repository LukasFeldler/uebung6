package at.jku;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Application {

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public JavaDelegate helloMQTT(){
    return new JavaDelegate() {
      @Override
      public void execute(DelegateExecution delegateExecution) throws Exception {
        final Object rfidTag = delegateExecution.getVariable("rfidTag");


        final Object stromverbrauch = delegateExecution.getVariable("stromverbrauch");


        String message = String.valueOf(rfidTag) + " " + String.valueOf(stromverbrauch);
        System.out.println(message);

        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost",1883);
        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();
        connection.publish("Stampfmaschine/Stromverbrauch",message.getBytes(), QoS.AT_LEAST_ONCE,false);


      }
    };
  }

}