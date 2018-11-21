package com.anfly.anflylibrary.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

import ai.yunji.running.RobotConnectAction;
import ai.yunji.running.RobotConnectCallBack;
import ai.yunji.running.constant.Notification;
import ai.yunji.running.entity.HotelNotification;
import ai.yunji.running.entity.HotelRobotResponse;
import ai.yunji.running.entity.Marker;
import ai.yunji.running.entity.MarkersReponse;
import ai.yunji.running.entity.RobotCurrentWifiInfoResponse;
import ai.yunji.running.entity.RobotCurrentWifiResponse;
import ai.yunji.running.entity.RobotGetCurrentMapResponse;
import ai.yunji.running.entity.RobotHardWareCheckUpdateResponse;
import ai.yunji.running.entity.RobotInfoResponse;
import ai.yunji.running.entity.RobotParamsReponse;
import ai.yunji.running.entity.RobotSoftWareCheckUpdateResponse;
import ai.yunji.running.entity.RobotStatusReponse;
import ai.yunji.running.entity.RobotVersionReponse;

public enum GlobalRobotControlUtil {
    INSTANCE;

    private android.os.Handler robotControlHandler = new android.os.Handler();

    private HashMap<String, Marker> markers;

    private final int requestStatusTime = 1000;

    private final String running = "running";

    private RobotStatusReponse robotStatus;

    public RobotStatusReponse getRobotStatus() {
        return robotStatus;
    }

    private Runnable getRobotStatusRunnable = new Runnable() {
        @Override
        public void run() {
            robotStatus = RobotConnectAction.init(null).getRobotStatus();
            if (robotStatus == null) {
                robotControlHandler.postDelayed(this::run, requestStatusTime);
                return;
            }

            RobotStatusReponse.ResultsBean results = robotStatus.results;

            if (results == null) {
                return;
            }

            isCharge = results.charge_state;

            barrage = results.power_percent;

            currentStopStatus = results.estop_state;

            isRunning = TextUtils.equals(running, results.move_status);

            moveTarget = results.move_target;

            robotControlHandler.postDelayed(this::run, requestStatusTime);
        }
    };


    GlobalRobotControlUtil() {
    }


    private boolean currentStopStatus;

    private int barrage;

    private boolean isCharge;

    private boolean isRunning;

    private boolean softStopStatus;

    private boolean hardStopStatus;

    private String moveTarget;

    public void init() {
        robotControlHandler.post(getRobotStatusRunnable);
    }

    /**
     * 获取当前所有点位
     *
     * @return
     */
    public HashMap<String, Marker> getMarkers() {
        if (markers == null || markers.isEmpty()) {
            getAllMarkers();
        }
        return markers;
    }

    /**
     * 获取当前急停状态
     *
     * @return
     */
    public boolean isCurrentStopStatus() {
        return currentStopStatus;
    }

    /**
     * 获取电量
     *
     * @return
     */
    public int getBarrage() {
        return barrage;
    }

    /**
     * 获取当前充电状态
     *
     * @return
     */
    public boolean isCharge() {
        return isCharge;
    }

    /**
     * 获取当前是否在移动
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 获取当前软急停状态
     *
     * @return
     */
    public boolean isSoftStopStatus() {
        return softStopStatus;
    }

    /**
     * 获取当前硬急停状态
     *
     * @return
     */
    public boolean isHardStopStatus() {
        return hardStopStatus;
    }

    /**
     * 获取当前移动点位名字
     *
     * @return
     */
    public String getMoveTarget() {
        return moveTarget;
    }


    private RobotControlCallBack robotControlCallBack;

