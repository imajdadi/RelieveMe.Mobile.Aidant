package org.snowcorp.login.helper;

public
class StaticData {
    public static  String ConnectedUSerId;
    public static  int WatchUserId;
    public static  WatchUser WatchUser;
    public static TaskPlan Tasks;

    public static void InitWatchUser(String uniqueCode)
    {
        WatchUser = new WatchUser (uniqueCode);
    }
}
