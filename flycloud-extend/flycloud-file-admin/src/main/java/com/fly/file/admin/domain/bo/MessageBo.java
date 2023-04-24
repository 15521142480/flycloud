package com.fly.file.admin.domain.bo;

import lombok.Data;

/**
 * 消息-bo
 *
 * @author lxs
 * @date 2023/4/24
 */
@Data
public class MessageBo {

    private int id;
    private int age;
    private String name;
    private String sex;
    private String love;
    private String phone;

    public MessageBo(Builder builder) {
        this.id = builder.id;
        this.age = builder.age;
        this.name = builder.name;
        this.sex = builder.sex;
        this.love = builder.love;
        this.phone = builder.phone;
    }


    public static class Builder {

        private int id = 0;
        private int age = 0;
        private String name = null;
        private String sex = null;
        private String love = null;
        private String phone = null;

        //构建的步骤
        public Builder addId(int id) {
            this.id = id;
            return this;
        }

        public Builder addAge(int age) {
            this.age = age;
            return this;
        }

        public Builder addName(String name) {
            this.name = name;
            return this;
        }

        public Builder addSex(String sex) {
            this.sex = sex;
            return this;
        }

        public Builder addLove(String love) {
            this.love = love;
            return this;
        }

        public Builder addPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public MessageBo build() {
            return new MessageBo(this);
        }
    }
}
