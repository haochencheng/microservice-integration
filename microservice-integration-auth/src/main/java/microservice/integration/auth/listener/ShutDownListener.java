package microservice.integration.auth.listener;

import com.netflix.discovery.DiscoveryManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: haochencheng
 * @create: 2020-02-24 11:39
 **/
@Component
public class ShutDownListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("程序关闭");
        DiscoveryManager.getInstance().shutdownComponent();
    }
}
