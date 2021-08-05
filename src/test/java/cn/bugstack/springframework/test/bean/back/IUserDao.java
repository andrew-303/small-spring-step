package cn.bugstack.springframework.test.bean.back;

/**
 * 定义一个IUserDao接口，之所以这样做是为了通过FactoryBean做一个自定义对象的代理操作
 */
public interface IUserDao {

    String queryUserName(String uId);

}
