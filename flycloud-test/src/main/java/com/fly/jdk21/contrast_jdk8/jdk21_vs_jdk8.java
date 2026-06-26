package com.fly.jdk21.contrast_jdk8;

import java.util.*;
import java.util.stream.Collectors;

/**
 * d
 *
 * @author: lxs
 * @date: 2026/6/26
 */
public class jdk21_vs_jdk8 {


    public static void main(String[] args) {
        compareSwitch();
        compareInstanceof();
        compareTextBlock();
        compareCollectionCreate();
        compareStreamToList();
        compareStringMethod();
        compareVar();
        compareRecord();
        compareOptional();
        compareSealed();
    }


    /**
     * 1. switch 写法对比
     * <p>
     * 常用程度：★★★★★
     */
    public static void compareSwitch() {
        System.out.println("========== 1. switch 写法对比 ==========");

        String type = "A";

        // JDK8 写法：case + break
        String jdk8Result;

        switch (type) {
            case "A":
                jdk8Result = "新增";
                break;
            case "B":
                jdk8Result = "修改";
                break;
            case "C":
                jdk8Result = "删除";
                break;
            default:
                jdk8Result = "未知";
                break;
        }

        System.out.println("JDK8 switch = " + jdk8Result);


        // JDK21 写法：switch 表达式，case ->，不需要 break
        String jdk21Result = switch (type) {
            case "A" -> "新增";
            case "B" -> "修改";
            case "C" -> "删除";
            default -> "未知";
        };

        System.out.println("JDK21 switch = " + jdk21Result);


        // JDK8 多 case 合并
        String role = "admin";
        String jdk8RoleName;

        switch (role) {
            case "admin":
            case "super_admin":
                jdk8RoleName = "管理员";
                break;
            case "user":
                jdk8RoleName = "普通用户";
                break;
            default:
                jdk8RoleName = "未知角色";
                break;
        }

        System.out.println("JDK8 多 case = " + jdk8RoleName);


        // JDK21 多 case 合并
        String jdk21RoleName = switch (role) {
            case "admin", "super_admin" -> "管理员";
            case "user" -> "普通用户";
            default -> "未知角色";
        };

        System.out.println("JDK21 多 case = " + jdk21RoleName);


        // JDK21 switch 代码块 + yield
        Integer status = 1;

        String statusName = switch (status) {
            case 0 -> "禁用";
            case 1 -> {
                System.out.println("JDK21 switch yield：这里可以写多行逻辑");
                yield "正常";
            }
            case 2 -> "锁定";
            default -> "未知状态";
        };

        System.out.println("JDK21 switch yield = " + statusName);
    }


    /**
     * 2. instanceof 写法对比
     * <p>
     * 常用程度：★★★★★
     */
    public static void compareInstanceof() {
        System.out.println("========== 2. instanceof 写法对比 ==========");

        Object obj = "hello";


        // JDK8 写法：先判断，再强转
        if (obj instanceof String) {
            String str = (String) obj;
            System.out.println("JDK8 instanceof = " + str.length());
        }


        // JDK21 写法：判断时直接声明变量
        if (obj instanceof String str) {
            System.out.println("JDK21 instanceof = " + str.length());
        }


        // JDK21 switch 模式匹配：判断对象类型
        Object value = 100;

        String result = switch (value) {
            case String str -> "字符串：" + str;
            case Integer num -> "整数：" + num;
            case Long num -> "Long数字：" + num;
            case null -> "空值";
            default -> "其他类型";
        };

        System.out.println("JDK21 switch 模式匹配 = " + result);
    }


    /**
     * 3. 多行字符串写法对比
     * <p>
     * 常用程度：★★★★★
     * <p>
     * 写 SQL、JSON、XML 时非常常用
     */
    public static void compareTextBlock() {
        System.out.println("========== 3. 多行字符串写法对比 ==========");


        // JDK8 写法：字符串拼接
        String jdk8Sql = "select * from sys_user " +
                "where username = ? " +
                "and status = ?";

        System.out.println("JDK8 SQL = " + jdk8Sql);


        // JDK21 写法：文本块
        String jdk21Sql = """
                select *
                from sys_user
                where username = ?
                  and status = ?
                """;

        System.out.println("JDK21 SQL = " + jdk21Sql);


        // JDK21 写 JSON
        String json = """
                {
                  "username": "admin",
                  "password": "123456"
                }
                """;

        System.out.println("JDK21 JSON = " + json);
    }


