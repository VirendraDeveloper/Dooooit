package cl.activaresearch.android_app.Dooit.models;

import java.util.List;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 20 Jun,2018
 */
public class RegionBean {

    /**
     * regionId : 1
     * regionName : Tarapacá
     * cities : [{"cityId":1107,"cityName":"Alto Hospicio"},{"cityId":1402,"cityName":"Camiña"},{"cityId":1403,"cityName":"Colchane"},{"cityId":1404,"cityName":"Huara"},{"cityId":1101,"cityName":"Iquique"},{"cityId":1405,"cityName":"Pica"},{"cityId":1401,"cityName":"Pozo Almonte"}]
     */

    private int regionId;
    private String regionName;
    private List<CitiesBean> cities;

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<CitiesBean> getCities() {
        return cities;
    }

    public void setCities(List<CitiesBean> cities) {
        this.cities = cities;
    }

    public static class CitiesBean {
        /**
         * cityId : 1107
         * cityName : Alto Hospicio
         */

        private int cityId;
        private String cityName;

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }
}
