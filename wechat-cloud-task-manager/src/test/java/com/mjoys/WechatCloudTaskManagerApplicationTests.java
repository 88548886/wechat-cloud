package com.mjoys;

import com.mjoys.po.Account;
import com.mjoys.po.Task;
import com.mjoys.service.IAccountService;
import com.mjoys.service.ITaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatCloudTaskManagerApplicationTests {

    @Resource
    IAccountService accountService;

    @Resource
    ITaskService taskService;

    @Test
    public void testTaskService() {
        List<Task> allByActionSubmitStatus = taskService.findAllByActionSubmitStatus(Task
                .ACTION_SUBMIT_STATUS_NOT_SUBMIT);
        allByActionSubmitStatus.forEach(i -> System.out.println(i));
    }

    @Test
    public void testAccountService() {
        List<Account> mjoysAccount = accountService.findByUserId("mjoys");
        mjoysAccount.forEach(i -> System.out.print(i));

    }
}