    /**
     * 水滴移动回调
     */
    private RobotConnectCallBack robotConnectCallBack = new RobotConnectCallBack() {
        //获取所有点位map
        @Override
        public void robotMarkersListResult(HashMap<String, Marker> mapMarker) {
            super.robotMarkersListResult(mapMarker);

            if (mapMarker == null || mapMarker.isEmpty()) {
                return;
            }

            markers = mapMarker;
        }

        @Override
        public void robotMarkersBriefReponseResult(HashMap<String, Marker> mapMarker) {
            super.robotMarkersBriefReponseResult(mapMarker);
        }

        @Override
        public void robotMarkersListResultError() {
            super.robotMarkersListResultError();
        }

        @Override
        public void robotMarkersBriefResultError() {
            super.robotMarkersBriefResultError();
        }

        @Override
        public void robotMarkersListResultError(int floor) {
            super.robotMarkersListResultError(floor);
        }

        //移动结果回调
        @Override
        public void robotMoveResult(String code) {
            super.robotMoveResult(code);

            if (robotControlCallBack == null) {
                return;
            }

            switch (code) {
                case Notification.MOVE_START:
                    robotControlCallBack.moveStart();
                    break;
                case Notification.MOVE_FAILED:
                    robotControlCallBack.moveFail();
                    break;
                case Notification.MOVE_CANCEL:
                    robotControlCallBack.moveCancle();
                    break;
                case Notification.MOVE_FINISHED:
                    robotControlCallBack.moveSuccess();
                    break;
                case Notification.MOVE_RETRY:
                    robotControlCallBack.moveRetry();
                    break;
            }
        }

        @Override
        public void robotMoveResultError(String markerName) {
            super.robotMoveResultError(markerName);
        }

        @Override
        public void robotMoveResultError(float x, float y, float theta) {
            super.robotMoveResultError(x, y, theta);
        }

        @Override
        public void robotMoveMarkersError(ArrayList<Marker> markers, float distance_tolerance, int count) {
            super.robotMoveMarkersError(markers, distance_tolerance, count);
        }

        //通知回调
        @Override
        public void robotNotificationResult(HotelNotification info) {
            super.robotNotificationResult(info);

            if (info == null || robotControlCallBack == null) {
                return;
            }

            switch (info.code) {
                //巡游开始
                case Notification.CRUISE_STARTED:
                    robotControlCallBack.strollStart();
                    break;
                //巡游结束
                case Notification.CRUISE_FINISHED:
                    robotControlCallBack.strollComplete();
                    break;
                //巡游失败
                case Notification.CRUISE_FAILED:
                    robotControlCallBack.strollFail();
                    break;
                //巡游取消
                case Notification.CRUISE_CANCELED:
                    robotControlCallBack.strollCancel();
                    break;
            }
        }

        @Override
        public void robotStatusResult(RobotStatusReponse data) {
            super.robotStatusResult(data);
        }

        @Override
        public void robotCancelError() {
            super.robotCancelError();
        }

        @Override
        public void robotStatusResultError() {
            super.robotStatusResultError();
        }

        @Override
        public void robotSetParamsError(String task) {
            super.robotSetParamsError(task);
        }

        @Override
        public void robotGetParamsError(String task) {
            super.robotGetParamsError(task);
        }

        @Override
        public void robotShutDownError(String task) {
            super.robotShutDownError(task);
        }

        @Override
        public void robotGetRobotInfoError(String task) {
            super.robotGetRobotInfoError(task);
        }

        @Override
        public void robotTurnAroundError(float angular) {
            super.robotTurnAroundError(angular);
        }

        @Override
        public void robotTurnAroundError(float angular, int duration) {
            super.robotTurnAroundError(angular, duration);
        }

        @Override
        public void robotJoyControlError(float angular, float linear) {
            super.robotJoyControlError(angular, linear);
        }

        @Override
        public void robotResponseStatus(String status) {
            super.robotResponseStatus(status);
        }

        @Override
        public void robotHotelRobotResponse(HotelRobotResponse response) {
            super.robotHotelRobotResponse(response);
        }

        @Override
        public void robotSendTaskError(String task) {
            super.robotSendTaskError(task);
        }

        @Override
        public void robotEStopError(boolean isStop) {
            super.robotEStopError(isStop);
        }

        @Override
        public void robotPositionAdjustError(String markerName) {
            super.robotPositionAdjustError(markerName);
        }

        @Override
        public void robotInsertMarkerError(String name, int type) {
            super.robotInsertMarkerError(name, type);
        }

        @Override
        public void robotGetParams(RobotParamsReponse paramsReponse) {
            super.robotGetParams(paramsReponse);
        }

        @Override
        public void robotGetParamsError(RobotParamsReponse paramsReponse) {
            super.robotGetParamsError(paramsReponse);
        }

        @Override
        public void robotGetVersion(RobotVersionReponse version) {
            super.robotGetVersion(version);
        }

        @Override
        public void robotGetWifi(HashMap<String, String> mapWifi) {
            super.robotGetWifi(mapWifi);
        }

        @Override
        public void robotGetCurrentWifi(RobotCurrentWifiResponse currentWifiResponse) {
            super.robotGetCurrentWifi(currentWifiResponse);
        }

        @Override
        public void robotGetCurrentWifiInfo(RobotCurrentWifiInfoResponse robotCurrentWifiInfoResponse) {
            super.robotGetCurrentWifiInfo(robotCurrentWifiInfoResponse);
        }

        @Override
        public void robotGetMapList(HashMap<String, int[]> mapList) {
            super.robotGetMapList(mapList);
        }

        @Override
        public void robotGetCurrentMap(RobotGetCurrentMapResponse getCurrentMapResponse) {
            super.robotGetCurrentMap(getCurrentMapResponse);
        }

        @Override
        public void robotSoftwareCheckUpdate(RobotSoftWareCheckUpdateResponse checkUpdateResponse) {
            super.robotSoftwareCheckUpdate(checkUpdateResponse);
        }

        @Override
        public void robotHardwareCheckUpdate(RobotHardWareCheckUpdateResponse checkUpdateResponse) {
            super.robotHardwareCheckUpdate(checkUpdateResponse);
        }

        @Override
        public void robotGetInfo(RobotInfoResponse robotInfoResponse) {
            super.robotGetInfo(robotInfoResponse);
        }

        //错误信息回调
        @Override
        public void error(String msg) {
            super.error(msg);
        }

        @Override
        public void robotMarkersCount(int count) {
            super.robotMarkersCount(count);
        }

        @Override
        public void robotGetMarkersCountError() {
            super.robotGetMarkersCountError();
        }
    };

