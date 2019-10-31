package g11tpe.server;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("g11tpe Server Starting ...");
        HazelcastInstance h = Hazelcast.newHazelcastInstance();

        Map<Long, String> map = h.getMap("data");
        IdGenerator idGenerator = h.getIdGenerator("newid");
        for (int i = 0; i < 10; i++) {
            map.put(Integer.toUnsignedLong(i), Integer.toString(i));
        }
    }
}
