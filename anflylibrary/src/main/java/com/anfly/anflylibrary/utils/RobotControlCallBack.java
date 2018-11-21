package com.anfly.anflylibrary.utils;

public abstract class RobotControlCallBack {
    public void moveSuccess() {
    }

    public void moveFail() {
    }


    public void moveStart() {
    }

    public void moveRetry() {
    }

    public void moveCancle() {
    }

    public void strollComplete() {
    }

    public void strollFail() {
    }

    public void strollStart() {
    }

    public void strollCancel() {
    }

    //执行命令失败
    public void excuteError(String msg) {

    }

    //执行命令成功
    public void excuteSuccess() {

    }

}
