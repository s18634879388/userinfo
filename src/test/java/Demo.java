import com.realmadrid.Application;
import com.realmadrid.util.NinehStringUtils;
import com.realmadrid.util.UCAgent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/8/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class Demo {
//    @Autowired
//    UCAgent ucAgent;


//    @Test
//    public void demo(){
//        String str = levelService.getLevel(20).getTitle();
//        System.out.println(str);
//        assertTrue((levelService.getLevel(20).getTitle()).equals("三夺冠军"));
//    }

    @Test
    public void demo2(){
        String request = NinehStringUtils.format( "/user/authorizedlogin2?&openid=%s&unionid=%s&appid=%s&authorizedtypeid=%s&&ip=%s&systemtypeid=%s&equipmentnum=%s",
                "null", null, null, null,
                null,
                null,
                null);
        System.out.println(request);
    }
}
