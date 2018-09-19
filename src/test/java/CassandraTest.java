import com.alibaba.fastjson.JSONObject;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.wayz.ai.CassandraUtil.Cassandra;
import com.wayz.ai.CassandraUtil.CassandraSessionPool;
import com.wayz.ai.MainApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= MainApp.class)
public class CassandraTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraTest.class);

    private static Cassandra cassandra;

    @Test
    public void testConnection() {
        try {
            if (cassandra == null)
                cassandra = new Cassandra();
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
