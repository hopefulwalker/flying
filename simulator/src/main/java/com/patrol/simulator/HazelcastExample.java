/**
 * Created by Walker.Zhang on 2015/7/22.
 * Revision History:
 * Date          Who              Version      What
 * 2015/7/22     Walker.Zhang     0.1.0        Created.
 */
package com.patrol.simulator;

import com.flying.common.model.IExchId;
import com.flying.oms.model.IBsSideId;
import com.flying.oms.model.OrderBO;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public class HazelcastExample {
    public static void main(String[] args) throws Exception {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Thread.sleep(1000);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName( "orderBOs" ).setInMemoryFormat(InMemoryFormat.OBJECT);


        Map<Long, OrderBO> orderBOs = hz.getMap("orderBOs");
        orderBOs.clear();
        OrderBO orderBO = new OrderBO();
        orderBO.setAcctId(10000);
        orderBO.setExchId(IExchId.SHANGHAI);
        orderBO.setSectCode("600001");
        orderBO.setBsSideId(IBsSideId.BUY);
        orderBO.setPrice(11.1);
        orderBO.setQty(9999);

        long start = System.currentTimeMillis();
        for (long i = 1; i < 50000; i++) {
            orderBO.setExtNo(i);
            orderBOs.put(i, orderBO);
        }
        System.out.println("Put Cost:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (long i = 1; i < 50000; i++) {
            orderBOs.get(i);
        }
        System.out.println("Get Cost:" + (System.currentTimeMillis() - start));

        System.out.println("Known orders: " + orderBOs.size());
        System.out.println("OrderBO of 1501: " + BeanUtils.describe(orderBOs.get(1501L)));
    }
}
