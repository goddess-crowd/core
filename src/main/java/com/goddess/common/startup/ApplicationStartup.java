package com.goddess.common.startup;

import com.goddess.common.util.SpringBootBeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Description: spring boot 容器加载完成后执行
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/9 下午8:23
 * @Copyright © 女神帮
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        /**
         * spring boot项目下，上面方式正确触发执行一次；如果是spring web项目下，
         * 可能会造成二次执行，因为此时系统会存在两个容器，一个是spring本身的root application context，
         * 另一个是servlet容器（作为spring容器的子容器，projectName-servlet context）
         * 此时，加以下限制条件规避：
         */
//        System.out.println(event.getApplicationContext().getApplicationName());
//        System.out.println("---------------------------------------------------------");
//        System.out.println(event.getApplicationContext().getParent().getParent());
//        System.out.println("---------------------------------------------------------");
        if(event.getApplicationContext().getParent().getParent()==null){
            //只有root application context 没有父容器
            ApplicationContext ac = event.getApplicationContext();
            SpringBootBeanUtils.setApplicationContext(ac);
        }
    }

}
