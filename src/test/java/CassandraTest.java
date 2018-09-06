import com.alibaba.fastjson.JSONObject;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.wayz.ai.CassandraUtil.Cassandra;
import com.wayz.ai.CassandraUtil.CassandraConfig;
import com.wayz.ai.CassandraUtil.CassandraSessionPool;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CassandraTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraTest.class);
    @Test
    public void testConnection() {
        try {
            CassandraConfig config = new CassandraConfig("192.168.99.100", 9042);
            CassandraSessionPool.initSessionPool(config);
            Cassandra cassandra = new Cassandra();
            Statement statement = new SimpleStatement("select * from test.users;");
            List<JSONObject> list = cassandra.query(statement);
            for (JSONObject jsonObject : list) {
                System.out.println("==================" + jsonObject.toJSONString() + "=================");
            }
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            CassandraSessionPool.getInstance().close();

        }
    }
}
