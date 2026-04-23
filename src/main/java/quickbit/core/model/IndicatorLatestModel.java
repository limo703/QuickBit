package quickbit.core.model;

import java.util.List;

public class IndicatorLatestModel {
    private Double ma;
    private Double ema;
    private List<Double> boll;
    private Double sar;
    private Double mavol;
    private List<Double> macd;
    private List<Double> kdj;
    private Double rsi;
    private Double wr;
    private List<Double> stochRsi;

    public Double getMa() {
        return ma;
    }

    public IndicatorLatestModel setMa(Double ma) {
        this.ma = ma;
        return this;
    }

    public Double getEma() {
        return ema;
    }

    public IndicatorLatestModel setEma(Double ema) {
        this.ema = ema;
        return this;
    }

    public List<Double> getBoll() {
        return boll;
    }

    public IndicatorLatestModel setBoll(List<Double> boll) {
        this.boll = boll;
        return this;
    }

    public Double getSar() {
        return sar;
    }

    public IndicatorLatestModel setSar(Double sar) {
        this.sar = sar;
        return this;
    }

    public Double getMavol() {
        return mavol;
    }

    public IndicatorLatestModel setMavol(Double mavol) {
        this.mavol = mavol;
        return this;
    }

    public List<Double> getMacd() {
        return macd;
    }

    public IndicatorLatestModel setMacd(List<Double> macd) {
        this.macd = macd;
        return this;
    }

    public List<Double> getKdj() {
        return kdj;
    }

    public IndicatorLatestModel setKdj(List<Double> kdj) {
        this.kdj = kdj;
        return this;
    }

    public Double getRsi() {
        return rsi;
    }

    public IndicatorLatestModel setRsi(Double rsi) {
        this.rsi = rsi;
        return this;
    }

    public Double getWr() {
        return wr;
    }

    public IndicatorLatestModel setWr(Double wr) {
        this.wr = wr;
        return this;
    }

    public List<Double> getStochRsi() {
        return stochRsi;
    }

    public IndicatorLatestModel setStochRsi(List<Double> stochRsi) {
        this.stochRsi = stochRsi;
        return this;
    }
}
