/**
 * Created by Walker.Zhang on 2015/4/4.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/4     Walker.Zhang     0.1.0        Created.
 */
package com.patrol.simulator;


import com.flying.framework.messaging.endpoint.Endpoint;
import com.flying.framework.messaging.endpoint.IEndpoint;
import com.flying.framework.messaging.engine.IPinger;
import com.flying.framework.messaging.engine.impl.ZMQPinger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingerMain {
    private static final Logger logger = LoggerFactory.getLogger(PingerMain.class);
    private static final long TIMES = Long.MAX_VALUE;

    public static void main(String args[]) {
        long succeed = 0;
        long failure = 0;
        IPinger pinger = new ZMQPinger();
        pinger.start();
        IEndpoint endpoint = new Endpoint("tcp://127.0.0.1:16888");
        for (long i = 0; i < TIMES; i++) {
            try {
                long start =System.currentTimeMillis();
                if (pinger.ping(endpoint, 10000))
                    succeed++;
                else
                    failure++;
                System.out.println(System.currentTimeMillis()-start);
            } catch (Exception ie) {
                logger.error("Interrupted when sweeping...", ie);
            }
            if (i % 100 == 0) {
                System.out.println("succeed:" + succeed);
                System.out.println("failure:" + failure);
            }
        }
        pinger.stop();
    }

}
