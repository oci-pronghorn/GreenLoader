package hello;

import java.util.Arrays;

import org.apache.coyote.AbstractProtocol;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer() throws Exception {

        return (ConfigurableEmbeddedServletContainer container) -> {

            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(
                        (connector) -> {
                            ((AbstractProtocol) connector.getProtocolHandler()).setPort(3302);
                            ((AbstractProtocol) connector.getProtocolHandler()).setMaxThreads(10_000);
                            ((AbstractProtocol) connector.getProtocolHandler()).setMaxConnections(10_000);
                            ((AbstractProtocol) connector.getProtocolHandler()).setAcceptCount(10_000);
                            ((AbstractProtocol) connector.getProtocolHandler()).setAcceptorThreadCount(50);
                            ((AbstractProtocol) connector.getProtocolHandler()).setConnectionTimeout(-1);
                            ((AbstractProtocol) connector.getProtocolHandler()).setKeepAliveTimeout(-1);
                        }
                );
            }
        };
    }
}