    /**
     * 4. 集合创建写法对比
     * <p>
     * 常用程度：★★★★★
     */
    public static void compareCollectionCreate() {
        System.out.println("========== 4. 集合创建写法对比 ==========");


        // JDK8 List 写法
        List<String> jdk8List = Arrays.asList("A", "B", "C");

        // JDK8 Set 写法
        Set<String> jdk8Set = new HashSet<>();
        jdk8Set.add("A");
        jdk8Set.add("B");

        // JDK8 Map 写法
        Map<String, Object> jdk8Map = new HashMap<>();
        jdk8Map.put("username", "admin");
        jdk8Map.put("age", 18);

        System.out.println("JDK8 List = " + jdk8List);
        System.out.println("JDK8 Set = " + jdk8Set);
        System.out.println("JDK8 Map = " + jdk8Map);


        // JDK21 List.of
        List<String> jdk21List = List.of("A", "B", "C");

        // JDK21 Set.of
        Set<String> jdk21Set = Set.of("A", "B");

        // JDK21 Map.of
        Map<String, Object> jdk21Map = Map.of(
                "username", "admin",
                "age", 18
        );

        System.out.println("JDK21 List = " + jdk21List);
        System.out.println("JDK21 Set = " + jdk21Set);
        System.out.println("JDK21 Map = " + jdk21Map);

        // 注意：
        // List.of / Set.of / Map.of 创建的是不可变集合
        // jdk21List.add("D"); // 会报错
    }


    /**
     * 5. Stream 转 List 写法对比
     * <p>
     * 常用程度：★★★★★
     */
    public static void compareStreamToList() {
        System.out.println("========== 5. Stream 转 List 写法对比 ==========");

        List<User> users = Arrays.asList(
                new User(1L, "admin", 18),
                new User(2L, "zhangsan", 20)
        );


        // JDK8 写法：collect(Collectors.toList())
        List<String> jdk8Names = users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        System.out.println("JDK8 Stream List = " + jdk8Names);


        // JDK21 写法：toList()
        List<String> jdk21Names = users.stream()
                .map(User::getUsername)
                .toList();

        System.out.println("JDK21 Stream List = " + jdk21Names);

        // 注意：
        // stream().toList() 返回的 List 通常不要再修改
    }


    /**
     * 6. String 常用方法对比
     * <p>
     * 常用程度：★★★★☆
     */
    public static void compareStringMethod() {
        System.out.println("========== 6. String 常用方法对比 ==========");

        String text = "   ";


        // JDK8 判断空白字符串
        if (text == null || text.trim().isEmpty()) {
            System.out.println("JDK8：字符串为空白");
        }


        // JDK21 判断空白字符串
        if (text == null || text.isBlank()) {
            System.out.println("JDK21：字符串为空白");
        }


        String hello = "  hello  ";

        // JDK8 去除前后空格
        System.out.println("JDK8 trim = [" + hello.trim() + "]");

        // JDK21 去除前后空白
        System.out.println("JDK21 strip = [" + hello.strip() + "]");


        // JDK8 重复字符串
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            builder.append("-");
        }
        System.out.println("JDK8 repeat = " + builder);


        // JDK21 重复字符串
        String line = "-".repeat(5);
        System.out.println("JDK21 repeat = " + line);


        // JDK21 按行处理
        String multiLineText = """
                Java
                Spring Boot
                MyBatis
                """;

