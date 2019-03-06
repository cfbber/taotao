import org.junit.Test;

public class TestMqQueue {

    @Test
    public void send() throws Exception {

      /*  ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.72.131:61616");
        final Connection connection = factory.createConnection();
        connection.start();
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Queue queue = session.createQueue("queue-test");
        final MessageProducer producer = session.createProducer(queue);
        final TextMessage hello_world = session.createTextMessage("hello world");
        producer.send(hello_world);
        connection.close();*/
    }

    @Test
    public void receiver() throws Exception{
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.72.131:61616");
//        final Connection connection = factory.createConnection();
//        connection.start();
//        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        final Queue queue = session.createQueue("queue-test");
//
//        final MessageConsumer consumer = session.createConsumer(queue);
//        final Message message = consumer.receive();
//        System.out.println(message);
    }

}
