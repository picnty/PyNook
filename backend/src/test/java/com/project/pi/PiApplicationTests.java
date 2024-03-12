package com.project.pi;

import com.project.pi.PiApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

// @RunWith(org.junit.runner.RunWith.class)
// @AutoConfigureMockMvc // 开启虚拟MVC调用
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @RunWith(SpringRunner.class) // 表示启动这个单元测试类（单元测试类是不能运行的）,需要传递一个参数,必须是SpringRunner的实例类型
@SpringBootTest(classes = PiApplication.class)
@WebAppConfiguration
class PiApplicationTests {

    @org.junit.jupiter.api.Test
    void contextLoads() {
    }

    // @org.junit.Before
    public void init() {
        System.out.println("----- 开始测试 -----");
    }

    // @org.junit.After
    public void after() {
        System.out.println("----- 测试结束 -----");
    }

}
