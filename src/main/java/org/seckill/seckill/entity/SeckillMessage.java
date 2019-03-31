package org.seckill.seckill.entity;

public class SeckillMessage {

    private long seckillId;

    private Long userPhone;

    private String md5;

    public SeckillMessage(long seckillId, Long userPhone, String md5) {
        this.seckillId = seckillId;
        this.userPhone = userPhone;
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public Long getUserPhone() {
        return userPhone;
    }

    public String getMd5() {
        return md5;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "SeckillMessage{" +
                "seckillId='" + seckillId + '\'' +
                ", userPhone=" + userPhone +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
