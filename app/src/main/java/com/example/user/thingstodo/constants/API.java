package com.example.user.thingstodo.constants;

/**
 * Created by user on 4/3/2019.
 */

public class API {
    private static final String ROOT_URL = "http://192.168.0.8:81/things_to_do/api/api.php?apicall=";
//    private static final String ROOT_URL = "http://192.168.1.69:81/things_to_do/api/api.php?apicall=";


    public static final String URL_INSERT_TASK =  ROOT_URL + "insertTask";
    public static final String URL_GET_TASK =  ROOT_URL + "getTask";
    public static final String URL_UPDATE_TASK =  ROOT_URL + "updateTask";
    public static final String URL_DELETE_TASK =  ROOT_URL + "deleteTask&task_id=";
    public static final String URL_HP_TASK =  ROOT_URL + "getHPTask";
    public static final String URL_MP_TASK =  ROOT_URL + "getMPTask";
    public static final String URL_LP_TASK =  ROOT_URL + "getLPTask";
    public static final String URL_DAILY_TASK =  ROOT_URL + "getDailyTask";
    public static final String URL_WEEKLY_TASK =  ROOT_URL + "getWeeklyTask";
    public static final String URL_COMPLETE_TASK =  ROOT_URL + "updateStatus";
    public static final String URL_COMPLETED_TASK =  ROOT_URL + "getCompletedTask";
    public static final String URL_DUE_TASK =  ROOT_URL + "getDueTask";


}
