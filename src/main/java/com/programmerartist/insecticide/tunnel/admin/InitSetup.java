package com.programmerartist.insecticide.tunnel.admin;

import com.programmerartist.insecticide.conf.Setup;
import com.programmerartist.insecticide.tunnel.admin.constants.DomainInit;
import com.programmerartist.insecticide.util.HttpUtill;

/**
 * Created by 程序员Artist on 16/3/24.
 */
public class InitSetup {


    /**
     * 发送相关 "设置" 给远程控制器
     *
     * @param domain
     * @throws Exception
     */
    public static void init(DomainInit domain) throws Exception {

        String url = Setup.Init.URL.getValue() + "?traceName=" + domain.getTraceName() + "&stop=" + domain.isStop();
        HttpUtill.get(url);
    }
}
