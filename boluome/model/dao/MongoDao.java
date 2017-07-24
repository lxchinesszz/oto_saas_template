package phoenix.jhbank.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @Package: elephant.zybank.model.dao
 * @Description:
 * @author: liuxin
 * @date: 17/6/13 上午11:53
 */
@Component
public class MongoDao {
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 根据订单号查询
     *
     * @param orderId
     * @param t
     * @param <T>
     * @return
     */
    public <T> T findOneById(String orderId, Class<T> t) {
        Assert.notNull(orderId, "订单号不能为空");
        Query query = Query.query(Criteria.where("id").is(orderId));
        return mongoTemplate.findOne(query, t);
    }

    /**
     * @param orderId
     * @param t         返回参数
     * @param orderType
     * @param <T>
     * @return
     */
    public <T> T findOneById(String orderId, Class<T> t, String orderType) {
        Assert.notNull(orderId, "订单号不能为空");
        Assert.notNull(orderType, "订单类型不能为空");
        Query query = Query.query(Criteria.where("id").is(orderId));
        String collectionName = "order_" + orderType;
        return mongoTemplate.findOne(query, t, collectionName);
    }

    /**
     * 根据订单号和订单类型
     *
     * @param orderId   订单号
     * @param orderType 订单类型
     */
    public boolean updateById(String orderId, String orderType, Map<String, Object> params) {
        Query query = Query.query(Criteria.where("id").is(orderId));
        Update update = new Update();
        for (Map.Entry<String, Object> map : params.entrySet()) {
            update.set(map.getKey(), map.getValue());
        }
        String collectionName = "order_" + orderType;
        int num = mongoTemplate.updateFirst(query, update, collectionName).getN();
        if (num > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询单个
     *
     * @param query
     * @param t
     * @param <T>
     * @return
     */
    public <T> T findOne(Query query, Class<T> t) {
        return mongoTemplate.findOne(query, t);
    }

    /**
     * 查询列表
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(Class<T> t) {
        return mongoTemplate.findAll(t);
    }

    public static void main(String[] args) {
        String orderId = null;
        Assert.notNull(orderId, "订单号不能为空");
    }

}
