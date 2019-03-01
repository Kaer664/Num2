package bean;

/**
 * Created by lucky on 2019/3/1.
 */

public class TrafficLightConfigActionBean {

    /**
     * serverInfo : {"RedTime":"1","GreenTime":"2","YellowTime":"48"}
     */

    private ServerInfoBean serverInfo;

    public ServerInfoBean getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfoBean serverInfo) {
        this.serverInfo = serverInfo;
    }

    public static class ServerInfoBean {
        /**
         * RedTime : 1
         * GreenTime : 2
         * YellowTime : 48
         */

        private String RedTime;
        private String GreenTime;
        private String YellowTime;

        public String getRedTime() {
            return RedTime;
        }

        public void setRedTime(String RedTime) {
            this.RedTime = RedTime;
        }

        public String getGreenTime() {
            return GreenTime;
        }

        public void setGreenTime(String GreenTime) {
            this.GreenTime = GreenTime;
        }

        public String getYellowTime() {
            return YellowTime;
        }

        public void setYellowTime(String YellowTime) {
            this.YellowTime = YellowTime;
        }
    }
}
