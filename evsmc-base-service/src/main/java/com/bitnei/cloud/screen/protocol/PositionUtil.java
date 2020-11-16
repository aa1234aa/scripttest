package com.bitnei.cloud.screen.protocol;

import com.bitnei.cloud.screen.model.Gps;

/**
 * @author xuzhijie
 */
public class PositionUtil {
    private static double xpi = 52.35987755982988D;
    private static double pi = 3.141592653589793D;
    private static double a = 6378245.0D;
    private static double ee = 0.006693421622965943D;

    public PositionUtil() {
    }

    public static Gps gps84ToGcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return null;
        } else {
            double dLat = transformLat(lon - 105.0D, lat - 35.0D);
            double dLon = transformLon(lon - 105.0D, lat - 35.0D);
            double radLat = lat / 180.0D * pi;
            double magic = Math.sin(radLat);
            magic = 1.0D - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = dLat * 180.0D / (a * (1.0D - ee) / (magic * sqrtMagic) * pi);
            dLon = dLon * 180.0D / (a / sqrtMagic * Math.cos(radLat) * pi);
            double mgLat = lat + dLat;
            double mgLon = lon + dLon;
            return new Gps(mgLat, mgLon);
        }
    }

    public static Gps gcj02ToGps84(double lat, double lon) {
        Gps gps = transform(lat, lon);
        double lontitude = lon * 2.0D - gps.getWgLon();
        double latitude = lat * 2.0D - gps.getWgLat();
        return new Gps(latitude, lontitude);
    }

    public static Gps gcj02ToBd09(double lat, double lon) {
        double z = Math.sqrt(lon * lon + lat * lat) + 2.0E-5D * Math.sin(lat * xpi);
        double theta = Math.atan2(lat, lon) + 3.0E-6D * Math.cos(lon * xpi);
        double bdLon = z * Math.cos(theta) + 0.0065D;
        double bdLat = z * Math.sin(theta) + 0.006D;
        return new Gps(bdLat, bdLon);
    }

    public static Gps bd09ToGcj02(double bdLat, double bdLon) {
        double x = bdLon - 0.0065D;
        double y = bdLat - 0.006D;
        double z = Math.sqrt(x * x + y * y) - 2.0E-5D * Math.sin(y * xpi);
        double theta = Math.atan2(y, x) - 3.0E-6D * Math.cos(x * xpi);
        double lon = z * Math.cos(theta);
        double lat = z * Math.sin(theta);
        return new Gps(lat, lon);
    }

    public static Gps bd09ToGps84(double lat, double lon) {
        Gps gcj02 = bd09ToGcj02(lat, lon);
        return gcj02ToGps84(gcj02.getWgLat(), gcj02.getWgLon());
    }

    public static Gps gps84ToBd09(double lat, double lon) {
        Gps gcj02 = gps84ToGcj02(lat, lon);
        return gcj02 == null ? null : gcj02ToBd09(gcj02.getWgLat(), gcj02.getWgLon());
    }

    public static boolean outOfChina(double lat, double lon) {
        double minLon = 72.004D;
        double maxLon = 137.8347D;
        double minLat = 0.8293D;
        double maxLat = 55.8271D;
        return lon < minLon || lon > maxLon || lat < minLat || lat > maxLat;
    }

    public static Gps transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new Gps(lat, lon);
        } else {
            double dLat = transformLat(lon - 105.0D, lat - 35.0D);
            double dLon = transformLon(lon - 105.0D, lat - 35.0D);
            double radLat = lat / 180.0D * pi;
            double magic = Math.sin(radLat);
            magic = 1.0D - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = dLat * 180.0D / (a * (1.0D - ee) / (magic * sqrtMagic) * pi);
            dLon = dLon * 180.0D / (a / sqrtMagic * Math.cos(radLat) * pi);
            double mgLat = lat + dLat;
            double mgLon = lon + dLon;
            return new Gps(mgLat, mgLon);
        }
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x * y + 0.2D * Math.sqrt(Math.abs(x));
        ret += (20.0D * Math.sin(6.0D * x * pi) + 20.0D * Math.sin(2.0D * x * pi)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(y * pi) + 40.0D * Math.sin(y / 3.0D * pi)) * 2.0D / 3.0D;
        ret += (160.0D * Math.sin(y / 12.0D * pi) + 320.0D * Math.sin(y * pi / 30.0D)) * 2.0D / 3.0D;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D * Math.sqrt(Math.abs(x));
        ret += (20.0D * Math.sin(6.0D * x * pi) + 20.0D * Math.sin(2.0D * x * pi)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(x * pi) + 40.0D * Math.sin(x / 3.0D * pi)) * 2.0D / 3.0D;
        ret += (150.0D * Math.sin(x / 12.0D * pi) + 300.0D * Math.sin(x / 30.0D * pi)) * 2.0D / 3.0D;
        return ret;
    }

    public static void main(String[] args) {
        Gps gps = new Gps(31.164601D, 121.486063D);
        System.out.println("wgs84 :" + gps);
        Gps gcj = gps84ToGcj02(gps.getWgLat(), gps.getWgLon());
        System.out.println("gcj02:" + gcj);

        assert gcj != null;

        Gps star = gcj02ToGps84(gcj.getWgLat(), gcj.getWgLon());
        System.out.println("wgs84:" + star);
        Gps bd = gcj02ToBd09(gcj.getWgLat(), gcj.getWgLon());
        System.out.println("bd09:" + bd);
        Gps gcj2 = bd09ToGcj02(bd.getWgLat(), bd.getWgLon());
        System.out.println("gcj02:" + gcj2);
    }
}