        multiLineText.lines()
                .forEach(item -> System.out.println("JDK21 lines = " + item));
    }


    /**
     * 7. var 局部变量类型推断
     * <p>
     * 常用程度：★★★★☆
     */
    public static void compareVar() {
        System.out.println("========== 7. var 写法对比 ==========");


        // JDK8 写法：必须显式声明类型
        String jdk8Name = "admin";
        List<String> jdk8List = new ArrayList<>();
        Map<String, Object> jdk8Map = new HashMap<>();

        jdk8List.add("A");
        jdk8Map.put("username", jdk8Name);

        System.out.println("JDK8 name = " + jdk8Name);
        System.out.println("JDK8 list = " + jdk8List);
        System.out.println("JDK8 map = " + jdk8Map);


        // JDK21 写法：局部变量可以使用 var
        var jdk21Name = "admin";
        var jdk21List = new ArrayList<String>();
        var jdk21Map = new HashMap<String, Object>();

        jdk21List.add("A");
        jdk21Map.put("username", jdk21Name);

        System.out.println("JDK21 name = " + jdk21Name);
        System.out.println("JDK21 list = " + jdk21List);
        System.out.println("JDK21 map = " + jdk21Map);

        // 注意：
        // var 只能用于局部变量
        // 成员变量、方法参数、方法返回值不能用 var
    }


    /**
     * 8. DTO / VO 写法对比
     * <p>
     * 常用程度：★★★★☆
     */
    public static void compareRecord() {
        System.out.println("========== 8. DTO / VO 写法对比 ==========");


        // JDK8 DTO 写法：普通 class
        UserDTO jdk8UserDTO = new UserDTO(1L, "admin");

        System.out.println("JDK8 DTO id = " + jdk8UserDTO.getId());
        System.out.println("JDK8 DTO username = " + jdk8UserDTO.getUsername());


        // JDK21 DTO 写法：record
        UserRecord jdk21UserRecord = new UserRecord(1L, "admin");

        System.out.println("JDK21 record id = " + jdk21UserRecord.id());
        System.out.println("JDK21 record username = " + jdk21UserRecord.username());
        System.out.println("JDK21 record = " + jdk21UserRecord);

        // 注意：
        // record 默认是不可变对象
        // 适合 DTO、VO、查询结果对象
        // 不太适合 JPA Entity、MyBatis 需要 set 的实体类
    }


    /**
     * 9. Optional 判空写法对比
     * <p>
     * 常用程度：★★★☆☆
     */
    public static void compareOptional() {
        System.out.println("========== 9. Optional 写法对比 ==========");

        User user = new User(1L, "admin", 18);


        // JDK8 传统判空
        String jdk8Name;

        if (user != null) {
            jdk8Name = user.getUsername();
        } else {
            jdk8Name = "未知用户";
        }

        System.out.println("JDK8 if 判空 = " + jdk8Name);


        // JDK8 就已经支持 Optional
        String optionalName = Optional.ofNullable(user)
                .map(User::getUsername)
                .orElse("未知用户");

        System.out.println("JDK8 Optional = " + optionalName);


        // JDK21 也可以继续这么用
        String jdk21Name = Optional.ofNullable(user)
                .map(User::getUsername)
                .orElse("未知用户");

        System.out.println("JDK21 Optional = " + jdk21Name);

        // 注意：
        // Optional 不要滥用
        // 简单判空时，if 反而更清楚
    }


    /**
     * 10. sealed 密封类
     * <p>
     * 常用程度：★★☆☆☆
     * <p>
     * 这个不是每天都会用，但是做固定类型限制时很好用
     */
    public static void compareSealed() {
        System.out.println("========== 10. sealed 写法对比 ==========");


        // JDK8 写法：普通接口，谁都可以实现
        Jdk8Result jdk8Success = new Jdk8Success("操作成功");
        Jdk8Result jdk8Fail = new Jdk8Fail("操作失败");

        System.out.println("JDK8 result success = " + jdk8Success.getMessage());
        System.out.println("JDK8 result fail = " + jdk8Fail.getMessage());


        // JDK21 写法：sealed 限制只能由指定类实现
        Result jdk21Success = new Success("操作成功");
        Result jdk21Fail = new Fail("操作失败");

        printResult(jdk21Success);
        printResult(jdk21Fail);
    }


    /**
     * JDK21 sealed + switch 模式匹配
     */
    public static void printResult(Result result) {
        String message = switch (result) {
            case Success success -> "成功：" + success.message();
            case Fail fail -> "失败：" + fail.message();
        };

        System.out.println("JDK21 sealed result = " + message);
    }


    /**
     * 普通实体类
     */
    public static class User {

        private Long id;

        private String username;

        private Integer age;

        public User(Long id, String username, Integer age) {
            this.id = id;
            this.username = username;
            this.age = age;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public Integer getAge() {
            return age;
        }
    }


    /**
     * JDK8 DTO 写法
     */
    public static class UserDTO {

        private Long id;

        private String username;

        public UserDTO(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
    }


    /**
     * JDK21 record 写法
     * <p>
     * 自动生成：
     * 1. 构造方法
     * 2. id()
     * 3. username()
     * 4. toString()
     * 5. equals()
     * 6. hashCode()
     */
    public record UserRecord(Long id, String username) {
    }


    /**
     * JDK8 普通接口
     */
    public interface Jdk8Result {
        String getMessage();
    }

    public static class Jdk8Success implements Jdk8Result {

        private final String message;

        public Jdk8Success(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    public static class Jdk8Fail implements Jdk8Result {

        private final String message;

        public Jdk8Fail(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }


    /**
     * JDK21 sealed 接口
     * <p>
     * Result 只允许 Success 和 Fail 实现
     */
    public sealed interface Result permits Success, Fail {
    }

    public record Success(String message) implements Result {
    }

    public record Fail(String message) implements Result {
    }




}
