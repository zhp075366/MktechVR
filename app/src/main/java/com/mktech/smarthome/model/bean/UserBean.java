package com.mktech.smarthome.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 作者：Created by ZouHaiping on 2017/5/26
 * 邮箱：haiping.zou@gotechcn.cn
 * 公司：MKTech
 */
@Entity(generateConstructors = true,
        //设置在数据库中的表名,默认为对应的类名
        nameInDb = "USERS_TABLE")
public class UserBean {
    @Id(autoincrement = true)
    private Long id;
    private String username;
    private String password;
    private Long testLong;

    @Generated(hash = 1884123126)
    public UserBean(Long id, String username, String password, Long testLong) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.testLong = testLong;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestLong() {
        return this.testLong;
    }

    public void setTestLong(Long testLong) {
        this.testLong = testLong;
    }
}
