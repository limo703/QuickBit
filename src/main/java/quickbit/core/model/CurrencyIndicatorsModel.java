package quickbit.core.model;

import java.util.ArrayList;
import java.util.List;

public class CurrencyIndicatorsModel {
    private List<String> labels = new ArrayList<>();
    private List<Double> closePrices = new ArrayList<>();
    private List<Double> ma = new ArrayList<>();
    private List<Double> ema = new ArrayList<>();
    private List<Double> bollUpper = new ArrayList<>();
    private List<Double> bollMiddle = new ArrayList<>();
    private List<Double> bollLower = new ArrayList<>();
    private List<Double> sar = new ArrayList<>();
    private List<Double> mavol = new ArrayList<>();
    private List<Double> macd = new ArrayList<>();
    private List<Double> macdSignal = new ArrayList<>();
    private List<Double> macdHistogram = new ArrayList<>();
    private List<Double> kdjK = new ArrayList<>();
    private List<Double> kdjD = new ArrayList<>();
    private List<Double> kdjJ = new ArrayList<>();
    private List<Double> rsi = new ArrayList<>();
    private List<Double> wr = new ArrayList<>();
    private List<Double> stochRsiK = new ArrayList<>();
    private List<Double> stochRsiD = new ArrayList<>();
    private IndicatorLatestModel latest = new IndicatorLatestModel();

    public List<String> getLabels() {
        return labels;
    }

    public CurrencyIndicatorsModel setLabels(List<String> labels) {
        this.labels = labels;
        return this;
    }

    public List<Double> getClosePrices() {
        return closePrices;
    }

    public CurrencyIndicatorsModel setClosePrices(List<Double> closePrices) {
        this.closePrices = closePrices;
        return this;
    }

    public List<Double> getMa() {
        return ma;
    }

    public CurrencyIndicatorsModel setMa(List<Double> ma) {
        this.ma = ma;
        return this;
    }

    public List<Double> getEma() {
        return ema;
    }

    public CurrencyIndicatorsModel setEma(List<Double> ema) {
        this.ema = ema;
        return this;
    }

    public List<Double> getBollUpper() {
        return bollUpper;
    }

    public CurrencyIndicatorsModel setBollUpper(List<Double> bollUpper) {
        this.bollUpper = bollUpper;
        return this;
    }

    public List<Double> getBollMiddle() {
        return bollMiddle;
    }

    public CurrencyIndicatorsModel setBollMiddle(List<Double> bollMiddle) {
        this.bollMiddle = bollMiddle;
        return this;
    }

    public List<Double> getBollLower() {
        return bollLower;
    }

    public CurrencyIndicatorsModel setBollLower(List<Double> bollLower) {
        this.bollLower = bollLower;
        return this;
    }

    public List<Double> getSar() {
        return sar;
    }

    public CurrencyIndicatorsModel setSar(List<Double> sar) {
        this.sar = sar;
        return this;
    }

    public List<Double> getMavol() {
        return mavol;
    }

    public CurrencyIndicatorsModel setMavol(List<Double> mavol) {
        this.mavol = mavol;
        return this;
    }

    public List<Double> getMacd() {
        return macd;
    }

    public CurrencyIndicatorsModel setMacd(List<Double> macd) {
        this.macd = macd;
        return this;
    }

    public List<Double> getMacdSignal() {
        return macdSignal;
    }

    public CurrencyIndicatorsModel setMacdSignal(List<Double> macdSignal) {
        this.macdSignal = macdSignal;
        return this;
    }

    public List<Double> getMacdHistogram() {
        return macdHistogram;
    }

    public CurrencyIndicatorsModel setMacdHistogram(List<Double> macdHistogram) {
        this.macdHistogram = macdHistogram;
        return this;
    }

    public List<Double> getKdjK() {
        return kdjK;
    }

    public CurrencyIndicatorsModel setKdjK(List<Double> kdjK) {
        this.kdjK = kdjK;
        return this;
    }

    public List<Double> getKdjD() {
        return kdjD;
    }

    public CurrencyIndicatorsModel setKdjD(List<Double> kdjD) {
        this.kdjD = kdjD;
        return this;
    }

    public List<Double> getKdjJ() {
        return kdjJ;
    }

    public CurrencyIndicatorsModel setKdjJ(List<Double> kdjJ) {
        this.kdjJ = kdjJ;
        return this;
    }

    public List<Double> getRsi() {
        return rsi;
    }

    public CurrencyIndicatorsModel setRsi(List<Double> rsi) {
        this.rsi = rsi;
        return this;
    }

    public List<Double> getWr() {
        return wr;
    }

    public CurrencyIndicatorsModel setWr(List<Double> wr) {
        this.wr = wr;
        return this;
    }

    public List<Double> getStochRsiK() {
        return stochRsiK;
    }

    public CurrencyIndicatorsModel setStochRsiK(List<Double> stochRsiK) {
        this.stochRsiK = stochRsiK;
        return this;
    }

    public List<Double> getStochRsiD() {
        return stochRsiD;
    }

    public CurrencyIndicatorsModel setStochRsiD(List<Double> stochRsiD) {
        this.stochRsiD = stochRsiD;
        return this;
    }

    public IndicatorLatestModel getLatest() {
        return latest;
    }

    public CurrencyIndicatorsModel setLatest(IndicatorLatestModel latest) {
        this.latest = latest;
        return this;
    }
}
