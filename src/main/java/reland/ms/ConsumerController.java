package reland.ms;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.SchemaOutputResolver;

/**
 * Created by zhaoyd on 18/2/16.
 */
@Import(FeignClientsConfiguration.class)
@RestController
public class ConsumerController {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ConsumerController.class);
    @Value("user.userServiceUrl")
    private String userServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private UserFeignClient userFeignClient;

    private UserFeignClient adminFeignClient;

//    @Autowired
//    public ConsumerController(Decoder decoder, Encoder encoder, Client client, Contract contract) {
//        this.userFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
//                .requestInterceptor(new BasicAuthRequestInterceptor("user", "password1")).target(UserFeignClient.class,
//                        "http://ms-provider-sample/");
//
//        this.adminFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
//                .requestInterceptor(new BasicAuthRequestInterceptor("admin", "password2")).target(UserFeignClient.class,
//                        "http://ms-provider-sample/");
//
//    }

    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
        System.out.println("abcdefghigk");
        ConsumerController.LOGGER.info("consumer-sample findById Method");
        return this.restTemplate.getForObject("http://ms-provider-sample/" + id, User.class);
//        return this.userFeignClient.findById(id);
    }

    @GetMapping("/log-instance")
    public void logUserInstance() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("ms-provider-sample");

        ConsumerController.LOGGER.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }

    @GetMapping("/user-user/{id}")
    public User findByIdUser(@PathVariable Long id) {
        return this.userFeignClient.findById(id);
    }

    @GetMapping("/user-admin/{id}")
    public User findByIdAdmin(@PathVariable Long id) {
        return this.adminFeignClient.findById(id);
    }
}