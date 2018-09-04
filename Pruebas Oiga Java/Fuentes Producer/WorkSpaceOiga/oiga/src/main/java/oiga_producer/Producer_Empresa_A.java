package oiga_producer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
 
public class Producer_Empresa_A {
	
    public static void main(String[] args) throws URISyntaxException, Exception {

        BrokerService broker = BrokerFactory.createBroker(new URI("broker:(tcp://localhost:61616)"));
        broker.start();
        Connection connection = null;
        try {
            // Producer
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("xQueue");
 
            MessageProducer producer = session.createProducer(queue);
            for (int i = 1; i < 100000; i++) {
                String payload = "ID:" + i + " Timestamp: " + new Date();
                Message msg = session.createTextMessage(payload);
                //System.out.println("Enviado ID:" + i + " Timestamp: " + new Date());
               // System.out.println("Envio Mensaje de '" + payload + "'");
                producer.send(msg);
                
                if(i % 5 == 0) {
                	TimeUnit.SECONDS.sleep(60);            	
                } 
            }
            session.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
            broker.stop();
        }
    }
}