    /**
     * 重新获取点位
     *
     * @return
     */
    public void getAllMarkers() {
        MarkersReponse markersReponse = RobotConnectAction.init(null).getMarkers();
        if (markersReponse == null) {
            return;
        }

        markers = markersReponse.results;
    }

    /**
     * 移动
     *
     * @param marker
     * @param robotControlCallBack
     */
    public void move(String marker, RobotControlCallBack robotControlCallBack) {
        if (isRunning) {
            robotControlCallBack.excuteError("the robot is running");
            return;
        }

        if (robotControlCallBack == null || TextUtils.isEmpty(marker) || getMarkers() == null || markers.isEmpty()) {
            return;
        }

        Marker targetMarker = markers.get(marker);

        if (targetMarker == null) {
            return;
        }


        this.robotControlCallBack = robotControlCallBack;

        RobotConnectAction.init(null).sendMoveMarker(targetMarker, robotConnectCallBack);
    }

    /**
     * 取消移动
     */
    public void cancelMove() {
        RobotConnectAction.init(null).moveCancel();
    }


    /**
     * 巡游
     *
     * @param markerNames
     * @param count
     * @param robotControlCallBack
     */
    public void stroll(String[] markerNames, int count, RobotControlCallBack robotControlCallBack) {
        if (isRunning) {
            robotControlCallBack.excuteError("the robot is running");
            return;
        }

        if (markers == null || markers.isEmpty()) {
            robotControlCallBack.excuteError("the markers is null or empty");
            return;
        }
        if (markerNames == null || markerNames.length == 0 || markerNames.length == 1) {
            robotControlCallBack.excuteError("the markerNames is null or length is less than 2");
            return;
        }

        ArrayList<Marker> strollMarkers = new ArrayList<>();

        for (String name : markerNames) {
            Marker strollMarker = markers.get(name);
            if (strollMarker != null) {
                strollMarkers.add(strollMarker);
            }
        }

    }

    /**
     * 巡游
     *
     * @param markerNames
     * @param count
     * @param robotControlCallBack
     */
    public void stroll(ArrayList<String> markerNames, int count, RobotControlCallBack robotControlCallBack) {
        if (markerNames == null || markerNames.size() <= 1) {
            robotControlCallBack.excuteError("the markerNames is null or size less than 2");
            return;
        }

        stroll(markerNames.toArray(new String[0]), count, robotControlCallBack);

    }

    public void startStroll(ArrayList<Marker> strollMarkers, int count, RobotControlCallBack robotControlCallBack) {
        if (strollMarkers == null || strollMarkers.isEmpty() || strollMarkers.size() <= 1) {
            robotControlCallBack.excuteError("the strollMarkers is null or size less than 2");
            return;
        }

        this.robotControlCallBack = robotControlCallBack;

        RobotConnectAction.init(null).sendMoveMarkers(strollMarkers, count, robotConnectCallBack);
    }
}
