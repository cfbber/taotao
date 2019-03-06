import org.junit.Test;

public class TestMQTopic {
    @Test
    public void send() throws Exception {

/*        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.72.131:61616");
        final Connection connection = factory.createConnection();
        connection.start();
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Topic test_topic = session.createTopic("test_topic");


        final MessageProducer producer = session.createProducer(test_topic);
        final TextMessage hello_world = session.createTextMessage("hello world");
        producer.send(hello_world);
        connection.close();*/
    }

    @Test
    public void receiver() throws Exception{
    /*    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.72.131:61616");
        final Connection connection = factory.createConnection();
        connection.start();
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Topic topic = session.createTopic("test_topic");

        final MessageConsumer consumer = session.createConsumer(topic);
        final Message message = consumer.receive();
        System.out.println(message);*/
    }
}
