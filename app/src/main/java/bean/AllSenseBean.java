package bean;

/**
 * Created by lucky on 2019/2/28.
 */

public class AllSenseBean {

    /**
     * serverInfo : {"pm2.5":6,"co2":5919,"LightIntensity":1711,"humidity":44,"temperature":28}
     */

    private ServerInfoBean serverInfo;

    public ServerInfoBean getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfoBean serverInfo) {
        this.serverInfo = serverInfo;
    }

    public static class ServerInfoBean {
        private int _$Pm25254; // FIXME check this code
        private int co2;
        private int LightIntensity;
        private int humidity;
        private int temperature;

        @com.google.gson.annotations.SerializedName("pm2.5")
        public int get_$Pm25254() {
            return _$Pm25254;
        }

        public void set_$Pm25254(int _$Pm25254) {
            this._$Pm25254 = _$Pm25254;
        }

        public int getCo2() {
            return co2;
        }

        public void setCo2(int co2) {
            this.co2 = co2;
        }

        public int getLightIntensity() {
            return LightIntensity;
        }

        public void setLightIntensity(int LightIntensity) {
            this.LightIntensity = LightIntensity;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }
    }
}
